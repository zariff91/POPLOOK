package com.tiseno.poplook.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rahn on 5/18/16.
 */
public class WebServiceAccessGetWithoutLoading extends AsyncTask<String, Void, JSONObject> {


    AsyncTaskCompleteListener<JSONObject> callback;
    static Context curContext;
    OkHttpClient client = new OkHttpClient();

    public WebServiceAccessGetWithoutLoading(Context context, AsyncTaskCompleteListener<JSONObject> listener){
        this.callback = listener;
        curContext = context;
    }

    public JSONObject SendHttpGet(String action) {
        try {
            Log.i("" + " WEB SERVICE START", "GET URL PARAMS: "+ action);
            Request request = new Request.Builder()
                    .url("https://poplook.com/webapi/"+action)
//                   .url("https://dev3.poplook.com/webapi/"+action)
                    .get()
                    .build();

            Response response =  client.newCall(request).execute();

            long tx = response.sentRequestAtMillis();
            long rx = response.receivedResponseAtMillis();
            System.out.println("okhttp3 response time : "+(rx - tx)+" ms");
            JSONObject json=new JSONObject(response.body().string());
            return json;
        }
        catch (Exception e)
        {
            // More about HTTP exception handling in another tutorial.
            // For now we just print the stack trace.
            e.printStackTrace();
            Log.i("" + " WS PARSING TASK", "Connection Timeout!" + e.getMessage());
            return null;
        }

    }


    @Override
    public void onPreExecute() {

    }


    @Override
    protected JSONObject doInBackground(String... params) {
        if (params[0] == null) {
            return SendHttpGet(null);
        } else {
            return SendHttpGet(params[0]);
        }
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            if (json != null) {
                Log.i("" + " WEB SERVICE DONE", "GET RECEIVE JSON: " + json.toString());
                callback.onTaskComplete(json);
            } else {
                callback.onTaskComplete(null);
            }

        } catch (Exception e) {

        }

    }

}
