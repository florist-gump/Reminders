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

    private static LocalStorageAdapter db;

    public static Reminders getReminders() {
        Reminders r = HelperFunctions.generateReminderInitData();
        for (Reminder reminder : r.getReminders()) {
           Log.d("Nok", Long.toString(db.createReminder(reminder)));
        }
//        r = db.getAllReminders();
        db.closeDB();

        return HelperFunctions.generateReminderDummmyData();
//        return r;
        //replace with data from DB
    }
    public static void saveReminders(Reminders reminders) {
        //save reminders including all associated objects (Tasks, Occurrences)
        for (Reminder reminder : reminders.getReminders()) {
            db.createReminder(reminder);
        }
    }

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
        ParseStorageAdapter db_cloud = new ParseStorageAdapter();
        db_cloud.createLogTaskComplete(MyApp.getUserID(), new Task("Do survey"));
    }

    public static void logAlert(Task task) {
        // log alart raised
        // to get userID call MyApp.getUserID();
        ParseStorageAdapter db_cloud = new ParseStorageAdapter();
        db_cloud.createLogAlert(MyApp.getUserID(), task);
    }

    public static void logTaskCompletetion(Task task) {
        // log task completion
        // to get userID call MyApp.getUserID();
        ParseStorageAdapter db_cloud = new ParseStorageAdapter();
        db_cloud.createLogTaskComplete(MyApp.getUserID(), task);
    }
}
