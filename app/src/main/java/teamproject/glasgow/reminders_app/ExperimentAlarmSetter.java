package teamproject.glasgow.reminders_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import Helpers.RandomNumberGen;


/**
 * Created by joshuamarsh on 11/5/15.
 */
public class ExperimentAlarmSetter {

    static Calendar calendar = Calendar.getInstance();
    private static PendingIntent pendingIntent;
    private static Context context = MyApp.getContext();
    private static int randAlarmID = RandomNumberGen.getInstance().randomInt();


    //TODO: test that these work
    public static void waitUntil19th(){
        calendar.set(2015, Calendar.NOVEMBER, 19, 12, 0);

        Intent intent = new Intent(context, Treatment2Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, randAlarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void waitUntil23rd(){
        calendar.set(2015, Calendar.NOVEMBER, 23, 12, 0);

        Intent intent = new Intent(context, Treatment3Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, randAlarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void waitUntil27th(){
        calendar.set(2015, Calendar.NOVEMBER, 27, 12, 0);

        Intent intent = new Intent(context, ExperimentConclusionReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, randAlarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    
}
