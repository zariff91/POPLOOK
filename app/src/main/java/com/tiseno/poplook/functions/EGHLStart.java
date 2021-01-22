package com.tiseno.poplook.functions;

/**
 * Created by rahnrazamai on 15/12/2016.
 */

import java.util.HashMap;

import com.epay.eghl.EGHLAPI;
import com.epay.eghl.EGHLInterface;
import com.epay.eghl.EGHLSaleActivity;
import com.epay.eghl.EGHLSerializableMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class EGHLStart extends EGHLSaleActivity{

    private String TAG = "EGHL-SDEMO";

    private EGHLInterface EPayCallback = null;
    private HashMap<String, String> eGHLParams = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setContentView(R.layout.activity_demo);  Don`t call 'setContentView' if extend EGHLSaleActivity at this activity.

        EGHLSerializableMap serializableMap = (EGHLSerializableMap)getIntent().getSerializableExtra("EGHL_User_Params");
        eGHLParams = serializableMap.getMap();// get all parameters list

        EPayCallback = new EGHLInterface(){

            @Override
            public void EGHLGetStatus(int sta, String status) {
                Log.i(TAG, "Sta:"+sta+" : "+status);
                if(EGHLAPI.EGHL_STA_OK == sta){
                    ToQueryPaymentAndShowPage();
                }
            }

            @Override
            public void EGHLGetError(int ret, String error) {
                String errVal ="Err: "+ret+" "+error;
                Toast.makeText(EGHLStart.this, errVal, Toast.LENGTH_LONG).show();
            }
        };
        this.setEPayCallBack(EPayCallback);
    }

    protected void ToQueryPaymentAndShowPage(){
        Intent result = new Intent();
        result.putExtra("PaymentGateway", eGHLParams.get("PaymentGateway"));
        result.putExtra("Password", eGHLParams.get("Password"));
        result.putExtra("PymtMethod", eGHLParams.get("PymtMethod"));
        result.putExtra("ServiceID", eGHLParams.get("ServiceID"));
        result.putExtra("PaymentID", eGHLParams.get("PaymentID"));
        result.putExtra("Amount", eGHLParams.get("Amount"));
        result.putExtra("CurrencyCode", eGHLParams.get("CurrencyCode"));

        result.setClass(this, EGHLSaleResult.class);
        startActivity(result);

        finish();
    }
}
