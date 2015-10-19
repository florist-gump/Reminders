package teamproject.glasgow.reminders_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
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
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import Helpers.AlarmReceiver;
import Helpers.HelperFunctions;
import Model.Occurrence;
import Model.Reminder;
import Controllers.ExpandListAdapter;

public class Reminders extends AppCompatActivity {

    Model.Reminders reminders;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    ExpandListAdapter ExpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Reminders.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        reminders = HelperFunctions.generateReminderTestData();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.ExpList);

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
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
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
                    createAlarmManager(reminder);
                    reminders.addReminder(reminder);
                }
                break;
            case 2: //modify reminder
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    if (res.getBoolean("delete")) {
                        Integer index =  res.getInt("index");
                        reminders.removeReminder(index);
                        break;
                    }
                    Reminder reminder =  (Reminder)res.getSerializable("reminder");
                    Integer index =  res.getInt("index");
                    createAlarmManager(reminder);
                    reminders.modifyReminder(reminder,index);
                }
                break;
        }
    }

    private void createAlarmManager(Reminder reminder) {
        Calendar calendar = Calendar.getInstance();
        for (Occurrence o : reminder.getOccurrences()) {
            calendar.set(Calendar.HOUR_OF_DAY, o.getTime().getHourOfDay());
            calendar.set(Calendar.MINUTE, o.getTime().getMinuteOfHour());
            calendar.set(Calendar.SECOND, o.getTime().getSecondOfMinute());
            Intent intent1 = new Intent(Reminders.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(Reminders.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) Reminders.this.getSystemService(Reminders.this.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reminders, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View arg0) {
                ExpAdapter.removeFilter();
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
