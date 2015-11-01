package Helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import teamproject.glasgow.reminders_app.MyApp;
import teamproject.glasgow.reminders_app.R;
import teamproject.glasgow.reminders_app.Reminders;

/**
 * Created by Flo on 31/10/15.
 */
public abstract class NotificationManager {

    public static int notificationID = 0;

    public static void createNotification(Context context, String title, String text) {

        if(context == null) {
            context = MyApp.getContext();
        }

        Intent notificationIntent = new Intent(context, Reminders.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(text).setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(notificationID, mNotifyBuilder.build());
        notificationID++;
    }
}
