package teamproject.glasgow.reminders_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ExperimentSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_setup);
    }

    public void start(View view) {
        SharedPreferences prefs = getSharedPreferences("teamproject.glasgow.reminders_app", MODE_PRIVATE);

        EditText text = (EditText)findViewById(R.id.user_id);
        int userID = Integer.parseInt(text.getText().toString());
        prefs.edit().putInt("user_id", userID).commit();

        // setup the design here
        // you can get the reminders via MyApp.getReminders() and userID via MyApp.getUserID()
        // if you want to wake up the system at certain times to change occ look into the AlarmSetter and AlarmReceiver classes
        // a reboot cancels your alarms so you'd also have to do something similar to what I did in BootReceiver to restore your previously set Alarms
        finish();
    }
}
