package de.dhbw.meetme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
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

import java.io.IOException;

public class HelloActivity extends Activity {

    Button btnShowLocation;
    Button btnShowUsers;
    TextView tvShowUserList;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);

        btnShowUsers = (Button) findViewById(R.id.showusers);
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
                SendGPS sendeGpsDaten = new SendGPS(HelloActivity.this);

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
