package teamproject.glasgow.reminders_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Helpers.PersistencyManager;
import Model.*;

/**
 * Created by joshuamarsh on 11/6/15.
 */
public class Treatment2Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyApp.initOnBroadCastReceiver(context);

        ExperimentSetup.treatment2();
        Model.Reminders reminders = MyApp.getReminders();
        PersistencyManager.saveReminders(reminders, false);
        System.out.println("*********In Treatment2Receiver *********\n****************");
    }
}
