package teamproject.glasgow.reminders_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import Helpers.RandomNumberGen;


/**
 * Created by joshuamarsh on 11/5/15.
 */
public class ExperimentAlarmSetter {

    static Calendar calendar = Calendar.getInstance();
    private static PendingIntent pendingIntent;
    private static Context context = MyApp.getContext();
    private static int randAlarmID = RandomNumberGen.getInstance().randomInt();

    public static void waittill23rd(){
        //calendar.set(2015,11,23);
        calendar.set(2015, Calendar.JANUARY, 5, 20, 42, 0);
        Intent intent = new Intent(context, ExperimentAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, randAlarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void waittill30th(){
        calendar.set(2015,11,30);

    }
}
