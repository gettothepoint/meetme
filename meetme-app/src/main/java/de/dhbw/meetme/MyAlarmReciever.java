package de.dhbw.meetme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kollemar on 11.10.2015.
 */
public class MyAlarmReciever extends BroadcastReceiver{
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {

            Intent i = new Intent(context, MyGpsService.class);
            i.putExtra("foo", "bar");
            context.startService(i);
    }
}
