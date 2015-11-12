package teamproject.glasgow.reminders_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
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


    private final static String taskA = "Floss Teeth";
    private final static String taskB = "Do Survey";
    private static Model.Reminder taskAReminder = null;
    private static Model.Reminder taskBReminder = null;
    private static List<Model.Reminder> otherTaskReminders = new ArrayList<Model.Reminder>();
    private static int frequencyOfA, frequencyOfB, frequencyOfC, frequencyOfD,
            frequencyOfE, frequencyOfF;
    private static List<Occurrence> occurrencesOfA, occurrencesOfB, occurrencesOfC, occurrencesOfD,
            occurrencesOfE, occurrencesOfF;
    private final List<Model.Task> allTasks = PersistencyManager.getTasks();
    private List<Reminder> allReminders = MyApp.getReminders().getReminders();

    public static void treatment1() {
        populateTaskReminders();
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
        populateTaskReminders();
        NotificationManager.createNotification(null, "A Message From the Reminders App", "Notifications for " + taskA + " have been shut off.");

        occurrencesOfA = taskAReminder.getOccurrences();

        for (Occurrence occurrence : occurrencesOfA) {
            occurrence.setIsActive(false);
        }

    }

    public static void treatment3() {
        populateTaskReminders();
        NotificationManager.createNotification(null, "A Message From the Reminders App", "Notifications for " + taskA + " have been turned on.");
        occurrencesOfA = taskAReminder.getOccurrences();
        for (Occurrence occurrence : occurrencesOfA) {
            occurrence.setIsActive(true);
        }
        occurrencesOfB = taskBReminder.getOccurrences();

        Random randomGen = new Random();
//        int rand1 = randomGen.nextInt(occurrencesOfB.size());
//        int rand2 = randomGen.nextInt(occurrencesOfB.size());
//        int rand3 = randomGen.nextInt(occurrencesOfB.size());
//        int rand4 = randomGen.nextInt(occurrencesOfB.size());
//        int rand5 = randomGen.nextInt(occurrencesOfB.size());
//
//        //TODO: Debug this
        List<Integer> uniqueRandomNums = new ArrayList<Integer>();
//        uniqueRandomNums.add(rand1);
//        uniqueRandomNums.add(rand2);
//        uniqueRandomNums.add(rand3);
//        uniqueRandomNums.add(rand4);
//        uniqueRandomNums.add(rand5);

        int rand = 0;

        //TODO: test this randomNum algorithm
        for (int i = 0; i < 5; i++) {
            rand = randomGen.nextInt(occurrencesOfB.size());
            if (i < occurrencesOfB.size()) {
                boolean isRepeat = false;
                for (int j = 0; j < uniqueRandomNums.size(); j++) {
                    if (uniqueRandomNums.get(j) == rand){
                        isRepeat = true;
                        i--;
                    }
                }
                if (isRepeat == false) uniqueRandomNums.add(rand);
            }
        }



        for (int i = 0; i < uniqueRandomNums.size(); i++) {
            int aRandomNum = uniqueRandomNums.get(i);
            System.out.println("*****aRandomNum= "+aRandomNum);
            occurrencesOfB.get(aRandomNum).setIsActive(false);
        }
    }

    public static void experimentConclusion() {
        NotificationManager.createNotification(MyApp.getContext(), "The Experiment Is Finished!",
                "Please delete the application now.");
        if (MyApp.trialStillRunning()) {
            MyApp.getReminders().cancelAlarmsForAllReminders();
            MyApp.setTrialStillRunning(false);
        }
    }

    public static synchronized Reminder setNotificationFrequency(Reminder reminder) {

        populateTaskReminders();

        String taskName = reminder.getName();
        List<Occurrence> occurrences = reminder.getOccurrences();

        String taskC = otherTaskReminders.get(0).getName();
        String taskD = otherTaskReminders.get(1).getName();
        String taskE = otherTaskReminders.get(2).getName();
        String taskF = otherTaskReminders.get(3).getName();

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
        
        int userID;
        try {
            userID = Integer.parseInt(text.getText().toString());
        } catch (NumberFormatException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Entered User ID not valid")
                    .setMessage("Please make sure you provide a valid User ID")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        prefs.edit().putInt("user_id", userID).commit();


        System.out.println("*******Printing here!*******");

        switch (userID) {
            case 1:
                frequencyOfA = 0;
                frequencyOfB = 0;
                frequencyOfC = 0;
                frequencyOfD = 2;
                frequencyOfE = 0;
                frequencyOfF = 0;
                break;
            case 2:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 2;
                frequencyOfD = 0;
                frequencyOfE = 0;
                frequencyOfF = 0;
                break;
            case 3:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 1;
                frequencyOfD = 1;
                frequencyOfE = 0;
                frequencyOfF = 0;
                break;
            case 4:
                frequencyOfA = 0;
                frequencyOfB = 2;
                frequencyOfC = 0;
                frequencyOfD = 1;
                frequencyOfE = 1;
                frequencyOfF = 0;
                break;
            case 5:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 0;
                frequencyOfD = 1;
                frequencyOfE = 0;
                frequencyOfF = 1;
                break;
            case 6:
                frequencyOfA = 2;
                frequencyOfB = 0;
                frequencyOfC = 0;
                frequencyOfD = 0;
                frequencyOfE = 1;
                frequencyOfF = 1;
                break;
            case 7:
                frequencyOfA = 0;
                frequencyOfB = 0;
                frequencyOfC = 1;
                frequencyOfD = 1;
                frequencyOfE = 1;
                frequencyOfF = 1;
                break;
            case 8:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 0;
                frequencyOfD = 2;
                frequencyOfE = 1;
                frequencyOfF = 1;
                break;
            case 9:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 0;
                frequencyOfD = 1;
                frequencyOfE = 2;
                frequencyOfF = 1;
                break;
            case 10:
                frequencyOfA = 0;
                frequencyOfB = 2;
                frequencyOfC = 0;
                frequencyOfD = 1;
                frequencyOfE = 1;
                frequencyOfF = 2;
                break;
            case 11:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 0;
                frequencyOfD = 0;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 12:
                frequencyOfA = 2;
                frequencyOfB = 0;
                frequencyOfC = 0;
                frequencyOfD = 2;
                frequencyOfE = 0;
                frequencyOfF = 2;
                break;
            case 13:
                frequencyOfA = 0;
                frequencyOfB = 0;
                frequencyOfC = 1;
                frequencyOfD = 2;
                frequencyOfE = 1;
                frequencyOfF = 2;
                break;
            case 14:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 1;
                frequencyOfD = 1;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 15:
                frequencyOfA = 2;
                frequencyOfB = 2;
                frequencyOfC = 0;
                frequencyOfD = 2;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 16:
                frequencyOfA = 0;
                frequencyOfB = 2;
                frequencyOfC = 2;
                frequencyOfD = 0;
                frequencyOfE = 2;
                frequencyOfF = 2;
                break;
            case 17:
                frequencyOfA = 1;
                frequencyOfB = 1;
                frequencyOfC = 2;
                frequencyOfD = 2;
                frequencyOfE = 0;
                frequencyOfF = 2;
                break;
            case 18:
                frequencyOfA = 2;
                frequencyOfB = 0;
                frequencyOfC = 2;
                frequencyOfD = 2;
                frequencyOfE = 2;
                frequencyOfF = 0;
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

    public static synchronized void populateTaskReminders() {
        List<Reminder> reminders = MyApp.getReminders().getReminders();
        for (int i = 0; i < reminders.size(); i++) {

            Model.Reminder reminder = reminders.get(i);
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
    }
}
