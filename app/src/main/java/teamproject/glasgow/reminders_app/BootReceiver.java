package teamproject.glasgow.reminders_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import Controllers.ExpandListAdapter;
import Helpers.AlarmSetter;
import Helpers.PersistencyManager;
import Model.*;

/**
 * Created by Flo on 31/10/15.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        MyApp.initOnBroadCastReceiver(context);

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /*
            Intent i = new Intent(context, Reminders.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("restart",true);
            context.startActivity(i);
            */

            Model.Reminders reminders = MyApp.getReminders();

            SharedPreferences prefs = MyApp.getPrefs();

            if (prefs.getBoolean("notfixed_1", true)) {
                for (Reminder reminder : reminders.getReminders()) {
                    ExperimentSetup.setNotificationFrequency(reminder);
                }
                Log.d("Fix","applied");
                prefs.edit().putBoolean("notfixed_1", false).commit();
            }

            for (Reminder reminder : reminders.getReminders()) {
                AlarmSetter.setRepeatingAlarmForReminder(reminder, null);
            }

            //TODO: Test this
            ExperimentAlarmSetter.waitUntil19th();
            ExperimentAlarmSetter.waitUntil23rd();
            ExperimentAlarmSetter.waitUntil27th();
            PersistencyManager.saveReminders(reminders, false);
        }
    }
}
