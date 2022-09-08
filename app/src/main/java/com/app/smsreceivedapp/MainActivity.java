package com.app.smsreceivedapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView tvBankID;
    String bankID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPermission(Manifest.permission.RECEIVE_SMS)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }
        tvBankID = findViewById(R.id.tvBankID);
        Intent intent = getIntent();
        bankID = (String) intent.getStringExtra("bankID");
        tvBankID.setText(bankID);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    private boolean checkPermission(String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public void startBackgroundProcessButtonClick(View view) {
        Intent intent = new Intent(this, SMSBroadcastReceiver.class);
        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
        Intent intent2 = new Intent(this, SMSBroadcastReceiver.class);
        intent2.setAction("my.action.string");
        intent2.putExtra("bankID", bankID);
        sendBroadcast(intent2);
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                try {
                    JSONObject data = new JSONObject();
                    data.put("bankID",bankID);
                    String res = new httpRequest().performPostCall(String.format(StringUtil.API_STATUS), data);
                    System.out.println("RESPONSE STATUS "+res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },0,30*1000);

    }
}
