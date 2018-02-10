package com.example.acer.chef;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Acer E1 571G on 12.01.2018.
 */

public class About extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        TextView version,gelistiriciler,copyright,back_button;
        ImageView logo;

        logo = (ImageView) findViewById(R.id.about_logo);
        logo.setImageResource(R.drawable.chef);

        version = (TextView) findViewById(R.id.app_name);
        gelistiriciler = (TextView) findViewById(R.id.gelistiriciler);
        copyright = (TextView) findViewById(R.id.copyright);
        back_button = (TextView) findViewById(R.id.back_button);

        back_button.setPaintFlags(back_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //text underline

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);

            }
        });
    }
}
