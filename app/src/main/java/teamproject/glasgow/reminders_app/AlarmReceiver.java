package teamproject.glasgow.reminders_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import Helpers.PersistencyManager;
import Model.Occurrence;
import Model.Reminder;
import teamproject.glasgow.reminders_app.R;
import teamproject.glasgow.reminders_app.Reminders;

/**
 * Created by Flo on 17/10/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApp.initOnBroadCastReceiver(context);

        Reminder reminder = (Reminder)intent.getSerializableExtra("Reminder");
        Occurrence occurrence = (Occurrence)intent.getSerializableExtra("Occurrence");

        //get updated version of occurrence
        if(MyApp.getReminders().getOccurenceFromId(occurrence.getId() )!= null) {
            occurrence = MyApp.getReminders().getOccurenceFromId(occurrence.getId());
            reminder = occurrence.getReminder();
        }
        else {
            return;
        }

        if(reminder != null && occurrence != null) {
            if(reminder.getTask() != null) {
                PersistencyManager.logAlert(reminder.getTask());
            }
            if(!occurrence.getIsActive()) {return;}
            Helpers.NotificationManager.createNotification(context,"Reminder for you",reminder.getName());
        }
    }

}

