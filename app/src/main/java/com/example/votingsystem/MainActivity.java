package com.example.votingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    try{
        Thread splash = new Thread(){
            @Override
            public void run() {

             try {
                 sleep(2000);
                 Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                 startActivity(intent);
                 finish();
             }catch (Exception e){

             }
            }
        };
        splash.start();

    }catch (Exception e){

    }
    }
}