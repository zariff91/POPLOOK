package com.tiseno.poplook.functions;

/**
 * Created by rahnrazamai on 15/12/2016.
 */
        import com.epay.eghl.EGHLAPI;
        import com.epay.eghl.EGHLInterface;
        import com.tiseno.poplook.R;

        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;
        import android.widget.TextView;

        import static com.tiseno.poplook.functions.EndlessScrollListener.TAG;

public class EGHLSaleResult extends Activity{
    private static TextView tvResult = null;

    private EGHLAPI eGHLAPI = null;
    private EGHLInterface EGHLCallback = null;
    String TransactionID,TransactionStatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        Intent intent=getIntent();

        String PaymentGateway = intent.getStringExtra("PaymentGateway");
        String Password = intent.getStringExtra("Password");
        String PymtMethod = intent.getStringExtra("PymtMethod");
        String ServiceID = intent.getStringExtra("ServiceID");
        String PaymentID = intent.getStringExtra("PaymentID");
        String Amount = intent.getStringExtra("Amount");
        String CurrencyCode = intent.getStringExtra("CurrencyCode");

        eGHLAPI.setEGHLParams("PaymentGateway", PaymentGateway);
        eGHLAPI.setEGHLParams("Password", Password);
        eGHLAPI.setEGHLParams("PymtMethod", PymtMethod);
        eGHLAPI.setEGHLParams("ServiceID", ServiceID);
        eGHLAPI.setEGHLParams("PaymentID", PaymentID);
        eGHLAPI.setEGHLParams("Amount", Amount);
        eGHLAPI.setEGHLParams("CurrencyCode", CurrencyCode);

        eGHLAPI.EGHLTransaction(EGHLAPI.EGHL_TRANS_QUERY);
    }

    private void initView(){
        eGHLAPI = new EGHLAPI();
            EGHLCallback = new EGHLInterface(){

                @Override
            public void EGHLGetError(int arg0, String arg1) {
                sendHandlerMsg(2, arg1);
                    finish();

                }

            @Override
            public void EGHLGetStatus(int arg0, String arg1) {
                String retVal = "", retKey = null;
                String []mainly_val_keys = { "TxnExists","QueryDesc","PymtMethod","PaymentID","ServiceID","OrderNumber","Amount","TxnID","IssuingBank","TxnStatus","AuthCode","BankRefNo","TxnMessage","CardNoMask","CardType"};
                if(EGHLAPI.EGHL_STA_OK == arg0){
                    for(int i=0; i<mainly_val_keys.length; i++){
                        retKey = mainly_val_keys[i];
                        retVal += retKey+"="+eGHLAPI.EGHLGetResponseValue(retKey)+"\n";
                    }
                    if(eGHLAPI.EGHLGetResponseValue("TxnStatus").equals("0")){
                        TransactionStatus="1";
                    }else if(eGHLAPI.EGHLGetResponseValue("TxnStatus").equals("1")){
                        TransactionStatus="1";
                    }else{
                        TransactionStatus="2";
                    }
                    SharedPreferences pref = EGHLSaleResult.this.getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("TransactionID", eGHLAPI.EGHLGetResponseValue("TxnID"));
                    editor.putString("TransactionStatus", TransactionStatus);
                    editor.apply();

                    sendHandlerMsg(1, retVal);
                    finish();

                }
            }

        };
        eGHLAPI.setEGHLCallBack(EGHLCallback);

    }

    public static Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String retVal = bundle.getString("msg");
            if(1 == msg.what){
                Log.i(TAG, "retVal:"+msg+" : "+retVal);

            }else if (2 == msg.what){
                Log.i(TAG, "retVal:"+msg+" : "+retVal);

            }

        }
    };

    private void sendHandlerMsg(int what, String msg){
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.clear();
        bundle.putString("msg", msg);
        message.setData(bundle);

        myHandler.sendMessage(message);
    }

    public void onBackPressed() {
        finish();
        return;
    }
}