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
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_OCCURRENCES = "occurrences";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_COMPLETE_LOG = "last_complete_log";

    private static final String KEY_DAY = "day";
    private static final String KEY_TIME = "time";
    private static final String KEY_NOTIFICATION_FREQUENCY = "notification_frequency";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_STATUS = "status";

    // Table Create Statements
    // Tasks table create statement
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_LAST_COMPLETE_LOG + " DATETIME," + KEY_CREATED_AT
            + " DATETIME," + KEY_UPDATED_AT + " DATETIME" + ")";

    // Occurrences table create statement
    private static final String CREATE_TABLE_OCCURRENCES = "CREATE TABLE " + TABLE_OCCURRENCES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK_ID + " INTEGER,"
            + KEY_DAY + " TEXT," + KEY_TIME + " DATETIME," + KEY_NOTIFICATION_FREQUENCY + " INTEGER," + KEY_STATUS + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT + " DATETIME" + ")";

    public LocalStorageAdapter() {
        super(MyApp.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_OCCURRENCES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OCCURRENCES);

        // create new tables
        onCreate(db);
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        int tr = db.delete(TABLE_TASKS, null, null);
        int or = db.delete(TABLE_OCCURRENCES, null, null);
        Log.d("Nok","delete all data >> num of task row: "+ tr+" num of occurrence row: "+or);
    }

    // ------------------------ "tasks" table methods ----------------//
    /**
     * Creating a task
     */
    public long createReminder(Reminder reminder) {
        if(reminder == null){
            return 0;
        }
        Task task = reminder.getTask();
        ArrayList<Occurrence> occurrences = reminder.getOccurrences();
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Nok", "createReminder: " + reminder.getName());

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, reminder.getName());
//        if(task.getLastCompletionLog() != null) {
//            values.put(KEY_LAST_COMPLETE_LOG, String.valueOf(task.getLastCompletionLog()));
//        }
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());

        // insert row
        long task_id = db.insert(TABLE_TASKS, null, values);

        // insert tag_ids
        for (Occurrence occurrence : occurrences) {
            createOccurrence((int)task_id, occurrence);
        }

        return task_id;
    }

    /**
     * get single task
     */
    public int getTaskID(String task_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_NAME + " = '" + task_name+"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        Log.d("Nok", c.getString(c.getColumnIndex(KEY_NAME)) + " " + c.getString(c.getColumnIndex(KEY_ID)));
//        Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
//        t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));

        return c.getInt(c.getColumnIndex(KEY_ID));
    }

    /**
     * getting all tasks
     * */
    public Reminders getAllReminders() {
//        Task task = new Task();
//        ArrayList <Occurrence> occurrences = new ArrayList<Occurrence>();
        Reminders reminders = new Reminders();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Reminder reminder = new Reminder(c.getString(c.getColumnIndex(KEY_NAME)));
                Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
//                t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));
                Log.d("Nok", "Task: " + t.getName());
                ArrayList <Occurrence> occurrences = getAllOccurrences(t);
                for(Occurrence o: occurrences){
                    reminder.addOccurrence(o);
                }
//                reminder.setOccurrences(occurrences);
                reminder.setTask(t);
                Log.d("Nok", "occ_size: "+occurrences.size());
                // adding to tasks
                reminders.addReminder(reminder);
            } while (c.moveToNext());
        }

        return reminders;
    }

    public Reminder getReminders(String task_name) {
//        Task task = new Task();
//        ArrayList <Occurrence> occurrences = new ArrayList<Occurrence>();
        Reminder reminder = new Reminder(task_name);
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_NAME + " = '" + task_name+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
//                t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));
            Log.d("Nok", "Task: " + t.getName());
            ArrayList <Occurrence> occurrences = getAllOccurrences(t);
            Log.d("Nok", "occ_size: "+occurrences.size());
            reminder.setOccurrences(occurrences);
            reminder.setTask(t);
        }

        return reminder;
    }

//    /**
//     * getting all todos under single tag
//     * */
//    public List<Todo> getAllToDosByTag(String tag_name) {
//        List<Todo> todos = new ArrayList<Todo>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
//                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
//                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
//                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
//                + "tt." + KEY_TODO_ID;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Todo td = new Todo();
//                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
//                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
//
//                // adding to todo list
//                todos.add(td);
//            } while (c.moveToNext());
//        }
//
//        return todos;
//    }

    /**
     * getting task count
     */
