package de.dhbw.meetme;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kollemar on 11.10.2015.
 */
public class MyGpsService extends IntentService {
public MyGpsService(){          //this might be incorrect
    super("MyGpsService");
    Log.i("DEBUG", "leerer Konstruktor");
}
    public MyGpsService(String name) {
        super("MyGpsService");
        Log.i("DEBUG", "voller konstruktor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    Log.i("MyGpsService", "The service is running now");
        GPSTracker gps;
        Log.i("DEBUG", getApplicationContext().toString());
        gps = new GPSTracker(getApplicationContext());
        // check if GPS enabled
        if (gps.canGetLocation()) {
Log.i("Debug", "BIN IN DER IF SCHLEIFE");
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String slatitude = Double.toString(latitude);
            String slongitude = Double.toString(longitude);

            SendGPS sendeGpsDaten = new SendGPS();
            sendeGpsDaten.postData(slatitude, slongitude);
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\n GPS sent to server!", Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        //SendGPS sendNewGpsData = new SendGPS();
        //sendNewGpsData.postData("55","55");
    }
}
