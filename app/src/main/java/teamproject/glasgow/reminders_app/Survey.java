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
import android.widget.ListView;
import android.widget.RadioGroup;

import org.joda.time.LocalTime;

import java.util.ArrayList;

import Controllers.SurveyListAdapter;
import Controllers.TaskListAdapter;
import Helpers.PersistencyManager;
import Model.Reminder;
import Model.SurveyQuestion;
import Model.Task;

public class Survey extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.survey_list);

        ArrayList<SurveyQuestion> questions = PersistencyManager.getSurveyQuestions();
        SurveyListAdapter adapter = new SurveyListAdapter(this,questions);
        listView.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        if (id==R.id.action_finish) {
            if(isSurveyIsCompleted()) {
                PersistencyManager.logSurveyCompletetion();
                finish();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Survey not completed")
                        .setMessage("Please make sure you answer every question before submitting")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        }

        return true;

    }

    private boolean isSurveyIsCompleted() {
        boolean isCompleted = true;
        for(int i = 0; i < listView.getChildCount(); i++) {
            RadioGroup radioGroup = (RadioGroup) listView.getChildAt(i).findViewById(R.id.radio_buttons_survey);
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                isCompleted = false;
            }
        }
        return isCompleted;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_survey, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
