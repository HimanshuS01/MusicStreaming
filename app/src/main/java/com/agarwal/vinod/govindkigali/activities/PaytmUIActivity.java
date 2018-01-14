package com.agarwal.vinod.govindkigali.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PaytmUIActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PaytmUI";
    Button btnPaytm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_ui);
        btnPaytm = findViewById(R.id.btn_paytm_start);
        btnPaytm.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_paytm_start:
                Toast.makeText(this, "Yo", Toast.LENGTH_SHORT).show();
                PaytmPGService paytmPGService = PaytmPGService.getStagingService();

                String phn = "7777777777";
                String email = "anirudhgupta281998@gmail.com";





                Map<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , "ZINKIF47808457699612");
                paramMap.put( "ORDER_ID" , "ORD4948534554245654");
                paramMap.put( "CUST_ID" , "111");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
//                paramMap.put( "INDUSTRY_TYPE_ID" , "PAYTM_INDUSTRY_TYPE_ID");
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , "1");
                paramMap.put( "WEBSITE" , "www.govindkigali.com");
                //paramMap.put( "CALLBACK_URL" , "https://www.google.co.in/");
//                paramMap.put( "CALLBACK_URL" , "https://pguat.paytm.com/oltp-web/processTransaction/theia/paytmCallback?ORDER_ID=ORD4948534554245654");
//                paramMap.put( "CALLBACK_URL" , "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=ORDER0000000001");
                paramMap.put( "EMAIL" , email);
                paramMap.put( "MOBILE_NO" , phn);


                TreeMap<String,String> paramTreeMap = new TreeMap<>(paramMap);
                CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
                String checkSum = "";
                try {
                    checkSum = checksumHelper.genrateCheckSum("gU@bYS#dl_IGRzFL", paramTreeMap);
                } catch (Exception e) {
                    Log.d(TAG, "onClick: checkSum failed");
                    e.printStackTrace();
                }


                paramMap.put( "CHECKSUMHASH" ,checkSum);

                PaytmOrder paytmOrder = new PaytmOrder(paramMap);
                paytmPGService.initialize(paytmOrder,null);
                paytmPGService.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Toast.makeText(PaytmUIActivity.this, "UIError", Toast.LENGTH_SHORT).show();
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Toast.makeText(PaytmUIActivity.this, "clientAuthenticationFailed", Toast.LENGTH_SHORT).show();
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Toast.makeText(PaytmUIActivity.this, "onErrorLoadingWebPage", Toast.LENGTH_SHORT).show();

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        Toast.makeText(PaytmUIActivity.this, "onBackPressedCancelTransaction", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }
                });

                break;



        }
    }
}
