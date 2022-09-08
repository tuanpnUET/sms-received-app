package com.app.smsreceivedapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InputBankIDActivity extends AppCompatActivity {
    EditText bankID;
    String strBankID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankid);
        bankID = findViewById(R.id.bankID);
    }
    public void goNextClick(View view) {
        strBankID = bankID.getText().toString();
        if (strBankID.equals("")) {
            Toast.makeText(this, "Must set bank id", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(InputBankIDActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("bankID", strBankID);
            startActivity(intent);
        }
    }
}
