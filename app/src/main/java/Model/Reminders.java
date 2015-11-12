package Model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import Helpers.AlarmSetter;
import Helpers.DAYSOFTHEWEEK;


/**
 * Created by Flo on 13/10/15.
 */
public class Reminders extends Observable implements Serializable {
    private ArrayList<Reminder> reminders;


    public Reminders() {
        reminders = new ArrayList<Reminder>();
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void addReminder(Reminder reminder) {
        this.reminders.add(reminder);
        AlarmSetter.setRepeatingAlarmForReminder(reminder,null);
        setChanged();
        notifyObservers();
    }

    public void modifyReminder(Reminder reminder, int index) {
        this.reminders.set(index, reminder);
        AlarmSetter.setRepeatingAlarmForReminder(reminder, null);
        setChanged();
        notifyObservers();
    }


    public void removeReminder(Reminder reminder) {
        this.reminders.remove(reminder);
        AlarmSetter.cancelRepeatingAlarmForReminder(reminder, null);
        setChanged();
        notifyObservers();
    }

    public void removeReminder(int index) {
        AlarmSetter.cancelRepeatingAlarmForReminder(this.reminders.get(index), null);
        this.reminders.remove(index);
        setChanged();
        notifyObservers();
    }

    public ArrayList<Occurrence> getSortedListOfAllOccurrences() {
        ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();
        for (Reminder r : reminders) {
            for (Occurrence o : r.getOccurrences()) {
                occurrences.add(o);
            }
        }
        Collections.sort(occurrences);
        return occurrences;
    }

    public ArrayList<DAYSOFTHEWEEK> getDaysOfWeekThatHaveOccurances() {
        Set<DAYSOFTHEWEEK> hs = new HashSet<DAYSOFTHEWEEK>();
        for (Reminder r : reminders) {
            for (Occurrence o : r.getOccurrences()) {
                hs.add(o.getDay());
            }
        }
        ArrayList<DAYSOFTHEWEEK> days = new ArrayList<DAYSOFTHEWEEK>();
        days.addAll(hs);
        Collections.sort(days);
        return days;
    }

    public Occurrence getOccurenceFromId(int id) {
        for (Reminder r : reminders) {
            for (Occurrence o : r.getOccurrences()) {
                if(id == o.getId()) {
                    return o;
                }
            }
        }
        return null;
    }

    public void cancelAlarmsForAllReminders() {
        for(Reminder reminder : reminders) {
            AlarmSetter.cancelRepeatingAlarmForReminder(reminder, null);
        }
    }

}
