package Controllers;

import android.content.Context;
import android.util.Log;

import com.parse.*;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import Helpers.DAYSOFTHEWEEK;
import Model.Occurrence;
import Model.Reminder;
import Model.Reminders;
import Model.Task;
import teamproject.glasgow.reminders_app.Tasks;

/**
 * Created by ttnok on 20/10/2558.
 */
public class ParseStorageAdapter {
//    private ParseObject reminderDetail = new ParseObject("ReminderList");

    public ParseStorageAdapter(Context context){
        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "ZhmW1AYvR4bRxEl0iqJwxsq4T7mzzByAaEKC6q1c", "w6luIBu0V5B7zT5ShqOsXMmeOoSEMX2ZYexD7WPj");

    }
    public void testAddNewTaskToDB(){
        //test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("Blabla", "baga");
        testObject.saveInBackground();
    }

    public Reminders getRemindersFromDB(){
        final Reminders remindersDB= new Reminders();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReminderList");
//        query.fromLocalDatastore();
//        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reminderList, ParseException e) {
                if (e == null) {
                    // Results were successfully found, looking first on the
                    // network and then on disk.
                    Reminder tmpReminder;
                    for (ParseObject reminder : reminderList) {
                        tmpReminder = new Reminder(reminder.getString("Title"));
//                        Log.d("Nok", tmpReminder.getName());
                        int hour = Integer.parseInt(reminder.getString("Time").substring(0, 2));
                        int min = Integer.parseInt(reminder.getString("Time").substring(3, 5));
                        for (DAYSOFTHEWEEK d : DAYSOFTHEWEEK.values()) {
                            String colName = d.name().substring(0, 1) + d.name().toLowerCase().substring(1, 3);
//                            Log.d("Nok",colName+" "+reminder.getInt(colName));
                            if (reminder.getInt(colName) >= 1) {
                                tmpReminder.addOccurrence(new Occurrence(d, new LocalTime(hour, min), tmpReminder));
//                                Log.d("Nok", Integer.toString(tmpReminder.getOccurrences().size()));
                            }
                        }
                        remindersDB.addReminder(tmpReminder);
//                        Log.d("Nok", Integer.toString(remindersDB.getReminders().size()));
                    }
                    Log.d("Nok", "already got all objects");
                } else {
                    // The network was inaccessible and we have no cached data
                    // for this query.
                    Log.d("Nok", "getReminder: " + e.getMessage());
                }
            }
        });
        return remindersDB;

    }
    public void addReminderToDB(Reminder reminder){
        ParseObject reminderDetail = new ParseObject("ReminderList");
        String title = reminder.getName();
//        reminderDetail.put("ID", "..");
//        reminderDetail.put("userID", "..");
        reminderDetail.put("Title", title);
        reminderDetail.put("Mon", 0);
        reminderDetail.put("Tue", 0);
        reminderDetail.put("Wed", 0);
        reminderDetail.put("Thu", 0);
        reminderDetail.put("Fri", 0);
        reminderDetail.put("Sat", 0);
        reminderDetail.put("Sun", 0);
        for (Occurrence o : reminder.getOccurrences()) {
            reminderDetail.put("Time", o.getTime().toString());
            switch(o.getDay()) {
                case MONDAY:
                    reminderDetail.put("Mon", 1);
                    break;
                case TUESDAY:
                    reminderDetail.put("Tue", 1);
                    break;
                case WEDNESDAY:
                    reminderDetail.put("Wed", 1);
                    break;
                case THURSDAY:
                    reminderDetail.put("Thu", 1);
                    break;
                case FRIDAY:
                    reminderDetail.put("Fri", 1);
                    break;
                case SATURDAY:
                    reminderDetail.put("Sat", 1);
                    break;
                case SUNDAY:
                    reminderDetail.put("Sun", 1);
                    break;
                default:
                    break;
            }
        }
//        Log.d("Nok", title);
//        reminderDetail.saveInBackground();
        reminderDetail.saveEventually(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    Log.d("Nok", "finish add new object");
                } else {
                    // The save failed.
                    Log.d("Nok", "addReminder: "+ e.getMessage());
                }
            }
        });;
    }
    public void updateReminderOnDB(String title, String time, final Reminder newReminderDetail){

//        final ParseObject reminderDetail = new ParseObject("ReminderList");
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("ReminderList");
//        Log.d("Nok", title + " " + time);
        query.whereEqualTo("Title", title);
        query.whereEqualTo("Time", time);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reminder, ParseException e) {
                if (e == null) {
                    if (reminder.size() == 1) {
//                        Log.d("Nok", reminder.get(0).getObjectId());
                        ParseObject point = ParseObject.createWithoutData("ReminderList", reminder.get(0).getObjectId());
                        point.put("Title", newReminderDetail.getName());
                        point.put("Mon", 0);
                        point.put("Tue", 0);
                        point.put("Wed", 0);
                        point.put("Thu", 0);
                        point.put("Fri", 0);
                        point.put("Sat", 0);
                        point.put("Sun", 0);
                        for (Occurrence o : newReminderDetail.getOccurrences()) {
                            point.put("Time", o.getTime().toString());
                            switch (o.getDay()) {
                                case MONDAY:
                                    point.put("Mon", 1);
                                    break;
                                case TUESDAY:
                                    point.put("Tue", 1);
                                    break;
                                case WEDNESDAY:
                                    point.put("Wed", 1);
                                    break;
                                case THURSDAY:
                                    point.put("Thu", 1);
                                    break;
                                case FRIDAY:
                                    point.put("Fri", 1);
                                    break;
                                case SATURDAY:
                                    point.put("Sat", 1);
                                    break;
                                case SUNDAY:
                                    point.put("Sun", 1);
                                    break;
                                default:
                                    break;
                            }
                        }
                        point.saveEventually(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Saved successfully.
                                    Log.d("Nok", "finish update");
                                } else {
                                    // The save failed.
                                }
                            }
                        });
                    } else {
                        Log.d("Nok", "Something wrong >> size in update reminder is not equal 1!! >> " + reminder.size());
                    }
                } else {
                    // The network was inaccessible and we have no cached data
                    // for this query.
                    Log.d("Nok", "updateReminder: "+e.getMessage());
                }
            }
        });




    }
    public void deleteReminderFromDB(String title, String time){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("ReminderList");
        Log.d("Nok", title + " " + time);
        query.whereEqualTo("Title", title);
        query.whereEqualTo("Time", time);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reminder, ParseException e) {
                if (e == null) {
                    if (reminder.size() == 1) {
                        ParseObject point = ParseObject.createWithoutData("ReminderList", reminder.get(0).getObjectId());
                        point.deleteEventually(new DeleteCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Saved successfully.
                                    Log.d("Nok", "finish delete");
                                } else {
                                    // The save failed.
                                }
                            }
                        });
                    } else {
                        Log.d("Nok", "Something wrong >> size in delete reminder object is not equal 1!! >> " + reminder.size());
                    }

                } else {
                    Log.d("Nok", "deleteReminder: "+ e.getMessage());
                }
            }
        });


    }
    public ArrayList<Task> getTaskFromDB(){
        final ArrayList<Task> tasks = new ArrayList<Task>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReminderList");
        //        query.fromLocalDatastore();
        //        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<ParseObject>() {
            //query distinct value still not support from parse
            public void done(List<ParseObject> reminderList, ParseException e) {
                if (e == null) {
                    for (ParseObject reminder : reminderList) {
                        Task task = new Task(reminder.getString("Title"));
                        tasks.add(task);
//                        Log.d("Nok", "getTask size: "+tasks.size());
                    }

                } else {
                    Log.d("Nok", "getTask: "+ e.getMessage());
                }
            }
        });
        return tasks;
    }
    public Reminders searchReminder(String text){
        final Reminders searchReminder= new Reminders();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReminderList");
        //        query.fromLocalDatastore();
        //        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.whereContains("Title", text);
        query.findInBackground(new FindCallback<ParseObject>() {
            //query distinct value still not support from parse
            //no case insensitive queries >> parse not support
            public void done(List<ParseObject> searchList, ParseException e) {
                if (e == null) {
                    Reminder tmpSearch;
                    for (ParseObject search : searchList) {
                        tmpSearch = new Reminder(search.getString("Title"));
//                        Log.d("Nok", tmpReminder.getName());
                        int hour = Integer.parseInt(search.getString("Time").substring(0, 2));
                        int min = Integer.parseInt(search.getString("Time").substring(3, 5));
                        for (DAYSOFTHEWEEK d : DAYSOFTHEWEEK.values()) {
                            String colName = d.name().substring(0, 1) + d.name().toLowerCase().substring(1, 3);
//                            Log.d("Nok",colName+" "+reminder.getInt(colName));
                            if (search.getInt(colName) >= 1) {
                                tmpSearch.addOccurrence(new Occurrence(d, new LocalTime(hour, min), tmpSearch));
//                                Log.d("Nok", Integer.toString(tmpReminder.getOccurrences().size()));
                            }
                        }
                        searchReminder.addReminder(tmpSearch);
//                        Log.d("Nok", tmpSearch.getName());
                    }
                    Log.d("Nok", "already got all search objects");


                } else {
                    Log.d("Nok", "getTask: "+ e.getMessage());
                }
            }
        });
        return searchReminder;

    }
}
