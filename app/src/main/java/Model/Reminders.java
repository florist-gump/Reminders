package Model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Observable;


/**
 * Created by Flo on 13/10/15.
 */
public class Reminders extends Observable {
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
        setChanged();
        notifyObservers();
    }

    public void modifyReminder(Reminder reminder) {
        this.reminders.set(reminders.indexOf(reminder),reminder);
        setChanged();
        notifyObservers();
    }


    public void removeReminder(Reminder reminder) {
        this.reminders.remove(reminder);
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

}
