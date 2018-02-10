package com.example.acer.chef;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;



public class KullaniciProfil extends AppCompatActivity {

    private EditText etKullaniciAdi,etSoyadi ,etMail ,etSifre;
    private TextView txKullaniciAdi,txSoyadi ,txMail ,txSifre;
    private Button guncelle_buton;
    private ImageButton resimSec;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        progressDialog = new ProgressDialog(this);

        init();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("KullaniciBilgileri");
        storageReference = FirebaseStorage.getInstance().getReference("KullaniciResim");

        guncelle_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Bilgileriniz Güncelleniyor...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                BilgiGuncelle();

                ResimYukle();

                progressDialog.dismiss();
            }
        });

        resimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
    }

    private void resimSec() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //resimyolu=kb.getResimyolu();

                String kullaniciAdi = dataSnapshot.child("k_adi").getValue().toString();
                String kullaniciSoyadi = dataSnapshot.child("k_soyadi").getValue().toString();
                String kullaniciMail = dataSnapshot.child("k_mail").getValue().toString();
                String kullaniciSifre = dataSnapshot.child("k_sifre").getValue().toString();

                etKullaniciAdi.setText(kullaniciAdi);
                etSoyadi.setText(kullaniciSoyadi);
                etMail.setText(kullaniciMail);
                etSifre.setText(kullaniciSifre);

                //Glide.with(getApplicationContext()).load(kb.getResimyolu()).into(profil_resim);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void BilgiGuncelle(){

        String k_adi = etKullaniciAdi.getText().toString().trim();
        String k_soyadi= etSoyadi.getText().toString().trim();
        String k_mail = etMail.getText().toString().trim();
        String k_sifre = etSifre.getText().toString().trim();

        if(!TextUtils.isEmpty(k_adi) && !TextUtils.isEmpty(k_soyadi) && !TextUtils.isEmpty(k_mail) && !TextUtils.isEmpty(k_sifre)){

            String kullaniciID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            KullaniciBilgileri kullaniciBilgileri = new KullaniciBilgileri(kullaniciID , k_adi , k_soyadi , k_mail , k_sifre);

            databaseReference.child(kullaniciID).setValue(kullaniciBilgileri);
        }
        else{
            Toast.makeText(this, "Lütfen tüm bilgilerinizi giriniz.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                resimSec.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ResimYukle()
    {
            final String KullaniciID =  FirebaseAuth.getInstance().getCurrentUser().getUid();

            if(filePath!=null) {
                storageReference.child(KullaniciID).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getApplicationContext(), "Bilgileriniz Güncellendi.", Toast.LENGTH_SHORT).show();

                    }
                });
            }else{
                Toast.makeText(this, "Lütfen bir profil resmi yükleyiniz.", Toast.LENGTH_SHORT).show();
            }
    }

    void init(){

        resimSec = (ImageButton) findViewById(R.id.img_kullanici_resmi);

        etKullaniciAdi = (EditText) findViewById(R.id.edit_kullanici_adi);

        etSoyadi = (EditText) findViewById(R.id.edit_kullanici_soyadi);

        etMail = (EditText) findViewById(R.id.edit_kullanici_mail);

        etSifre = (EditText) findViewById(R.id.edit_kullanici_sifre);

        guncelle_buton = (Button) findViewById(R.id.button_guncelle);

    }
}
