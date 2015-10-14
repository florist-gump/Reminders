package Model; /**
 * Created by Flo on 13/10/15.
 */
import org.joda.time.LocalTime;

import Helpers.DAYSOFTHEWEEK;

public class Occurrence implements Comparable<Occurrence> {
    private DAYSOFTHEWEEK day;
    private LocalTime time;
    private Reminder reminder;

    public Occurrence(DAYSOFTHEWEEK day, LocalTime time, Reminder reminder) {
        this.day = day;
        this.time = time;
        this.reminder = reminder;
    }

    public DAYSOFTHEWEEK getDay() {
        return day;
    }

    public void setDay(DAYSOFTHEWEEK day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public int compareTo(Occurrence another) {
        int i = day.compareTo(another.day);
        if (i != 0) return i;

        i = time.compareTo(another.time);
        if (i != 0) return i;

        return 0;
    }
}
