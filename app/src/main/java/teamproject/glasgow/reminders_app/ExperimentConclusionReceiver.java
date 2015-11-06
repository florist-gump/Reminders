package teamproject.glasgow.reminders_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joshuamarsh on 11/5/15.
 */
public class ExperimentConclusionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ExperimentSetup.experimentConclusion();
        System.out.println("*********In ExperimentConclusionReceiver*********\n****************");
    }
}
