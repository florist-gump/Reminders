package Model; /**
 * Created by Flo on 13/10/15.
 */
import org.joda.time.LocalTime;

import java.io.Serializable;

import Helpers.DAYSOFTHEWEEK;

public class Occurrence implements Comparable<Occurrence>, Serializable {
    private DAYSOFTHEWEEK day;
    private LocalTime time;
    private Reminder reminder;
    private Boolean isActive;

    public Occurrence(DAYSOFTHEWEEK day, LocalTime time, Reminder reminder) {
        this.day = day;
        this.time = time;
        this.reminder = reminder;
        isActive = true;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
