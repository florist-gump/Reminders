package Controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.LocalTime;

import Helpers.DAYSOFTHEWEEK;
import Model.Occurrence;
import Model.Reminder;
import Model.Reminders;
import Model.Task;
import teamproject.glasgow.reminders_app.MyApp;

import static Helpers.DAYSOFTHEWEEK.*;

/**
 * Created by ttnok on 1/11/2558.
 */

public class LocalStorageAdapter extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "Nok";//LocalStorageAdapter.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "reminders";

    // Table Names
    private static final String TABLE_REMINDERS = "reminders";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_OCCURRENCES = "occurrences";
    private static final String TABLE_ALARMIDS = "alarmids";

    // Column names
    private static final String KEY_ID = "id";
//    private static final String KEY_CREATED_AT = "created_at";
//    private static final String KEY_UPDATED_AT = "updated_at";

    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_COMPLETE_LOG = "last_complete_log";

    private static final String KEY_DAY = "day";
    private static final String KEY_TIME = "time";
    private static final String KEY_NOTIFICATION_FREQUENCY = "notification_frequency";
    private static final String KEY_REMINDER_ID = "reminder_id";
    private static final String KEY_STATUS = "status";

    private static final String KEY_OCCURRENCE_ID = "occurrence_id";
    private static final String KEY_VALUE_ALAREMID = "value_alarmID";

    // Table Create Statements
    // AlarmsIDs table create statement
    private static final String CREATE_TABLE_REMINDERS = "CREATE TABLE " + TABLE_REMINDERS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" +  ")";
    // Tasks table create statement
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_REMINDER_ID + " INTEGER," + KEY_NAME
            + " TEXT," + KEY_LAST_COMPLETE_LOG + " DATETIME" + ")";

    // Occurrences table create statement
    private static final String CREATE_TABLE_OCCURRENCES = "CREATE TABLE " + TABLE_OCCURRENCES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_REMINDER_ID + " INTEGER,"
            + KEY_DAY + " TEXT," + KEY_TIME + " DATETIME," + KEY_NOTIFICATION_FREQUENCY + " INTEGER," + KEY_STATUS + " BOOLEAN"
            + ")";

    // AlarmsIDs table create statement
    private static final String CREATE_TABLE_ALARMIDS = "CREATE TABLE " + TABLE_ALARMIDS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OCCURRENCE_ID + " INTEGER,"
            + KEY_VALUE_ALAREMID + " INTEGER" +  ")";

    public LocalStorageAdapter() {
        super(MyApp.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_REMINDERS);
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_OCCURRENCES);
        db.execSQL(CREATE_TABLE_ALARMIDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OCCURRENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMIDS);
        // create new tables
        onCreate(db);
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        int rr = db.delete(TABLE_REMINDERS, null, null);
//        int tr = db.delete(TABLE_TASKS, null, null);
        int or = db.delete(TABLE_OCCURRENCES, null, null);
        int ar = db.delete(TABLE_ALARMIDS, null, null);
    }

    public long createReminder(Reminder reminder, boolean firstTime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, reminder.getName());
        long reminder_id = db.insert(TABLE_REMINDERS, null, values);
        // insert Tasks
        if(reminder.getTask()!=null && firstTime){
            createTask((int)reminder_id, reminder.getTask());
        }

        ArrayList<Occurrence> occurrences = reminder.getOccurrences();
        // insert Occurrences
        for (Occurrence occurrence : occurrences) {
            createOccurrence((int)reminder_id, occurrence);
        }

        return reminder_id;
    }
    public long createTask(int reminder_id, Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_REMINDER_ID, reminder_id);
        if(task.getLastCompletionLog() != null) {
            values.put(KEY_LAST_COMPLETE_LOG, String.valueOf(task.getLastCompletionLog()));
        }else{
            values.put(KEY_LAST_COMPLETE_LOG, "null");
        }


        // insert row
        long task_id = db.insert(TABLE_TASKS, null, values);
