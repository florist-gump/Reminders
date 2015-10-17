package Helpers;

import org.joda.time.LocalTime;

import Model.Occurrence;
import Model.Reminder;

/**
 * Created by Flo on 14/10/15.
 */
public class HelperFunctions {
    public static Model.Reminders generateReminderTestData() {
        Reminder reminder1 = new Reminder("Floss Teeth");

        reminder1.addOccurrence(new Occurrence(DAYSOFTHEWEEK.FRIDAY, new LocalTime(12, 2), reminder1));
        reminder1.addOccurrence(new Occurrence(DAYSOFTHEWEEK.WEDNESDAY, new LocalTime(12, 50), reminder1));
        reminder1.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, new LocalTime(11, 2), reminder1));
        reminder1.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, new LocalTime(10, 2), reminder1));

        Reminder reminder2 = new Reminder("Exercise");

        reminder2.addOccurrence(new Occurrence(DAYSOFTHEWEEK.TUESDAY, new LocalTime(16, 55), reminder2));
        reminder2.addOccurrence(new Occurrence(DAYSOFTHEWEEK.FRIDAY, new LocalTime(20, 50), reminder2));
        reminder2.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, new LocalTime(9, 2), reminder2));
        reminder2.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SATURDAY, new LocalTime(11, 2), reminder2));

        Reminder reminder3 = new Reminder("Complete Survey");

        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, new LocalTime(19, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.TUESDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.TUESDAY, new LocalTime(19, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.WEDNESDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.WEDNESDAY, new LocalTime(19, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.THURSDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.THURSDAY, new LocalTime(19, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.FRIDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.FRIDAY, new LocalTime(19, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SATURDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SATURDAY, new LocalTime(19, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SUNDAY, new LocalTime(8, 0), reminder3));
        reminder3.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SUNDAY, new LocalTime(19, 0), reminder3));


        Model.Reminders reminders = new Model.Reminders();
        reminders.addReminder(reminder1);
        reminders.addReminder(reminder2);
        reminders.addReminder(reminder3);
        return reminders;
    }
}
