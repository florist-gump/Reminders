package Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Model.SurveyQuestion;
import Model.Task;
import teamproject.glasgow.reminders_app.R;

/**
 * Created by Flo on 17/10/15.
 */
public class SurveyListAdapter extends ArrayAdapter<SurveyQuestion> {


    public SurveyListAdapter(Context context, ArrayList<SurveyQuestion> questions) {
        super(context, 0, questions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.survey_list_view, null);
        }

        SurveyQuestion s = getItem(position);

        if (s != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.survey_text);


            if (tt1 != null) {
                tt1.setText(s.getQuestion());
            }

        }
        return v;
    }
}
