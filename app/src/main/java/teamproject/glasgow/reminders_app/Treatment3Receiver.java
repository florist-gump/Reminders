package teamproject.glasgow.reminders_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joshuamarsh on 11/6/15.
 */
public class Treatment3Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ExperimentSetup.treatmenr3();
//        System.out.println("*********In Treatment3Receiver *********\n****************");
    }

}
