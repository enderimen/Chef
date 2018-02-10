package com.example.acer.chef;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xw.repo.BubbleSeekBar;

import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;

public class PaylasimYap extends AppCompatActivity {

    ImageView img;

    private EditText etYemekAdi, etHazirlanmaSuresi,etPismeSuresi,etTarif,edit_icindekiler;
    private BubbleSeekBar kacKislik;

    private Button gonder;
    private ImageButton imagebutton;
    private Uri filePath;

    String kullaniciAdi ,kullaniciSoyadi;

    ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasim_yap);

        kullaniciBilgiCek();

        databaseReference=FirebaseDatabase.getInstance().getReference("Paylasimlar");
        storageReference= FirebaseStorage.getInstance().getReference("PaylasimResim");

        init();

        final TextView textView =(TextView)findViewById(R.id.txt_seekbar_ekranda_goster);

        kacKislik.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
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
                resimSec();
            }
        });

        gonder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                paylas();

                progressDialog = new ProgressDialog(PaylasimYap.this);
                progressDialog.setMessage("Gönderi Paylaşılıyor...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

            }
        });
    }

    private void resimSec() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagebutton.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kullaniciBilgiCek(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("KullaniciBilgileri");

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //resimyolu=kb.getResimyolu();

                kullaniciAdi = dataSnapshot.child("k_adi").getValue().toString();
                kullaniciSoyadi = dataSnapshot.child("k_soyadi").getValue().toString();

                //Glide.with(getApplicationContext()).load(kb.getResimyolu()).into(profil_resim);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {

        etYemekAdi = (EditText)findViewById(R.id.edit_yemek_adi);
        etHazirlanmaSuresi = (EditText)findViewById(R.id.edit_zaman_hazirlanma);
        etPismeSuresi= (EditText) findViewById(R.id.edit_zaman_pisme);
        etTarif= (EditText) findViewById(R.id.edit_tarif);
        kacKislik=(BubbleSeekBar)findViewById(R.id.seekBar);
        edit_icindekiler=(EditText)findViewById(R.id.edit_icindekiler);
        imagebutton=(ImageButton)findViewById(R.id.image_resim_yukle);
        gonder = (Button) findViewById(R.id.button_gonder);
    }

    public void paylas(){

        final String yemekadi = etYemekAdi.getText().toString().trim();
        final String HazirlanmaSuresi = etHazirlanmaSuresi.getText().toString().trim();
        final String PismeSuresi = etPismeSuresi.getText().toString().trim();
        final String Tarif = etTarif.getText().toString().trim();
        final String Icindekiler = edit_icindekiler.getText().toString().trim();
        final int kacKislikDeger = kacKislik.getProgress();

        final String id=databaseReference.push().getKey();

        storageReference.child(id).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String resimYolu = taskSnapshot.getDownloadUrl().toString();

                if (TextUtils.isEmpty(HazirlanmaSuresi) || TextUtils.isEmpty(PismeSuresi)){

                    Toast.makeText(PaylasimYap.this, "Lütfen geçerli bir süre giriniz.", Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(yemekadi) ||  TextUtils.isEmpty(HazirlanmaSuresi) || TextUtils.isEmpty(PismeSuresi) || TextUtils.isEmpty(Tarif) || resimYolu=="" || TextUtils.isEmpty(Icindekiler) || kacKislikDeger < 0){

                    Toast.makeText(PaylasimYap.this, "Tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();

                }else {

                    String K_ad_soyad = kullaniciAdi + " " + kullaniciSoyadi;

                    MPaylasimlar mPaylasimlar=new MPaylasimlar(id,yemekadi, "",resimYolu , "", Icindekiler,Tarif,  kacKislikDeger,  HazirlanmaSuresi, PismeSuresi, K_ad_soyad);

                    databaseReference.child(id).setValue(mPaylasimlar);
                    progressDialog.dismiss();
                    Toast.makeText(PaylasimYap.this, "Paylaşıldı", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplication(),MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,R.anim.right_out);

                }

                etYemekAdi.setText("");
                etHazirlanmaSuresi.setText("");
                etPismeSuresi.setText("");
                etTarif.setText("");
                edit_icindekiler.setText("");
            }
        });
    }
}
