package teamproject.glasgow.reminders_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import org.joda.time.LocalTime;

import java.util.ArrayList;

import Helpers.DAYSOFTHEWEEK;
import Model.*;
import RemindersView.ExpandListAdapter;

public class Reminders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Reminder reminder = new Reminder("Teeth");
        Occurrence occurrence1 = new Occurrence(DAYSOFTHEWEEK.FRIDAY,new LocalTime(12,2),reminder);
        Occurrence occurrence2 = new Occurrence(DAYSOFTHEWEEK.WEDNESDAY,new LocalTime(12,50),reminder);
        Occurrence occurrence3 = new Occurrence(DAYSOFTHEWEEK.MONDAY,new LocalTime(11,2),reminder);
        Occurrence occurrence4 = new Occurrence(DAYSOFTHEWEEK.MONDAY,new LocalTime(10,2),reminder);

        reminder.addOccurrence(occurrence1);
        reminder.addOccurrence(occurrence2);
        reminder.addOccurrence(occurrence3);
        reminder.addOccurrence(occurrence4);

        Model.Reminders reminders = new Model.Reminders();
        reminders.addReminder(reminder);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.ExpList);
        ExpandListAdapter ExpAdapter = new ExpandListAdapter(Reminders.this, reminders);
        expandableListView.setAdapter(ExpAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
