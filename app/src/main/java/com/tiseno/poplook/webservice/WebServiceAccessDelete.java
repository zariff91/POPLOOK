package com.tiseno.poplook.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import com.tiseno.poplook.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by billygoh on 9/14/15.
 */
public class WebServiceAccessDelete extends AsyncTask<RequestBody, Void, JSONObject> {

    ProgressDialog progressDialog;
    AsyncTaskCompleteListener<JSONObject> callback;
    Context curContext;
    String action;
    OkHttpClient client = new OkHttpClient();

    public WebServiceAccessDelete(Context context, AsyncTaskCompleteListener<JSONObject> listener){
        this.callback = listener;
        curContext = context;
    }

    public void setAction(String action1){
        action=action1;
    }

    public JSONObject SendHttpDelete(RequestBody postParams) {
        try {
            Log.i("" + " WEB SERVICE START", "DELETE URL PARAMS: "+ action);
            Request request = new Request.Builder()
                    .url("https://poplook.com/webapi/"+action)
//                    .url("https://dev3.poplook.com/webapi/"+action)

                    .delete(postParams)
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
        progressDialog = ProgressDialog.show(curContext, null, null, true, false);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.getWindow().setDimAmount(0);
    }


    @Override
    protected JSONObject doInBackground(RequestBody... params) {
        Log.i("" + " WEB SERVICE START", "DELETE SENDING REQUESTBODY: "+bodyToString(params[0]));
        if (params[0] == null) {
            return SendHttpDelete(null);
        } else {
            return SendHttpDelete(params[0]);
        }
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            progressDialog.dismiss();

            if (json != null) {
                Log.i("" + " WEB SERVICE DONE", "DELETE RECEIVE JSON: " + json.toString());
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
