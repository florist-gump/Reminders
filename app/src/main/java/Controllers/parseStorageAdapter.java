package Controllers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.*;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import Helpers.DAYSOFTHEWEEK;
import Model.Occurrence;
import Model.Reminder;
import Model.Reminders;
import Model.Task;
import teamproject.glasgow.reminders_app.MyApp;
import teamproject.glasgow.reminders_app.Tasks;

/**
 * Created by ttnok on 20/10/2558.
 */
public class ParseStorageAdapter {
//    private ParseObject reminderDetail = new ParseObject("ReminderList");

    public ParseStorageAdapter(){
        // Enable Local Datastore.
        Parse.enableLocalDatastore(MyApp.getAppContext());
        Parse.initialize(MyApp.getAppContext(), "cUMU855gEthkMhDtCaRzcPNoY88qs0Q4Cegve4LM", "zgxQ24kOnuFONN6zTTe0b3T7fuKXtkjxIU784lKh");

    }
    public void testAddNewTaskToDB(){
        //test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("Blabla", "bagannnnnnnnn");
        testObject.saveEventually();
//        testObject.saveInBackground();
    }

    public void createLogAlert (int user_id, Task task){
        ParseObject logAlert = new ParseObject("LogAlert");
        logAlert.put("USER_ID", user_id);
        logAlert.put("TASK_NAME", task.getName());
        logAlert.put("TIMESTAMP", LocalDateTime.now().toString());
//        logAlert.saveInBackground();
        logAlert.saveEventually(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    Log.d("Nok", "finish log alert");
                } else {
                    // The save failed.
                    Log.d("Nok", "createLogAlert: " + e.getMessage());
                }
            }
        });;
    }

    public void createLogTaskComplete (int user_id, Task task){
        ParseObject logTaskComplete = new ParseObject("LogCompleteTask");
        logTaskComplete.put("USER_ID", user_id);
        logTaskComplete.put("TASK_NAME", task.getName());
        logTaskComplete.put("TIMESTAMP", LocalDateTime.now().toString());
//        logTaskComplete.saveInBackground();
        logTaskComplete.saveEventually(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    Log.d("Nok", "finish log task complete");
                } else {
                    // The save failed.
                    Log.d("Nok", "createLogTaskComplete: "+ e.getMessage());
                }
            }
        });;
    }

}
