package teamproject.glasgow.reminders_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Helpers.DAYSOFTHEWEEK;
import Helpers.HelperFunctions;
import Helpers.PersistencyManager;
import Model.Occurrence;
import Model.Reminder;
import Model.Task;
import at.markushi.ui.CircleButton;

public class ModifyReminder extends AppCompatActivity {

    private boolean isAddMode;
    private int reminderIndex;
    private TableLayout tableLayout;
    private Context context;
    private Reminder reminder;
    private Task task;
    private int notificationFrequency = 0;

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

        tableLayout = (TableLayout)findViewById(R.id.modify_reminders_table);
        context = this;


        switch(displayType) {
            case "add_reminder":
                isAddMode = true;
                addEmptyRow();
                break;
            case "modify_reminder":
                isAddMode = false;
                reminder = (Reminder) res.getSerializable("reminder");
                task = reminder.getTask();
                reminderIndex = res.getInt("index");
//                Log.d("Nok", "check: ModifyReminder.java: "+reminder.getName()+" "+reminder.getOccurrences().size());
                notificationFrequency = reminder.getOccurrences().get(0).getNotificationFrequency();
                addPrefilledRows(reminder);
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

            String message;
            if(reminder.getTask() != null) {
                message = (reminder.getName().equals("") ?  "This Reminder" : reminder.getName()) + " has a Task associated, are you sure you want to delete it?" ;
            }
            else {
                message = "Are you sure you want to delete " + (reminder.getName().equals("") ? "this" : reminder.getName()) + " reminder?";

            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Reminder")
                    .setMessage(message)
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

                //TODO: Test this.
                if(reminder.getTask() !=null) {
                    reminder = ExperimentSetup.setNotificationFrequency(reminder);
                }



                if (MyApp.getUserID() > 18) {
                    HelperFunctions.setAllOccurrencesToInactive(reminder);
                }
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

        int x = tableLayout.getChildCount();

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            TimePicker timePicker = (TimePicker) row.findViewById(R.id.time_picker);

            LocalTime time = new LocalTime(timePicker.getCurrentHour(),timePicker.getCurrentMinute());

            Occurrence o;
            CheckBox checkBox = (CheckBox) row.findViewById(R.id.check_mo);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.MONDAY, time, reminder,notificationFrequency);
                reminder.addOccurrence(o);
            }

            checkBox = (CheckBox) row.findViewById(R.id.check_tu);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.TUESDAY, time, reminder,notificationFrequency);
                reminder.addOccurrence(o);
            }

            checkBox = (CheckBox) row.findViewById(R.id.check_we);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.WEDNESDAY, time, reminder,0);
                reminder.addOccurrence(o);
            }

            checkBox = (CheckBox) row.findViewById(R.id.check_th);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.THURSDAY, time, reminder,notificationFrequency);
                reminder.addOccurrence(o);
            }

            checkBox = (CheckBox) row.findViewById(R.id.check_fr);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.FRIDAY, time, reminder,0);
                reminder.addOccurrence(o);
            }

            checkBox = (CheckBox) row.findViewById(R.id.check_sa);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.SATURDAY, time, reminder,0);
                reminder.addOccurrence(o);
            }

            checkBox = (CheckBox) row.findViewById(R.id.check_su);
            if(checkBox.isChecked()) {
                o = new Occurrence(DAYSOFTHEWEEK.SUNDAY, time, reminder,0);
                reminder.addOccurrence(o);
            }

        }

        return reminder;
    }

    public void setRowDataFromOccurances(List<Occurrence> occurrences, TableRow row) {


        TimePicker timePicker = (TimePicker)row.findViewById(R.id.time_picker);

        for(Occurrence o : occurrences) {
            timePicker.setCurrentHour(o.getTime().getHourOfDay());
            timePicker.setCurrentMinute(o.getTime().getMinuteOfHour());

            if(o.getDay() == DAYSOFTHEWEEK.MONDAY) {
                ((CheckBox)row.findViewById(R.id.check_mo)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.TUESDAY) {
                ((CheckBox)row.findViewById(R.id.check_tu)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.WEDNESDAY) {
                ((CheckBox)row.findViewById(R.id.check_we)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.THURSDAY) {
                ((CheckBox)row.findViewById(R.id.check_th)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.FRIDAY) {
                ((CheckBox)row.findViewById(R.id.check_fr)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.SATURDAY) {
                ((CheckBox)row.findViewById(R.id.check_sa)).setChecked(true);
            }

            if(o.getDay() == DAYSOFTHEWEEK.SUNDAY) {
                ((CheckBox)row.findViewById(R.id.check_su)).setChecked(true);
            }

        }

    }

    private TableRow addEmptyRow() {
        TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.modify_reminders_row, null);
        CircleButton circleButton = (at.markushi.ui.CircleButton) row.findViewById(R.id.add_modify_reminder_row);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmptyRow();
            }
        });
        circleButton = (at.markushi.ui.CircleButton) row.findViewById(R.id.remove_modify_reminder_row);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow row = (TableRow) view.getParent().getParent().getParent();
                if (tableLayout.getChildCount() > 1) {
                    tableLayout.removeView(row);
                }
            }
        });
        tableLayout.addView(row);
        return row;
    }

    private void addPrefilledRows(Reminder reminder) {
        EditText text = (EditText)findViewById(R.id.reminder_text);
        text.setText(reminder.getName());

        Comparator<Occurrence> timeSorter = new Comparator<Occurrence>() {

            public int compare(Occurrence o1, Occurrence o2) {

                int i = o1.getTime().compareTo(o2.getTime());
                if (i != 0) return i;

                return 0;
            }

        };

        List<Occurrence> occurrencesSortedByTime = reminder.getOccurrences();
        Collections.sort(occurrencesSortedByTime,timeSorter);

        Occurrence current = occurrencesSortedByTime.get(0);
        List<Occurrence> occurrencesAtSameTime = new ArrayList<Occurrence>();
        for(int i = 0; i < occurrencesSortedByTime.size(); i++) {
            if (current.getTime().equals(occurrencesSortedByTime.get(i).getTime())) {
                occurrencesAtSameTime.add(occurrencesSortedByTime.get(i));
            } else {
                setRowDataFromOccurances(occurrencesAtSameTime, addEmptyRow());
                occurrencesAtSameTime.clear();
                current = occurrencesSortedByTime.get(i);
                occurrencesAtSameTime.add(occurrencesSortedByTime.get(i));
            }
        }
        setRowDataFromOccurances(occurrencesAtSameTime, addEmptyRow());
    }
}
