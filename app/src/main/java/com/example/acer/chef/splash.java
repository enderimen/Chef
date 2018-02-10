package com.example.acer.chef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Acer E1 571G on 3.12.2017.
 */

public class splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Animation downToUp , upToDown;

        ImageView logo;
        logo = (ImageView) findViewById(R.id.imageViewId);
        logo.setImageResource(R.drawable.chef);

        ImageView chefText;
        chefText = (ImageView) findViewById(R.id.chef_text);
        chefText.setImageResource(R.drawable.chef_text);

        downToUp = AnimationUtils.loadAnimation(this,R.anim.down_to_up);

        upToDown = AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        logo.setAnimation(downToUp);
        chefText.setAnimation(upToDown);

        Thread timerThread = new Thread(){

            public void run(){
                try {
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent;
                    intent = new Intent(splash.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
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
