package com.example.acer.chef;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xw.repo.BubbleSeekBar;

import android.view.View.OnClickListener;

public class PaylasimYap extends AppCompatActivity {


    EditText et;
    ImageView img;

    public static String yemek_basligi = "baslik";
    public static String kac_kisilik  = "kac_kisi";
    public static String tarif_yaz = "tarif";

    private EditText etBaslik, etKisi, etTarif = null;
    private String strBaslik, strKisi, strTarif = null;
    private int kisi = -1;
    private Button gonder = null;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference textMessage;
    private static final int IMAGE_PICK = 1;
    private ImageButton imagebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasim_yap);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        textMessage=databaseReference.child("textMessage");


        init();
        BubbleSeekBar bubbleSeekBar=(BubbleSeekBar)findViewById(R.id.seekBar);
        final TextView textView=(TextView)findViewById(R.id.txt_seekbar_ekranda_goster);

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

        imagebutton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Bir Fotoðraf Seçin"), IMAGE_PICK);

            }
        });
        gonder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(PaylasimYap.this,MainActivity.class);
                intent.putExtra("veri", et.getText().toString());
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_PICK:
                    this.imageFromGallery(resultCode, data);
                    break;
                default:
                    break;
            }
        }
    }
    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        this.imagebutton.setImageBitmap(BitmapFactory.decodeFile(filePath));
        cursor.close();

    }

    private void init() {

        gonder = (Button) findViewById(R.id.button_gonder);
        et = (EditText)findViewById(R.id.edit_yemek_adi);

        imagebutton=(ImageButton)findViewById(R.id.image_resim_yukle);
        gonder = (Button) findViewById(R.id.button_gonder);
        gonder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickedSubmit();
            }
        });
    }

    public void paylas(){
        databaseReference = firebaseDatabase.getReference("Paylasimlar");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = databaseReference.push();
        databaseReference.setValue(et);
    }

    private void clickedSubmit() {
        databaseReference = firebaseDatabase.getReference("Paylasimlar");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = databaseReference.push();
        databaseReference.setValue(kisi);
        // Formdan verileri alalim
        strBaslik = etBaslik.getText().toString().trim();
        kisi = Integer.parseInt(etKisi.getText().toString().trim());
        strTarif = etTarif.getText().toString().trim();

        try {
            // Bundle olusturup verileri bundle'a ekleyelim
            Bundle extras = new Bundle();
            extras.putString(yemek_basligi, strBaslik);
            extras.putString(tarif_yaz, strTarif);
            extras.putInt(kac_kisilik, kisi);

            // Intent olusturalim
            Intent intent = new Intent();

            // Bundle'i intente ekleyelim
            intent.putExtras(extras);

            // Yeni sayfayi cagiralim
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
