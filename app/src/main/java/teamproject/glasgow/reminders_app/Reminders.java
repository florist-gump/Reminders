package teamproject.glasgow.reminders_app;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import Controllers.ParseStorageAdapter;
import Helpers.AlarmSetter;
import Helpers.HelperFunctions;
import Helpers.PersistencyManager;
import Model.Occurrence;
import Model.Reminder;
import Controllers.ExpandListAdapter;

public class Reminders extends AppCompatActivity {

    private Model.Reminders reminders;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ParseStorageAdapter cloudMem;

    private ExpandListAdapter ExpAdapter;
    private ExpandableListView expandableListView;

    private BroadcastReceiver receiver;
    public static Reminders _reminders;

    private SharedPreferences prefs = null;
    private int UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("teamproject.glasgow.reminders_app", MODE_PRIVATE);

        setContentView(R.layout.activity_reminders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        cloudMem = new ParseStorageAdapter(this);
//        cloudMem.testAddNewTaskToDB();
//        cloudMem.getRemindersFromDB();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView) findViewById(R.id.navList);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (prefs.getBoolean("firstrun", true)) {
            reminders = HelperFunctions.generateReminderInitData();
            PersistencyManager.saveReminders(reminders);
        }
        else {
            reminders = PersistencyManager.getReminders();
        }

        //reminders = cloudMem.getRemindersFromDB();
//        cloudMem.searchReminder("a");

        expandableListView = (ExpandableListView) findViewById(R.id.ExpList);


        // Display indicator on the right
        Display newDisplay = getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expandableListView.setIndicatorBounds(width-100, width);

        ExpAdapter = new ExpandListAdapter(Reminders.this, reminders);
        expandableListView.setAdapter(ExpAdapter);

        //expand all items
        for ( int i = 0; i < ExpAdapter.getGroupCount(); i++ ) {
            expandableListView.expandGroup(i);
        }


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Occurrence occurrence = (Occurrence) ExpAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("teamproject.glasgow.reminders_app", "teamproject.glasgow.reminders_app.ModifyReminder");
                intent.putExtra("display_type", "modify_reminder");
                Reminder reminder = occurrence.getReminder();
                intent.putExtra("reminder", reminder);
                intent.putExtra("index", reminders.getReminders().indexOf(reminder));
                AlarmSetter.cancelRepeatingAlarmForReminder(reminder, null);
                startActivityForResult(intent, 2);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(view.getContext(), ModifyReminder.class);
                //startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("teamproject.glasgow.reminders_app", "teamproject.glasgow.reminders_app.ModifyReminder");
                intent.putExtra("display_type", "add_reminder");
                startActivityForResult(intent, 1);
            }
        });

        //setButtonColor
        Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_input_add, getTheme());
        Drawable white = myFabSrc.getConstantState().newDrawable();
        white.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        fab.setImageDrawable(white);

        _reminders = this;
        MyApp.setReminders(reminders);
        UserID = MyApp.getUserID();
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("Nok", "onStop: "+reminders.getReminders().size());
////        PersistencyManager.saveReminders(reminders);
//    }
    @Override
    public void onPause() {
        super.onPause();
//        Log.d("Nok", "onPause: " + reminders.getReminders().size());
        PersistencyManager.saveReminders(reminders);
    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("Nok", "onDestroy: " + reminders.getReminders().size());
////        PersistencyManager.saveReminders(reminders);
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);;
            intent.setClassName("teamproject.glasgow.reminders_app", "teamproject.glasgow.reminders_app.ExperimentSetup");
            startActivity(intent);
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    private void addDrawerItems() {
        String[] listItems = { "Reminders", "Tasks", "Survey"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation");
                supportInvalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:  // add reminder
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    Reminder reminder =  (Reminder)res.getSerializable("reminder");
                    reminders.addReminder(reminder);
//                    PersistencyManager.saveReminder(reminder);
                    //cloudMem.addReminderToDB(reminder);
                }
                break;
            case 2: //modify reminder
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    if (res.getBoolean("delete")) {
                        Integer index =  res.getInt("index");
//                        Log.d("Nok", "delete " + res.toString());
//                        PersistencyManager.deleteReminder(reminders.getReminders().get(index));
                        //cloudMem.deleteReminderFromDB(reminders.getReminders().get(index).getName(), reminders.getReminders().get(index).getOccurrences().get(0).getTime().toString());
                        reminders.removeReminder(index);
                        break;
                    }
                    Reminder reminder =  (Reminder)res.getSerializable("reminder");
                    Integer index =  res.getInt("index");
//                    Log.d("Nok","Reminders.java >> modify reminders: "+reminders.getReminders().get(index).getName()+" occ: "+ reminders.getReminders().get(index).getOccurrences().size() +"task: " + reminders.getReminders().get(index).getTask().getName());
//                    Log.d("Nok","modify reminders get index: "+reminders.getReminders().get(index));
//                    PersistencyManager.updateReminder(reminders.getReminders().get(index));
                    //cloudMem.updateReminderOnDB(reminders.getReminders().get(index).getName(), reminders.getReminders().get(index).getOccurrences().get(0).getTime().toString(), reminder);
                    reminders.modifyReminder(reminder, index);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id==R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reminders, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View arg0) {
                ExpAdapter.removeFilter();
                // fix collapsed groups
                for (int i = 0; i < ExpAdapter.getGroupCount(); i++) {
                    expandableListView.expandGroup(i);
                }
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                ExpAdapter.searchStarted();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ExpAdapter.filter(newText);
                // fix collapsed groups
                for (int i = 0; i < ExpAdapter.getGroupCount(); i++) {
                    expandableListView.expandGroup(i);
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

        /** Swaps fragments in the main content view */
        private void selectItem(int position) {
            Intent intent = new Intent(Intent.ACTION_VIEW);;
            switch(position) {
                case 1:
                    intent.setClassName("teamproject.glasgow.reminders_app", "teamproject.glasgow.reminders_app.Tasks");
                    startActivity(intent);
                    break;
                case 2:
                    intent.setClassName("teamproject.glasgow.reminders_app", "teamproject.glasgow.reminders_app.Survey");
                    startActivity(intent);
                    break;
            }
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

        }
    }
}
