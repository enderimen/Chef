package com.example.acer.chef;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.app.TimePickerDialog;
import com.xw.repo.BubbleSeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PaylasimYap extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasim_yap);

        BubbleSeekBar bubbleSeekBar=(BubbleSeekBar)findViewById(R.id.seekBar);
        final TextView textView=(TextView)findViewById(R.id.txtView);

        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                textView.setText(String.format("%d kişilik tarif",progress));
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }
}
