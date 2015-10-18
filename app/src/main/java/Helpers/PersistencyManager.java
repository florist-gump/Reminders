package Helpers;

import org.joda.time.LocalTime;

import java.util.ArrayList;

import Model.SurveyQuestion;
import Model.Task;

/**
 * Created by Flo on 17/10/15.
 */
public abstract class PersistencyManager {

    public static ArrayList<SurveyQuestion> getSurveyQuestions() {
        return createDummySurveyQuestions();
    }

    private static ArrayList<SurveyQuestion> createDummySurveyQuestions() {
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
        return createDummyTasks();
    }

    private static ArrayList<Task> createDummyTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Task task = new Task("Take Multivitamin");
        tasks.add(task);

        task = new Task("Take a Nap");
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

    public static void logSurveyCompletetion(LocalTime time) {
        // log completion of survey
    }

    public static void logTaskCompletetion(Task task) {
        // log task completion
    }
}
