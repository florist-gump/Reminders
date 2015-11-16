package teamproject.glasgow.reminders_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Helpers.AlarmSetter;
import Helpers.PersistencyManager;
import Model.Reminder;

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

            for (Reminder reminder : MyApp.getReminders().getReminders()) {
                AlarmSetter.setRepeatingAlarmForReminder(reminder, null);
            }

            //TODO: Test this
            ExperimentAlarmSetter.waitUntil19th();
            ExperimentAlarmSetter.waitUntil23rd();
            ExperimentAlarmSetter.waitUntil27th();
        }
    }
}
