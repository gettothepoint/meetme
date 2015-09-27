package de.dhbw.meetme;

import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import java.io.IOException;

/**
 * Created by kollemar on 22.09.2015.
 */
public class HttpConnection {
    private final Context mContext;
    public HttpConnection(Context context) {
        this.mContext = context;

    }

    public void postData(String la, String lo) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
      //  HttpGet htget = new HttpGet("http://<your_app_url>/Home/Book/"+la+"/"+lo);
        HttpGet htget = new HttpGet("localhost:8087/meetmeserver/api/user/list");
        try {
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(htget);
            String resp = response.getStatusLine().toString();
            Toast.makeText(mContext, "msg msg", Toast.LENGTH_LONG).show();
            Toast bread = Toast.makeText(mContext, resp, 5000);
            bread.show();


        } catch (ClientProtocolException e) {
           Toast.makeText(mContext, "Error", 5000).show();
        } catch (IOException e) {
           Toast.makeText(mContext, "Error", 5000).show();
        }
    }


}
