package teamproject.glasgow.reminders_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import Helpers.DAYSOFTHEWEEK;
import Model.Occurrence;
import Model.Reminder;

public class ModifyReminder extends AppCompatActivity {

    private boolean isAddMode;
    private int reminderIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent  = getIntent();
        Bundle res = intent.getExtras();
        String displayType = res.getString("display_type");
        switch(displayType) {
            case "add_reminder":
                isAddMode = true;
                break;
            case "modify_reminder":
                isAddMode = false;
                Reminder reminder = (Reminder) res.getSerializable("reminder");
                reminderIndex = res.getInt("index");
                setViewDataFromReminder(reminder);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_modify_reminders, menu);
        if(isAddMode) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(false);
            this.setTitle("Add Reminder");
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            new AlertDialog.Builder(this)
                    .setTitle("Discard Changes")
                    .setMessage("Are you sure you want to go back and discard all changes" )
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // keep page open
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (id==R.id.action_delete) {

            new AlertDialog.Builder(this)
                    .setTitle("Delete reminder")
                    .setMessage("Are you sure you want to delete " + (getReminder().getName().equals("") ? getReminder().getName() : "this")   + " reminder?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("delete", true);
                            resultIntent.putExtra("index", reminderIndex);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // keep page open
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        if (id==R.id.action_finish) {
            Intent resultIntent = new Intent();

            Reminder reminder = getReminder();

            if(!isDataValid(reminder)) {
                new AlertDialog.Builder(this)
                        .setTitle("Entered Reminder not valid")
                        .setMessage("Please make sure you provide a name and dates for the reminder")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                resultIntent.putExtra("reminder",reminder);
                resultIntent.putExtra("index",reminderIndex);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        }
        return true;

    }

    private boolean isDataValid(Reminder reminder) {
        return !reminder.getName().equals("") && reminder.getOccurrences().size()>0;
    }

    public Reminder getReminder() {


        EditText text = (EditText)findViewById(R.id.reminder_text);
        String reminderName = text.getText().toString();

        Reminder reminder = new Reminder(reminderName);

        TimePicker timePicker = (TimePicker)findViewById(R.id.time_picker);
        LocalTime time = new LocalTime(timePicker.getCurrentHour(),timePicker.getCurrentMinute());

        CheckBox checkBox = (CheckBox)findViewById(R.id.check_mo);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.MONDAY, time, reminder));
        }

        checkBox = (CheckBox)findViewById(R.id.check_tu);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.TUESDAY, time, reminder));
        }

        checkBox = (CheckBox)findViewById(R.id.check_we);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.WEDNESDAY, time, reminder));
        }

        checkBox = (CheckBox)findViewById(R.id.check_th);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.THURSDAY, time, reminder));
        }

        checkBox = (CheckBox)findViewById(R.id.check_fr);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.FRIDAY, time, reminder));
        }

        checkBox = (CheckBox)findViewById(R.id.check_sa);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SATURDAY, time, reminder));
        }

        checkBox = (CheckBox)findViewById(R.id.check_su);
        if(checkBox.isChecked()) {
            reminder.addOccurrence(new Occurrence(DAYSOFTHEWEEK.SUNDAY, time, reminder));
        }

        return reminder;
    }

    public void setViewDataFromReminder(Reminder reminder) {


        EditText text = (EditText)findViewById(R.id.reminder_text);
        text.setText(reminder.getName());

        TimePicker timePicker = (TimePicker)findViewById(R.id.time_picker);

        for(Occurrence o : reminder.getOccurrences()) {
            timePicker.setCurrentHour(o.getTime().getHourOfDay());
            timePicker.setCurrentMinute(o.getTime().getMinuteOfHour());

            if(o.getDay() == DAYSOFTHEWEEK.MONDAY) {
                ((CheckBox)findViewById(R.id.check_mo)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.TUESDAY) {
                ((CheckBox)findViewById(R.id.check_tu)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.WEDNESDAY) {
                ((CheckBox)findViewById(R.id.check_we)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.THURSDAY) {
                ((CheckBox)findViewById(R.id.check_th)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.FRIDAY) {
                ((CheckBox)findViewById(R.id.check_fr)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.SATURDAY) {
                ((CheckBox)findViewById(R.id.check_sa)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.SUNDAY) {
                ((CheckBox)findViewById(R.id.check_su)).setChecked(true);
            }

        }

    }
}
