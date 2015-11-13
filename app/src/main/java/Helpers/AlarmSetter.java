package Helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.Calendar;

import Model.Occurrence;
import Model.Reminder;
import teamproject.glasgow.reminders_app.AlarmReceiver;
import teamproject.glasgow.reminders_app.MyApp;
import teamproject.glasgow.reminders_app.Reminders;

/**
 * Created by Flo on 31/10/15.
 */
public abstract class AlarmSetter {

    public static void setRepeatingAlarmForReminder(Reminder reminder, Context context) {
        if(context == null) {
            context = MyApp.getContext();
        }
        for (Occurrence o : reminder.getOccurrences()) {
            cancleAllAlarmsForOccurrence(o, context);
            setAllAlarmsForOccurrence(o, context);
        }
    }

    public static void cancelRepeatingAlarmForReminder(Reminder reminder, Context context) {
        if(context == null) {
            context = MyApp.getContext();
        }
        for (Occurrence o : reminder.getOccurrences()) {
            cancleAllAlarmsForOccurrence(o, context);
        }
    }

    public static void setAllAlarmsForOccurrence(Occurrence o, Context context) {
        if(context == null) {
            context = MyApp.getContext();
        }
        Calendar calendar = Calendar.getInstance();

        int dateShift = 0;
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == Calendar.SUNDAY)
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.SUNDAY.ordinal()) % 7;
        if (dayofweek == Calendar.MONDAY)
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.MONDAY.ordinal()) % 7;
        if (dayofweek == Calendar.TUESDAY)
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.TUESDAY.ordinal()) % 7;
        if (dayofweek == Calendar.WEDNESDAY)
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.WEDNESDAY.ordinal()) % 7;
        if (dayofweek == Calendar.THURSDAY)
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.THURSDAY.ordinal()) % 7;
        if (dayofweek == Calendar.FRIDAY)
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.FRIDAY.ordinal()) % 7;
        if (dayofweek == Calendar.SATURDAY) {
            dateShift = (o.getDay().ordinal() - DAYSOFTHEWEEK.SATURDAY.ordinal()) % 7;
        }
        if (dateShift<0) { dateShift += 7;}
        if(dateShift == 0) {
            //if(Minutes.minutesBetween(LocalTime.now(), o.getTime()).getMinutes() <= 0) {
            if(LocalTime.now().isAfter(o.getTime())) {
                dateShift = 7;
            }
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, o.getTime().getHourOfDay());
        calendar.set(Calendar.MINUTE, o.getTime().getMinuteOfHour());
        calendar.set(Calendar.SECOND, o.getTime().getSecondOfMinute());
        calendar.set(Calendar.MILLISECOND, o.getTime().getMillisOfSecond());
        calendar.add(Calendar.DATE, dateShift);
        long l = calendar.getTimeInMillis();
        Intent intent1 = new Intent(context, AlarmReceiver.class);
        intent1.putExtra("Reminder", o.getReminder());
        intent1.putExtra("Occurrence", o);
        int alarmId = RandomNumberGen.getInstance().randomInt();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        o.getAlarmIds().add(alarmId);
        if(o.getNotificationFrequency() > 0) {
            for (int i = 1; i <= o.getNotificationFrequency(); i++) {
                alarmId = RandomNumberGen.getInstance().randomInt();
                pendingIntent = PendingIntent.getBroadcast(context,alarmId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                if(Calendar.getInstance().getTimeInMillis() <= (calendar.getTimeInMillis() - 20 * i * 60000)) {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 20 * i * 60000 , AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                } else {
                    if(dateShift == 0) {
                        dateShift = 7;
                    }
                    am.setRepeating(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis() - 20 * i * 60000) + AlarmManager.INTERVAL_DAY * dateShift , AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                }

                o.getAlarmIds().add(alarmId);
            }
        }
    }

    public static void cancleAllAlarmsForOccurrence(Occurrence o, Context context) {
        if(context == null) {
            context = MyApp.getContext();
        }
        for(int alarmId : o.getAlarmIds()) {
            Intent intent1 = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        }
        o.getAlarmIds().clear();
    }

}
