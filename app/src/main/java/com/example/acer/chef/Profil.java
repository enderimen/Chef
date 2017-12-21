package com.example.acer.chef;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by osman_000 on 15.12.2017.
 */

public class Profil extends Activity{

    private TextView tvBaslik, tvKisi, tvTarif = null;
    private String strBaslik, strTarif = null;
    private int kisi = -1;

    private Bundle extras = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasim_yap);
        init();
    }
    private void init() {
        tvBaslik = (TextView) findViewById(R.id.edit_yemek_adi);
        tvKisi  = (TextView) findViewById(R.id.txt_seekbar_ekranda_goster);
        tvTarif   = (TextView) findViewById(R.id.edit_tarif);

        /** Renkleri degistirelim */
        tvBaslik.setTextColor(Color.GRAY);
        tvKisi.setTextColor(Color.GRAY);
        tvTarif.setTextColor(Color.GRAY);

        /** Bir onceki sayfadan verileri alalim */
        extras = getIntent().getExtras();

        /** Verileri alip atamalari yapalim */
        strBaslik = extras.getString(PaylasimYap.yemek_basligi);
        strTarif   = extras.getString(PaylasimYap.tarif_yaz);
        kisi     = extras.getInt(PaylasimYap.kac_kisilik);

        /** Aldigimiz verileri ekranda gosterelim */
        tvBaslik.setText(strBaslik);
        tvKisi.setText(String.valueOf(kisi));
        tvTarif.setText(strTarif);
    }

  }
