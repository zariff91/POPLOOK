package com.tiseno.poplook.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by billygoh on 9/18/15.
 */
public class WebServiceAccessPostWithoutLoading extends AsyncTask<RequestBody, Void, JSONObject> {


    AsyncTaskCompleteListener<JSONObject> callback;
    static Context curContext;
    String action;
    OkHttpClient client = new OkHttpClient();

    public WebServiceAccessPostWithoutLoading(Context context, AsyncTaskCompleteListener<JSONObject> listener){
        this.callback = listener;
        curContext = context;
    }
    public void setAction(String action1){
        action=action1;
    }

    public JSONObject SendHttpPost(RequestBody postParams) {
        try {
            Log.i("" + " WEB SERVICE START", "POST URL PARAMS: "+ action);
            Request request = new Request.Builder()
                    .url("https://poplook.com/webapi/"+action)
//                    .url("https://dev3.poplook.com/webapi/"+action)
                    .post(postParams)
                    .build();

            Response response = client.newCall(request).execute();
            JSONObject json=new JSONObject(response.body().string());
            return json;

        }
        catch (Exception e)
        {
            // More about HTTP exception handling in another tutorial.
            // For now we just print the stack trace.
            e.printStackTrace();
            Log.i("NDP" + " WS PARSING TASK", "Connection Timeout!" + e.getMessage());
            return null;
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    protected JSONObject doInBackground(RequestBody... params) {
        Log.i("" + " WEB SERVICE START", "POST SENDING REQUESTBODY: "+bodyToString(params[0]));
        if (params[0] == null) {
            return SendHttpPost(null);
        } else {
            return SendHttpPost(params[0]);
        }
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {


            if (json != null) {
                Log.i("" + " WEB SERVICE DONE", "POST RECEIVE JSON: " + json.toString());
                callback.onTaskComplete(json);
            } else {
                callback.onTaskComplete(null);
            }

        } catch (Exception e) {

        }

    }
    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }

}
