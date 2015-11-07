package teamproject.glasgow.reminders_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Helpers.NotificationManager;
import Helpers.PersistencyManager;
import Model.Occurrence;
import Model.Reminder;

public class ExperimentSetup extends AppCompatActivity {


    private final static String taskA = "Take Multivitamin";
    private final static String taskB = "Take a Nap";
    private static Model.Reminder taskAReminder = null;
    private static Model.Reminder taskBReminder = null;
    private static List<Model.Reminder> otherTaskReminders = new ArrayList<Model.Reminder>();
    private static int frequencyOfA = 0, frequencyOfB = 0, frequencyOfC = 0, frequencyOfD = 0,
            frequencyOfE = 0, frequencyOfF = 0;
    private static List<Occurrence> occurrencesOfA, occurrencesOfB, occurrencesOfC, occurrencesOfD,
            occurrencesOfE, occurrencesOfF;
    private final int participantID = MyApp.getUserID();
    private final List<Model.Task> allTasks = PersistencyManager.getTasks();
    private List<Reminder> allReminders = MyApp.getReminders().getReminders();

    public static void treatment1() {
        //Assign treatment 1 frequencies
        occurrencesOfA = taskAReminder.getOccurrences();
        occurrencesOfB = taskBReminder.getOccurrences();
        occurrencesOfC = otherTaskReminders.get(0).getOccurrences();
        occurrencesOfD = otherTaskReminders.get(1).getOccurrences();
        occurrencesOfE = otherTaskReminders.get(2).getOccurrences();
        occurrencesOfF = otherTaskReminders.get(3).getOccurrences();

        for (Occurrence occurrence : occurrencesOfA) {
            occurrence.setNotificationFrequency(frequencyOfA);
        }

        for (Occurrence occurrence : occurrencesOfB) {
            occurrence.setNotificationFrequency(frequencyOfB);
        }

        for (Occurrence occurrence : occurrencesOfC) {
            occurrence.setNotificationFrequency(frequencyOfC);
        }

        for (Occurrence occurrence : occurrencesOfD) {
            occurrence.setNotificationFrequency(frequencyOfD);
        }

        for (Occurrence occurrence : occurrencesOfE) {
            occurrence.setNotificationFrequency(frequencyOfE);
        }

        for (Occurrence occurrence : occurrencesOfF) {
            occurrence.setNotificationFrequency(frequencyOfF);
        }
    }

    public static void treatment2() {
        NotificationManager.createNotification(null, "A Message From the Reminders App Due to certain circumstances of the experiment,\" +\n" +
                "                \"we will be turning off notifications associated with \"+taskA+\". In the mean time, please try and remember to do \" +\n" +
                "                \"it on your own. Sorry for the inconvenience.", "Due to certain circumstances of the experiment," +
                "we will be turning off notifications associated with " + taskA + ". In the mean time, please try and remember to do " +
                "it on your own. Sorry for the inconvenience.");


        occurrencesOfA = taskAReminder.getOccurrences();
        for (Occurrence occurrence : occurrencesOfA) {
            occurrence.setIsActive(false);
        }
    }

    public static void treatmenr3() {
        NotificationManager.createNotification(null, "A Message From the Reminders App", "We have turned the notifications for " +
                "back on. Thanks for your patience.");
        occurrencesOfA = taskAReminder.getOccurrences();
        for (Occurrence occurrence : occurrencesOfA) {
            occurrence.setIsActive(true);
        }
        occurrencesOfB = taskBReminder.getOccurrences();

        Random randomGen = new Random();
        int rand1 = randomGen.nextInt(occurrencesOfB.size());
        int rand2 = randomGen.nextInt(occurrencesOfB.size());
        int rand3 = randomGen.nextInt(occurrencesOfB.size());
        int rand4 = randomGen.nextInt(occurrencesOfB.size());
        int rand5 = randomGen.nextInt(occurrencesOfB.size());

        List<Integer> randomNums = new ArrayList<Integer>();
        int rand = 0;

        //TODO: test this randomNum algorithm
        for (int i = 0; i < 5; i++) {
            rand = randomGen.nextInt(occurrencesOfB.size());
            if (i < occurrencesOfB.size()) {
                boolean isRepeat = false;
                for (int j = 0; j < randomNums.size(); j++) {
                    if (randomNums.get(j) == rand) isRepeat = true;
                }
                if (isRepeat == false) randomNums.add(rand);
            }
        }

        for (int i = 0; i < randomNums.size(); i++) {
            int aRandomNum = randomNums.get(i);
            occurrencesOfB.get(aRandomNum).setIsActive(false);
        }
    }

    public static void experimentConclusion() {
        NotificationManager.createNotification(MyApp.getContext(), "The Experiment Is Finished!",
                "Thank you for participating in our experiment. The experiment has now ended. Please" +
                        " make sure to delete the Reminders application from your phone as it is not" +
                        "designed to be used after this point. The last part of the study is a short" +
                        "exit survey. If you have not already made arrangements to do this, then" +
                        "please contact us as soon as possible. Thank you again for your participation!");
    }

