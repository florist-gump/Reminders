package RemindersView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import Helpers.CustomPredicates;
import Helpers.DAYSOFTHEWEEK;
import Model.Occurrence;
import Model.Reminders;
import teamproject.glasgow.reminders_app.R;


/**
 * Created by Flo on 13/10/15.
 */
public class ExpandListAdapter extends BaseExpandableListAdapter implements Observer {

    private Context context;
    private Reminders reminders;
    // Objects for displaying reminders
    private ArrayList<Occurrence> sortedListOfAllOccurances;


    public ExpandListAdapter(Context context, Reminders reminders) {
        this.context = context;
        this.reminders = reminders;
        if (reminders != null)
            sortedListOfAllOccurances = reminders.getSortedListOfAllOccurrences();
        reminders.addObserver(this);
    }

    public Reminders getReminders() {
        return reminders;
    }

    public void setReminders(Reminders reminders) {
        this.reminders = reminders;
    }

    @Override
    public int getGroupCount() {
        return DAYSOFTHEWEEK.values().length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Occurrence> filteredOccurrences = new ArrayList<Occurrence>(Collections2.filter(sortedListOfAllOccurances, CustomPredicates.filterPredicatesWithDayOfTheWeek(DAYSOFTHEWEEK.values()[groupPosition])));
        return filteredOccurrences.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return DAYSOFTHEWEEK.values()[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Occurrence> filteredOccurrences = new ArrayList<Occurrence>(Collections2.filter(sortedListOfAllOccurances, CustomPredicates.filterPredicatesWithDayOfTheWeek(DAYSOFTHEWEEK.values()[groupPosition])));
        return filteredOccurrences.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DAYSOFTHEWEEK day = (DAYSOFTHEWEEK) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.weekday_list_view, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.Day);
        tv.setText(day.name());
        //ExpandableListView expandableListView = (ExpandableListView) parent;
        //expandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Occurrence occurrence = (Occurrence) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.occurances_list_view, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.Text);
        tv.setText(occurrence.getReminder().getName());
        TextView time = (TextView) convertView.findViewById(R.id.Time);
        time.setText(((Occurrence) getChild(groupPosition, childPosition)).getTime().toString("HH:mm"));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        sortedListOfAllOccurances = reminders.getSortedListOfAllOccurrences();
        notifyDataSetChanged();
    }
}
