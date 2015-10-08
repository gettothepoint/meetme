package de.dhbw.meetme;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.hello.R;

import java.io.IOException;

/**
 * Created by kollemar on 28.09.2015.
 */
public class SendGPS {
    private Context mContext;
    private TextView tvUsers;
    private static final String USERNAME = "martin";
    private static final String HOSTNAME = "10.0.2.2";
    private static final int PORT = 8087;

    public SendGPS(Context context) {
        this.mContext = context;

    }
    public String postData(String la, String lo) {
        // Create a new HttpClient and Post Header
        DefaultHttpClient httpClient = new DefaultHttpClient();
        // /meetmeserver/api/ "+la+"/"+la

        try {
            // Execute HTTP Post Request
            HttpHost targetComputer = new HttpHost(HOSTNAME, PORT, "http");
            HttpPut putRequest = new HttpPut("/meetmeserver/api/login/martink/55/55");
            HttpResponse httpResponse = httpClient.execute(targetComputer, putRequest);
           // HttpEntity entity = httpResponse.getEntity();

            //tvUsers = (TextView)
            //Toast.makeText(mContext, "msg msg", Toast.LENGTH_LONG).show();
            //Toast bread = Toast.makeText(mContext, resp, 5000);
            //bread.show();

           return httpResponse.toString(); //EntityUtils.toString(entity);

        } catch (ClientProtocolException e) {
            Toast.makeText(mContext, "Error", 5000).show();
            return e.toString();
        } catch (IOException e) {
            Toast.makeText(mContext, "Error", 5000).show();
            return e.toString();
        }
}}
