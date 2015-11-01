package Model;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.io.Serializable;

import Helpers.PersistencyManager;

/**
 * Created by Flo on 16/10/15.
 */
public class Task implements Serializable {

    private String name;
    private LocalTime lastCompletionLog;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getLastCompletionLog() {
        return lastCompletionLog;
    }

    public void setLastCompletionLog(LocalTime lastCompletionLog) {
        this.lastCompletionLog = lastCompletionLog;
    }

    public void logTaskCompletetion() {
        lastCompletionLog = LocalTime.now();
        PersistencyManager.logTaskCompletetion(this);
    }

    public boolean canTaskBeLogged() {
        if (lastCompletionLog != null) {
            if(Minutes.minutesBetween(LocalTime.now(),lastCompletionLog).getMinutes() < 1) {
                return false;
            }
        }
        return  true;
    }
}
