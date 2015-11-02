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

import static Helpers.DAYSOFTHEWEEK.*;

/**
 * Created by ttnok on 1/11/2558.
 */

public class LocalStorageAdapter extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = LocalStorageAdapter.class.getName();

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

    public LocalStorageAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    // ------------------------ "tasks" table methods ----------------//

    /**
     * Creating a task
     */
    public long createTask(Task task, ArrayList<Occurrence> occurrences) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_LAST_COMPLETE_LOG, String.valueOf(task.getLastCompletionLog()));
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());

        // insert row
        long task_id = db.insert(TABLE_TASKS, null, values);

        // insert tag_ids
        for (Occurrence occurrence : occurrences) {
            createOccurrence(task_id, occurrence);
        }

        return task_id;
    }

    /**
     * get single todo
     */
    public int getTaskID(String task_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_NAME + " = " + task_name;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

//        Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
//        t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));

        return c.getColumnIndex(KEY_ID);
    }

    /**
     * getting all tasks
     * */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
                t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));

                // adding to tasks
                tasks.add(t);
            } while (c.moveToNext());
        }

        return tasks;
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
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a task
     */
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_LAST_COMPLETE_LOG, String.valueOf(task.getLastCompletionLog()));
        values.put(KEY_UPDATED_AT, String.valueOf(task.getLastCompletionLog()));

        // updating row
        return db.update(TABLE_TASKS, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(task.getName()) });
    }

    /**
     * Deleting a task
     */
    public void deleteTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task_id) });
    }

    // ------------------------ "occurrences" table methods ----------------//

    /**
     * Creating occurrence
     */
    public long createOccurrence(long task_id, Occurrence occurrence) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task_id);
        values.put(KEY_DAY, String.valueOf(occurrence.getDay()));
        values.put(KEY_NOTIFICATION_FREQUENCY, occurrence.getNotificationFrequency());
        values.put(KEY_STATUS, "on"); //for alarm...
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());

        // insert row
        long occurrence_id = db.insert(TABLE_OCCURRENCES, null, values);

        return occurrence_id;
    }

    /**
     * getting all occurrenes
     *
     * @param t*/
    public List<Occurrence> getAllOccurrences(Task t) {
        List<Occurrence> occurrences = new ArrayList<Occurrence>();
        String selectQuery = "SELECT  * FROM " + TABLE_OCCURRENCES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            DAYSOFTHEWEEK d = MONDAY;
            do {
                Reminder r = new Reminder(t.getName());
                switch(Integer.parseInt(c.getString(c.getColumnIndex(KEY_DAY)))) { //------------check!!
                    case 0:
                        d = MONDAY;
                        break;
                    case 1:
                        d = TUESDAY;
                        break;
                    case 2:
                        d = WEDNESDAY;
                        break;
                    case 3:
                        d = THURSDAY;
                        break;
                    case 4:
                        d = FRIDAY;
                        break;
                    case 5:
                        d = SATURDAY;
                        break;
                    case 6:
                        d = SUNDAY;
                        break;
                    default:
                        break;
                }

                Occurrence o = new Occurrence(d, LocalTime.parse(c.getString(c.getColumnIndex(KEY_TIME))), r, c.getInt(c.getColumnIndex(KEY_NOTIFICATION_FREQUENCY)));

                // adding to tags list
                occurrences.add(o);
            } while (c.moveToNext());
        }
        return occurrences;
    }

    public List<Integer> getOccurrencesID(int task_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> occurences_id = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_TASK_ID + " = " + task_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            if(c.moveToFirst()){
                do {
                    occurences_id.add(c.getInt(c.getColumnIndex(KEY_TASK_ID)));
                } while (c.moveToNext());
            }

//        Task t = new Task(c.getString(c.getColumnIndex(KEY_NAME)));
//        t.setLastCompletionLog(LocalTime.parse(c.getString(c.getColumnIndex(KEY_LAST_COMPLETE_LOG))));

        return occurences_id;
    }

    /**
     * Updating a occurrence
     */
    public int updateOccurrences(Task t, List<Occurrence> occurrences) {
        SQLiteDatabase db = this.getWritableDatabase();

        int task_id = getTaskID(t.getName());
        deleteOccurrences(getOccurrencesID(task_id));

        for (Occurrence occurrence : occurrences) {
            createOccurrence(task_id, occurrence);
        }
//

//        ContentValues values = new ContentValues();
//        values.put(KEY_TASK_ID, task_id);
//
//        // updating row
//        return db.update(TABLE_OCCURRENCES, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(tag.getId()) });
        return 0;
    }

    /**
     * Deleting occurrences
     */
    public void deleteOccurrences(List<Integer> occurrences_id) {
        SQLiteDatabase db = this.getWritableDatabase();
            // delete all occurrences
            for (Integer occurrence_id : occurrences_id) {
                // delete occurrence
                deleteOccurrence(occurrence_id);
            }
    }

    public void deleteOccurrence(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete occurrence
        db.delete(TABLE_OCCURRENCES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
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