//        Log.d("Nok", "task_id: "+task_id+" value set: "+ values.valueSet());
        return task_id;
    }

    public long createOccurrence(int reminder_id, Occurrence occurrence) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REMINDER_ID, reminder_id);
        values.put(KEY_DAY, String.valueOf(occurrence.getDay()));
        values.put(KEY_TIME, String.valueOf(occurrence.getTime()));
        values.put(KEY_NOTIFICATION_FREQUENCY, occurrence.getNotificationFrequency());
        values.put(KEY_STATUS, occurrence.getIsActive());

        // insert row
        long occurrence_id = db.insert(TABLE_OCCURRENCES, null, values);

        // insert AlarmID
        for (int alarmID :occurrence.getAlarmIds() ) {
            createAlarmID(alarmID, (int) occurrence_id);
        }

        return occurrence_id;
    }

    public long createAlarmID(int alarmID_value, int occurrence_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OCCURRENCE_ID, occurrence_id);
        values.put(KEY_VALUE_ALAREMID, alarmID_value);

        long alarmID_value_id = db.insert(TABLE_ALARMIDS, null, values);

        return alarmID_value_id;
    }

    public int getReminderID(String reminder_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_NAME + " = '" + reminder_name+"'";
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return c.getInt(c.getColumnIndex(KEY_ID));
    }

    public Reminders getAllReminders() {
        Reminders reminders = new Reminders();
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Reminder reminder = new Reminder(c.getString(c.getColumnIndex(KEY_NAME)));
                int reminder_id = getReminderID(reminder.getName());
                Task t = getTask(reminder_id);
                reminder.setTask(t);
                ArrayList <Occurrence> occurrences = getAllOccurrences(reminder_id, reminder );
                reminder.setOccurrences(occurrences);
                reminders.addReminder(reminder);
            } while (c.moveToNext());
        }

        return reminders;
    }

    public Task getTask(int reminder_id) {
        Task t = null;
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS +" WHERE "+KEY_REMINDER_ID +"="+reminder_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
//        Log.d("Nok", "num rows: "+c.getCount());
        if (c.moveToFirst()) {
            do {
                t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
                if(!c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG)).equals("null")) {
                    t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));
                }
            } while (c.moveToNext());
        }
        return t;
    }

    public ArrayList<Occurrence> getAllOccurrences(int reminder_id, Reminder r) {
        ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();
        String selectQuery = "SELECT  * FROM " + TABLE_OCCURRENCES +" WHERE "+KEY_REMINDER_ID +"="+reminder_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            DAYSOFTHEWEEK d;
            do {
                if(c.getString(c.getColumnIndex(KEY_DAY)).equals("MONDAY")){
                    d = MONDAY;
                } else if(c.getString(c.getColumnIndex(KEY_DAY)).equals("TUESDAY")){
                    d = TUESDAY;
                }else if(c.getString(c.getColumnIndex(KEY_DAY)).equals("WEDNESDAY")){
                    d = WEDNESDAY;
                }else if(c.getString(c.getColumnIndex(KEY_DAY)).equals("THURSDAY")){
                    d = THURSDAY;
                }else if(c.getString(c.getColumnIndex(KEY_DAY)).equals("FRIDAY")){
                    d = FRIDAY;
                }else if(c.getString(c.getColumnIndex(KEY_DAY)).equals("SATURDAY")){
                    d = SATURDAY;
                }else{
                    d = SUNDAY;
                }

                Occurrence o = new Occurrence(d, LocalTime.parse(c.getString(c.getColumnIndex(KEY_TIME))), r, c.getInt(c.getColumnIndex(KEY_NOTIFICATION_FREQUENCY)));
                List<Integer> alarmIDs = getAllAlarmIDs(c.getInt(c.getColumnIndex(KEY_ID)));
                o.setAlarmIds(alarmIDs);
                occurrences.add(o);
            } while (c.moveToNext());
        }
        return occurrences;
    }

    public List<Integer> getAllAlarmIDs(int occurrence_id) {
        ArrayList<Integer> alarmIDs = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMIDS + " WHERE "
                + KEY_OCCURRENCE_ID + " = " + occurrence_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                alarmIDs.add(c.getInt(c.getColumnIndex(KEY_VALUE_ALAREMID)));
            } while (c.moveToNext());
        }

        return alarmIDs;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
