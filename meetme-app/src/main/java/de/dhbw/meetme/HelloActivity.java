package de.dhbw.meetme;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hello.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.*;
//import com.google.android.maps.MapActivity;

import java.io.IOException;

public class HelloActivity extends Activity {

    Button btnShowLocation;
    Button btnShowUsers;
    TextView tvShowUserList;
    private GoogleMap meetMeMap;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
        scheduleAlarm();
        Log.i("DEBUG", "scheduleAlarm is started");
        meetMeMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        meetMeMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);



       /* btnShowUsers = (Button) findViewById(R.id.showusers);
        btnShowLocation = (Button) findViewById(R.id.showlocation);
        tvShowUserList = (TextView)findViewById(R.id.showuserlist);

        btnShowUsers.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                HttpConnection getList = new HttpConnection(HelloActivity.this);

              //  Toast.makeText(getApplicationContext(), "List for users is updated", Toast.LENGTH_LONG).show();
                tvShowUserList.setText(getList.callClient());
            }
        });
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(HelloActivity.this);
                SendGPS sendeGpsDaten = new SendGPS();

                tvShowUserList.setText(sendeGpsDaten.postData("9.1","51.00"));

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String slatitude = Double.toString(latitude);
                    String slongitude = Double.toString(longitude);
                    //SendGPS sendeGpsDaten = new SendGPS(HelloActivity.this);

                    //tvShowUserList.setText(sendeGpsDaten.postData("9.1","51.00"));

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\n GPS sent to server!", Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }


            }
        });

*/

        /*Code Maren
            created: 12.10.2015
        */



    }


    //started den Alarm scheduler - funktioniert
public static final long INTERVAL_MINUTES = 30000;
    public void scheduleAlarm() {
        Log.i("DEBUG", "we are in the method schdule alarm");
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReciever.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReciever.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
               INTERVAL_MINUTES, pIntent);
    }

}

/*
public class HelloActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText("Hello world!");
    }

}*/
