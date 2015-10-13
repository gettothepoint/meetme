package de.dhbw.meetme;

import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
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
import org.apache.http.util.EntityUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import javax.ws.rs.client.Client;
import java.io.IOException;

/**
 * Created by kollemar on 22.09.2015.
 */
public class HttpConnection {

    private static final String HOSTNAME = "10.0.2.2";
    private static final int PORT = 8087;

    private Context mContext;
    public HttpConnection(Context context) {
        this.mContext = context;

    }

        public String callClient() {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            try{
                HttpHost targetComputer = new HttpHost(HOSTNAME, PORT, "http");

                HttpGet getRequest = new HttpGet("/meetmeserver/api/login/list");
                HttpResponse httpResponse = httpClient.execute(targetComputer,getRequest);
                HttpEntity entity = httpResponse.getEntity();
                //Toast.makeText(mContext, EntityUtils.toString(entity), Toast.LENGTH_LONG).show();

                return EntityUtils.toString(entity);
            }
            catch (Exception e){
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                return e.toString();
            }

            //Default GET need POST
        }



}
