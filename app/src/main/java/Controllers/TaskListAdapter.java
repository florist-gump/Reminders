package Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.Task;
import teamproject.glasgow.reminders_app.R;

/**
 * Created by Flo on 17/10/15.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tasks_list_view, null);
        }

        Task t = getItem(position);

        if (t != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.task_text);


            if (tt1 != null) {
                tt1.setText(t.getName());
            }

        }
        return v;
    }
}
