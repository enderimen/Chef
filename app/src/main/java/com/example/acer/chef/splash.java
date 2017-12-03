package com.example.acer.chef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * Created by Acer E1 571G on 3.12.2017.
 */

public class splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView logo;
        logo = (ImageView) findViewById(R.id.imageViewId);
        logo.setImageResource(R.drawable.chef);

        ImageView text;
        text = (ImageView) findViewById(R.id.chef_text);
        text.setImageResource(R.drawable.chef_text);

        Thread timerThread = new Thread(){

            public void run(){
                try {
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent;
                    intent = new Intent(splash.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
