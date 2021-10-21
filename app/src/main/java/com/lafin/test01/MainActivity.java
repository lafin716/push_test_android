package com.lafin.test01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FCM 연동
        Intent fcm = new Intent(getApplicationContext(), FirebaseService.class);
        startService(fcm);

        setContentView(R.layout.activity_main);
    }
}