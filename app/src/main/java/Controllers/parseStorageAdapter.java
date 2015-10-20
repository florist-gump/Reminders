package Controllers;

import android.content.Context;

import com.parse.*;

/**
 * Created by ttnok on 20/10/2558.
 */
public class parseStorageAdapter {

    public parseStorageAdapter(Context context){
        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "ZhmW1AYvR4bRxEl0iqJwxsq4T7mzzByAaEKC6q1c", "w6luIBu0V5B7zT5ShqOsXMmeOoSEMX2ZYexD7WPj");

    }
    public void addNewTaskToDB(){
        //test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}
