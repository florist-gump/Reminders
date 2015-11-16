package Model; /**
 * Created by Flo on 13/10/15.
 */
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Helpers.AlarmSetter;
import Helpers.DAYSOFTHEWEEK;
import Helpers.RandomNumberGen;

public class Occurrence implements Comparable<Occurrence>, Serializable {
    private DAYSOFTHEWEEK day;
    private LocalTime time;
    private Reminder reminder;
    private Boolean isActive;
    private List<Integer> alarmIds;
    private int notificationFrequency;
    private int id;

    public Occurrence(DAYSOFTHEWEEK day, LocalTime time, Reminder reminder, int notificationFrequency ) {
        this.day = day;
        this.time = time;
        this.reminder = reminder;
        isActive = true;
        alarmIds = new ArrayList<Integer>();
        this.notificationFrequency = notificationFrequency;
        id = RandomNumberGen.getInstance().randomInt();
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
        //updateAlarms();
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

    public List<Integer> getAlarmIds() {
        return alarmIds;
    }

    public void setAlarmIds(List<Integer> alarmIds) {
        this.alarmIds = alarmIds;
    }

    public int getNotificationFrequency() {
        return notificationFrequency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNotificationFrequency(int notificationFrequency) {
        this.notificationFrequency = notificationFrequency;
        //updateAlarms();
    }

    @Override
    public int compareTo(Occurrence another) {
        int i = day.compareTo(another.day);
        if (i != 0) return i;

        i = time.compareTo(another.time);
        if (i != 0) return i;

        return 0;
    }

    public void updateAlarms() {
        AlarmSetter.cancleAllAlarmsForOccurrence(this,null);
        AlarmSetter.setAllAlarmsForOccurrence(this,null);
    }
}
