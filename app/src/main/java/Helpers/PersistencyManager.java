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
        return r;
        //replace with data from DB
    }

    public static void saveReminders(Reminders reminders, boolean firstTime) {
        //save reminders including all associated objects (Tasks, Occurrences)
        LocalStorageAdapter db = new LocalStorageAdapter();
            db.deleteAllData();
        for (int i=0;i< reminders.getReminders().size();i++) {
            Reminder reminder = reminders.getReminders().get(i);
            db.createReminder(reminder, firstTime);
        }
        db.closeDB();
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

        Task task = new Task("Floss Teeth");
        tasks.add(task);

        task = new Task("Do Survey");
        tasks.add(task);

        task = new Task("Eat a snack");
        tasks.add(task);

        task = new Task("Send Evaluator an Email");
        tasks.add(task);

        task = new Task("Make Bed");
        tasks.add(task);

        task = new Task("Eat out");
        tasks.add(task);

        return tasks;
    }

    public static void logSurveyCompletetion() {
        // log completion of survey
        // to get userID call MyApp.getUserID();
        Log.d("Nok", "Log Survey Completetion");
        ParseStorageAdapter db_cloud = MyApp.getDBcloud();
        db_cloud.createLogTaskComplete(MyApp.getUserID(), new Task("Do survey"));
    }

    public static void logAlert(Task task) {
        // log alart raised
        // to get userID call MyApp.getUserID();
        Log.d("Nok", "Log Alert");
        ParseStorageAdapter db_cloud = MyApp.getDBcloud();
        db_cloud.createLogAlert(MyApp.getUserID(), task);
    }

    public static void logTaskCompletetion(Task task) {
        // log task completion
        // to get userID call MyApp.getUserID();
        Log.d("Nok", "Log Task Completetion");
        ParseStorageAdapter db_cloud = MyApp.getDBcloud();
        db_cloud.createLogTaskComplete(MyApp.getUserID(), task);
    }
}
