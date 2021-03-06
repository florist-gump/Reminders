package teamproject.glasgow.reminders_app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import Controllers.ParseStorageAdapter;
import Helpers.PersistencyManager;
import Model.*;
import Model.Reminders;

/**
 * Created by Flo on 31/10/15.
 */
public class MyApp extends Application {
    private static Application instance;
    private static Model.Reminders reminders;
    private static SharedPreferences prefs = null;
    private static Context context;
    private static ParseStorageAdapter db_cloud;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("teamproject.glasgow.reminders_app", MODE_PRIVATE);
        instance = this;
        MyApp.context = getApplicationContext();
        db_cloud = new ParseStorageAdapter();
    }

    public static void initOnBroadCastReceiver(Context context) {
        if(instance == null) {
            instance = new MyApp();
        }
        if(prefs == null ) {
            prefs = context.getSharedPreferences("teamproject.glasgow.reminders_app", MODE_PRIVATE);
        }
        if(context == null) {
            MyApp.context = context;
        }
        if(reminders == null) {
            reminders = PersistencyManager.getReminders();
        }
        Log.d("Context:",context.toString());
    }

    public static Context getContext() {
        if(instance != null) {
            return instance.getApplicationContext();
        } else {
            return context;
        }
    }

    public static synchronized Reminders getReminders() {
        for(Reminder reminder : reminders.getReminders()) {
            for(Occurrence o : reminder.getOccurrences()) {
                for(Integer i : o.getAlarmIds()) {
                    Log.d("x1 alarmids getRem", o.getReminder().getName() + o.getDay() + i.toString());
                }
                Log.d("x1 getRem active", o.getIsActive().toString());
                Log.d("x1 getRem freq", Integer.toString(o.getNotificationFrequency()));
            }
        }
        return reminders;
    }

    public static synchronized void setReminders(Reminders reminders) {
        MyApp.reminders = reminders;
    }

    public static int getUserID() {
        return prefs.getInt("user_id", -1);
    }

    public static void setUserID(int userID) {
        prefs.edit().putInt("user_id", userID).commit();
    }

    public static boolean trialStillRunning() {
        return prefs.getBoolean("trialrunning", true);
    }

    public static void setTrialStillRunning(boolean b) {
        prefs.edit().putBoolean("trialrunning", b).commit();
    }

    public static SharedPreferences getPrefs() {
        return MyApp.prefs;
    }

    public static Context getAppContext() {
        return MyApp.context;
    }

    public static ParseStorageAdapter getDBcloud() {
        return db_cloud;
    }

}