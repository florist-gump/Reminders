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

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Reminder reminder = (Reminder)intent.getSerializableExtra("Reminder");
        Occurrence occurrence = (Occurrence)intent.getSerializableExtra("Occurrence");
        if(!occurrence.getIsActive()) {return;}

        Intent notificationIntent = new Intent(context, Reminders.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent finishedTaskIntent = new Intent("Finish");
        finishedTaskIntent.putExtra("Reminder",reminder);
        finishedTaskIntent.putExtra("Notification",notificationID);
        
        PendingIntent finishedTaskPendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), finishedTaskIntent, 0);

        Intent postponeIntent = new Intent("Postpone");
        postponeIntent.putExtra("Reminder",reminder);
        postponeIntent.putExtra("Occurrence",occurrence);
        finishedTaskIntent.putExtra("Notification",notificationID);
        PendingIntent postponePendingIntent = PendingIntent.getBroadcast(context,(int) System.currentTimeMillis(),postponeIntent,0 );

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Reminder for you")
                .setContentText(reminder.getName()).setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_snooze_grey_600_24dp, "remind me in 10 minutes", postponePendingIntent)
                .addAction(R.drawable.ic_done_grey_600_24dp, "log completion", finishedTaskPendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(notificationID, mNotifyBuilder.build());
        notificationID++;

    }

}