//    public int getTaskCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }

    /**
     * Updating a task
     */
    public int updateReminder(Reminder reminder) {
        Task task = reminder.getTask();
        ArrayList<Occurrence> occurrences = reminder.getOccurrences();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
//        values.put(KEY_LAST_COMPLETE_LOG, String.valueOf(task.getLastCompletionLog()));
        values.put(KEY_UPDATED_AT, String.valueOf(task.getLastCompletionLog()));

        updateOccurrences(task, occurrences);

        // updating row
        return db.update(TABLE_TASKS, values, KEY_UPDATED_AT + " = ?", //assume that task name will never change
                new String[] { String.valueOf(getDateTime()) });
    }

    /**
     * Deleting a task
     */
    public void deleteReminder(Reminder reminder) {
        Task t = reminder.getTask();
        ArrayList<Occurrence> o = reminder.getOccurrences();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_NAME + " = ?",
                new String[] { String.valueOf(t.getName()) });
        deleteOccurrence(getTaskID(t.getName()));
    }

    // ------------------------ "occurrences" table methods ----------------//

    /**
     * Creating occurrence
     */
    public long createOccurrence(int task_id, Occurrence occurrence) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task_id);
        values.put(KEY_DAY, String.valueOf(occurrence.getDay()));
        values.put(KEY_TIME, String.valueOf(occurrence.getTime()));
        values.put(KEY_NOTIFICATION_FREQUENCY, occurrence.getNotificationFrequency());
        values.put(KEY_STATUS, "on"); //for alarm...
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());

        Log.d("Nok","create occurrence >> task id: " +task_id+" day: "+occurrence.getDay()+ " time: "+ occurrence.getTime());

        // insert row
        long occurrence_id = db.insert(TABLE_OCCURRENCES, null, values);

        return occurrence_id;
    }

    /**
     * getting all occurrenes
     *
     * @param t*/
    public ArrayList<Occurrence> getAllOccurrences(Task t) {
        ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();
        String selectQuery = "SELECT  * FROM " + TABLE_OCCURRENCES + " WHERE "
                + KEY_TASK_ID + " = " + getTaskID(t.getName());

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            DAYSOFTHEWEEK d;
            do {
                Reminder r = new Reminder(t.getName());

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
                Log.d("Nok", c.getString(c.getColumnIndex(KEY_ID))+" "+c.getString(c.getColumnIndex(KEY_TASK_ID))
                        +" "+c.getString(c.getColumnIndex(KEY_DAY))+" "+c.getString(c.getColumnIndex(KEY_TIME))
                        +" "+c.getString(c.getColumnIndex(KEY_NOTIFICATION_FREQUENCY))
                        +" "+c.getString(c.getColumnIndex(KEY_STATUS))
                        +" "+c.getString(c.getColumnIndex(KEY_CREATED_AT))
                        +" "+c.getString(c.getColumnIndex(KEY_UPDATED_AT)));

                Log.d("Nok", "day: "+d);

                Occurrence o = new Occurrence(d, LocalTime.parse(c.getString(c.getColumnIndex(KEY_TIME))), r, c.getInt(c.getColumnIndex(KEY_NOTIFICATION_FREQUENCY)));

                // adding to tags list
                occurrences.add(o);
            } while (c.moveToNext());
        }
        return occurrences;
    }

//    public List<Integer> getOccurrencesID(int task_id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<Integer> occurences_id = new ArrayList<Integer>();
//        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
//                + KEY_TASK_ID + " = " + task_id;
//
//        Log.e(LOG, selectQuery);
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c != null)
//            if(c.moveToFirst()){
//                do {
//                    occurences_id.add(c.getInt(c.getColumnIndex(KEY_TASK_ID)));
//                } while (c.moveToNext());
//            }
//
////        Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
////        t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));
//
//        return occurences_id;
//    }

    public String getCreatedAt(int task_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> occurences_id = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_ID + " = " + task_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        String createAt = "";

        if (c != null)
            if(c.moveToFirst()){
                createAt = c.getString(c.getColumnIndex(KEY_CREATED_AT));
            }

//        Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
//        t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));

        return createAt;
    }

    /**
     * Updating a occurrence
     */
    public int updateOccurrences(Task t, ArrayList<Occurrence> occurrences) {
        SQLiteDatabase db = this.getWritableDatabase();

        int task_id = getTaskID(t.getName());
        deleteOccurrence(task_id);

        for (Occurrence occurrence : occurrences) {
            createOccurrence(task_id, occurrence);
        }
//

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task_id);
//
//        // updating row
        return db.update(TABLE_OCCURRENCES, values, KEY_CREATED_AT + " = ?",
                new String[] { String.valueOf(getCreatedAt(task_id)) });
//        return 0;
    }

    /**
     * Deleting occurrences
     */
//    public void deleteOccurrences(List<Integer> occurrences_id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//            // delete all occurrences
//            for (Integer occurrence_id : occurrences_id) {
//                // delete occurrence
//                deleteOccurrence(occurrence_id);
//            }
//    }

    public void deleteOccurrence(int task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete occurrence
        db.delete(TABLE_OCCURRENCES, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(task_id) });
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