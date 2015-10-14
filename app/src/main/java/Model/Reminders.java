package Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Flo on 13/10/15.
 */
public class Reminders {
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
