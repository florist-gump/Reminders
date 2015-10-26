package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Flo on 13/10/15.
 */
public class Reminder implements Serializable {
    private String name;
    private ArrayList<Occurrence> occurrences;
    private Task task;

    public Reminder(String name) {
        this.name = name;
        occurrences = new ArrayList<Occurrence>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Occurrence> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(ArrayList<Occurrence> occurrences) {
        this.occurrences = occurrences;
    }

    @Override
    public String toString(){
        return "Reminder" + name;
    }

    public void addOccurrence(Occurrence occurrence) {
        occurrences.add(occurrence);
    }

    public void sortOccurrences() {
        Collections.sort(occurrences);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
