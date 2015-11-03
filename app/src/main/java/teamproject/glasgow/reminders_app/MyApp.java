package teamproject.glasgow.reminders_app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import Model.*;
import Model.Reminders;

/**
 * Created by Flo on 31/10/15.
 */
public class MyApp extends Application {
    private static Application instance;
    private static Model.Reminders reminders;
    private static SharedPreferences prefs = null;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences("teamproject.glasgow.reminders_app", MODE_PRIVATE);
        instance = this;
        MyApp.context = getApplicationContext();
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static synchronized Reminders getReminders() {
        return reminders;
    }

    public static synchronized void setReminders(Reminders reminders) {
        MyApp.reminders = reminders;
    }

    public static int getUserID() {
        return prefs.getInt("user_id", -1);
    }

    private static Context context;

    public static Context getAppContext() {
        return MyApp.context;
    }
}