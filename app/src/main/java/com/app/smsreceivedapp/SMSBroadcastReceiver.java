package com.app.smsreceivedapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    static String bankID="";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("my.action.string")){
            bankID = intent.getExtras().getString("bankID");
        }
        if(SMS_RECEIVED.equals(intent.getAction())){
            System.out.println("BankID "+bankID);
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i = 0; i < pdus.length; i++){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    System.out.println("messages: "+messages[0].getMessageBody()+" phone: "+messages[0].getDisplayOriginatingAddress());
                    Map<String, String> map = new HashMap<>();
                    map.put("bankID", bankID);
                    map.put("sms", messages[0].getMessageBody());
                    JSONObject data = new JSONObject(map);
                    System.out.println("DATA "+data);

                    if (data!=null) {
                        String res = new httpRequest().performPostCall(String.format(StringUtil.API_SMS), data);
                        System.out.println("Response "+res);
                    }
                }
            }
        }
    }

}
