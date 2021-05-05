package com.example.carbluetoothcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();

                //Call this when your activity is done and should be closed.
                // The ActivityResult is propagated back to whoever launched you via onActivityResult().
               finish();

            }
        },SPLASH_SCREEN);




    }

    public void nextActivity(){
        Intent intent=new Intent(this, HomePage.class);
        startActivity(intent);
    }

}