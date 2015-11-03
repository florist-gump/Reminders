package Helpers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import Controllers.LocalStorageAdapter;
import Controllers.ParseStorageAdapter;
import Model.Reminder;
import Model.Reminders;
import Model.SurveyQuestion;
import Model.Task;
import teamproject.glasgow.reminders_app.MyApp;

/**
 * Created by Flo on 17/10/15.
 */
public abstract class PersistencyManager {

    public static Reminders getReminders() {
        LocalStorageAdapter db = new LocalStorageAdapter();
        Reminders r = db.getAllReminders();
        db.closeDB();

//        return HelperFunctions.generateReminderDummmyData();
        return r;
        //replace with data from DB
    }

    public static void saveReminders(Reminders reminders) {
        //save reminders including all associated objects (Tasks, Occurrences)
        LocalStorageAdapter db = new LocalStorageAdapter();
            db.deleteAllData();
        for (int i=0;i< reminders.getReminders().size();i++) {
            Reminder reminder = reminders.getReminders().get(i);
//            Log.d("Nok", "save reminder: "+reminder.getName()+" "+reminder.getOccurrences().size());
            db.createReminder(reminder);
        }
        db.closeDB();
    }

//    public static Reminder getReminder(String task_name) {
//        Reminder r = db.getReminders(task_name);
//        db.closeDB();
//
//        return r;
//        //replace with data from DB
//    }
//
//    public static void updateReminder(Reminder reminder) {
//        db.updateReminder(reminder);
//        db.closeDB();
//
//        return;
//        //replace with data from DB
//    }
//
//    public static void deleteReminder(Reminder reminder) {
//        db.deleteReminder(reminder);
//        db.closeDB();
//
//        return;
//        //replace with data from DB
//    }



//    public static void saveReminder(Reminder reminder) {
//        //save reminders including all associated objects (Tasks, Occurrences)
//        Log.d("Nok", "From PersistencyManager.java save reminder: " + reminder.getName());
//        db.createReminder(reminder);
//    }

    public static ArrayList<SurveyQuestion> getSurveyQuestions() {
        return createSurveyQuestions();
    }

    private static ArrayList<SurveyQuestion> createSurveyQuestions() {
        ArrayList<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();

        SurveyQuestion question = new SurveyQuestion("How is your mood",0);
        questions.add(question);

        question = new SurveyQuestion("How did you sleep last night",0);
        questions.add(question);

        question = new SurveyQuestion("How is your stress level",0);
        questions.add(question);

        question = new SurveyQuestion("How is your workload",0);
        questions.add(question);

        return questions;
    }

    public static ArrayList<Task> getTasks() {
        return createTasks();
    }

    private static ArrayList<Task> createTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Task task = new Task("Take Multivitamin");
        tasks.add(task);

        task = new Task("Take a Nap");
        tasks.add(task);

        task = new Task("Study");
        tasks.add(task);

        task = new Task("Return Library Book");
        tasks.add(task);

        task = new Task("Meeting with Evaluator");
        tasks.add(task);

        task = new Task("Make bed");
        tasks.add(task);

        return tasks;
    }

    public static void logSurveyCompletetion() {
        // log completion of survey
        // to get userID call MyApp.getUserID();
        Log.d("Nok", "Log Survey Completetion");
        ParseStorageAdapter db_cloud = MyApp.getDBcloud();
//        ParseStorageAdapter db_cloud = new ParseStorageAdapter();
        db_cloud.createLogTaskComplete(MyApp.getUserID(), new Task("Do survey"));
    }

    public static void logAlert(Task task) {
        // log alart raised
        // to get userID call MyApp.getUserID();
        Log.d("Nok", "Log Alert");
        ParseStorageAdapter db_cloud = MyApp.getDBcloud();
//        ParseStorageAdapter db_cloud = new ParseStorageAdapter();
        db_cloud.createLogAlert(MyApp.getUserID(), task);
    }

    public static void logTaskCompletetion(Task task) {
        // log task completion
        // to get userID call MyApp.getUserID();
        Log.d("Nok", "Log Task Completetion");
        ParseStorageAdapter db_cloud = MyApp.getDBcloud();
//        ParseStorageAdapter db_cloud = new ParseStorageAdapter();
//        db_cloud.testAddNewTaskToDB();
        Log.d("Nok", "user_ID: "+MyApp.getUserID());
        db_cloud.createLogTaskComplete(MyApp.getUserID(), task);
    }
}
