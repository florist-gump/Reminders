package Helpers;

import org.joda.time.LocalTime;

import java.util.List;

import Model.Occurrence;
import Model.Reminder;
import Model.Task;

/**
 * Created by Flo on 14/10/15.
 */
public class HelperFunctions {
    public static Model.Reminders generateReminderInitData() {
        Reminder r;
        Model.Reminders reminders = new Model.Reminders();
        for(Task t : PersistencyManager.getTasks()) {
            r = new Reminder(t.getName());
            r.setTask(t);
            r.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, new LocalTime(8, 0), r,0));
            reminders.addReminder(r);
        }
        return reminders;
    }

    public static  Model.Reminders generateReminderDummmyData() {
        Reminder r;
        Model.Reminders reminders = new Model.Reminders();
        r = new Reminder("test");
        Occurrence o = new Occurrence(DAYSOFTHEWEEK.SUNDAY, new LocalTime(15, 00), r,3);
        r.addOccurrence(o);
        reminders.addReminder(r);
        return reminders;
    }
}