    public static synchronized Reminder setNotificationFrequency(Reminder reminder) {

        String taskName = reminder.getName();
        List<Occurrence> occurrences = reminder.getOccurrences();

        String taskC = otherTaskReminders.get(0).getName();
        String taskD = otherTaskReminders.get(0).getName();
        String taskE = otherTaskReminders.get(0).getName();
        String taskF = otherTaskReminders.get(0).getName();

        if (taskName.equals(taskA)) {
            for (Occurrence occurrence : occurrences) {
                occurrence.setNotificationFrequency(frequencyOfA);
            }
        }

        if (taskName.equals(taskB)) {
            for (Occurrence occurrence : occurrences) {
                occurrence.setNotificationFrequency(frequencyOfB);
            }
        }

        if (taskName.equals(taskC)) {
            for (Occurrence occurrence : occurrences) {
                occurrence.setNotificationFrequency(frequencyOfC);
            }
        }

        if (taskName.equals(taskD)) {
            for (Occurrence occurrence : occurrences) {
                occurrence.setNotificationFrequency(frequencyOfD);
            }
        }

        if (taskName.equals(taskE)) {
            for (Occurrence occurrence : occurrences) {
                occurrence.setNotificationFrequency(frequencyOfE);
            }
        }

        if (taskName.equals(taskF)) {
            for (Occurrence occurrence : occurrences) {
                occurrence.setNotificationFrequency(frequencyOfF);
            }
        }
        return reminder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_setup);
    }

    public void start(View view) {
        SharedPreferences prefs = getSharedPreferences("teamproject.glasgow.reminders_app", MODE_PRIVATE);

        EditText text = (EditText) findViewById(R.id.user_id);
        //TODO: App crashes if this is not an int.
        int userID = Integer.parseInt(text.getText().toString());
        prefs.edit().putInt("user_id", userID).commit();


        System.out.println("*******Printing here!*******");

        switch (participantID) {
            case 1:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 1;
                frequencyOfD = 3;
                frequencyOfE = 1;
                frequencyOfF = 1;
                break;
            case 2:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 3;
                frequencyOfD = 1;
                frequencyOfE = 1;
                frequencyOfF = 1;
                break;
            case 3:
                frequencyOfA = 3;
                frequencyOfB = 3;
                frequencyOfC = 2;
                frequencyOfD = 2;
                frequencyOfE = 1;
                frequencyOfF = 1;
                break;
            case 4:
                frequencyOfA = 1;
                frequencyOfB = 3;
                frequencyOfC = 1;
                frequencyOfD = 2;
                frequencyOfE = 2;
                frequencyOfF = 1;
                break;
            case 5:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 1;
                frequencyOfD = 2;
                frequencyOfE = 1;
                frequencyOfF = 2;
                break;
            case 6:
                frequencyOfA = 3;
                frequencyOfB = 1;
                frequencyOfC = 1;
                frequencyOfD = 1;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 7:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 2;
                frequencyOfD = 2;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 8:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 1;
                frequencyOfD = 3;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 9:
                frequencyOfA = 3;
                frequencyOfB = 3;
                frequencyOfC = 1;
                frequencyOfD = 2;
                frequencyOfE = 3;
                frequencyOfF = 2;
                break;
            case 10:
                frequencyOfA = 1;
                frequencyOfB = 3;
                frequencyOfC = 1;
                frequencyOfD = 2;
                frequencyOfE = 2;
                frequencyOfF = 3;
                break;
            case 11:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 1;
                frequencyOfD = 1;
                frequencyOfE = 3;
                frequencyOfF = 3;
                break;
            case 12:
                frequencyOfA = 3;
                frequencyOfB = 1;
                frequencyOfC = 1;
                frequencyOfD = 3;
                frequencyOfE = 1;
                frequencyOfF = 3;
                break;
            case 13:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 2;
                frequencyOfD = 3;
                frequencyOfE = 2;
                frequencyOfF = 3;
                break;
            case 14:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 2;
                frequencyOfD = 2;
                frequencyOfE = 3;
                frequencyOfF = 3;
                break;
            case 15:
                frequencyOfA = 3;
                frequencyOfB = 3;
                frequencyOfC = 1;
                frequencyOfD = 3;
                frequencyOfE = 3;
                frequencyOfF = 3;
                break;
            case 16:
                frequencyOfA = 1;
                frequencyOfB = 3;
                frequencyOfC = 3;
                frequencyOfD = 1;
                frequencyOfE = 3;
                frequencyOfF = 3;
                break;
            case 17:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 3;
                frequencyOfD = 3;
                frequencyOfE = 1;
                frequencyOfF = 3;
                break;
            case 18:
                frequencyOfA = 3;
                frequencyOfB = 1;
                frequencyOfC = 3;
                frequencyOfD = 3;
                frequencyOfE = 3;
                frequencyOfF = 1;
                break;
            default:
                break;
        }


        for (int i = 0; i < allReminders.size(); i++) {

            Model.Reminder reminder = allReminders.get(i);
            String reminderName = reminder.getTask().getName();

            if (reminderName.equals(taskA)) {
                taskAReminder = reminder;
            }

            if (reminderName.equals(taskB)) {
                taskBReminder = reminder;
            }

            if (reminderName != null && !reminderName.equals(taskA) && !reminderName.equals(taskB)) {
                otherTaskReminders.add(reminder);
            }
        }


        treatment1();


        ExperimentAlarmSetter.waitUntil19th();


        ExperimentAlarmSetter.waitUntil23rd();

        ExperimentAlarmSetter.waitUntil27th();


        // setup the design here
        // you can get the reminders via MyApp.getReminders() and userID via MyApp.getUserID()
        // if you want to wake up the system at certain times to change occ look into the AlarmSetter and AlarmReceiver classes
        // a reboot cancels your alarms so you'd also have to do something similar to what I did in BootReceiver to restore your previously set Alarms
        finish();
    }
}
