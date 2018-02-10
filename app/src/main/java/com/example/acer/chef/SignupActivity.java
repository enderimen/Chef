package com.example.acer.chef;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Acer E1 571G on 29.12.2017.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialogSignUp , progressDialogInternetKontrol;

    InternetKontrol internetKontrol = new InternetKontrol(this);

    private ImageView logo;
    private EditText adi, soyadi, email, sifre;
    private TextView geriDon;
    private Button kaydol_buton;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        progressDialogInternetKontrol = new ProgressDialog(this);
        progressDialogSignUp = new ProgressDialog(this);

        if(!internetKontrol.isConnected(getApplicationContext()))
        {
            progressDialogInternetKontrol.setMessage("İnternet bağlantınızı kontrol ediniz!");
            progressDialogInternetKontrol.setTitle("İnternet Bağlantısı Sorunu");
            progressDialogInternetKontrol.show();
            progressDialogInternetKontrol.setCanceledOnTouchOutside(true);
        }

        logo = (ImageView) findViewById(R.id.login_logo_text);
        adi = (EditText) findViewById(R.id.etAdi);
        soyadi = (EditText) findViewById(R.id.etSoyadi);
        email = (EditText) findViewById(R.id.etEmail);
        sifre = (EditText) findViewById(R.id.etSifre);
        geriDon = (TextView) findViewById(R.id.geriDon);
        kaydol_buton = (Button) findViewById(R.id.buttonKaydol);

        geriDon.setPaintFlags(geriDon.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //text underline

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("KullaniciBilgileri");

        geriDon.setOnClickListener(this);
        kaydol_buton.setOnClickListener(this);
    }

    public void onClick(View v) {

        if(v == kaydol_buton) {
            F_KayitOl();
        }

        if (v == geriDon){
            Intent intent;
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
    }

    private void F_KayitOl() {

        String mail = email.getText().toString().trim();
        String sifresi = sifre.getText().toString().trim();

        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(sifresi)) {
            Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialogSignUp.setMessage("Lüffen Bekleyiniz!");
        progressDialogSignUp.show();

        firebaseAuth.createUserWithEmailAndPassword(mail,sifresi)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            finish();
                            F_KullaniciBilgileriKayit();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Kayıt işlemi başarısız!", Toast.LENGTH_SHORT).show();
                            progressDialogSignUp.cancel();
                        }
                    }
                });
    }

    private void F_KullaniciBilgileriKayit(){

        String ad = adi.getText().toString().trim();
        String soyad = soyadi.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String sifresi = sifre.getText().toString().trim();

        if(!TextUtils.isEmpty(ad) && !TextUtils.isEmpty(soyad) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(sifresi)){

            FirebaseUser user = firebaseAuth.getCurrentUser();
            String KullaniciID = user.getUid();

            KullaniciBilgileri KullaniciBilgileri = new KullaniciBilgileri(KullaniciID,ad,soyad,mail,sifresi);

            databaseReference.child(KullaniciID).setValue(KullaniciBilgileri);

            Toast.makeText(this, "Kayıt işlemi başarılı!", Toast.LENGTH_SHORT).show();

        }
        else{

            Toast.makeText(this, "Lütfen tüm alanları doldurunuz! ", Toast.LENGTH_SHORT).show();

        }
    }
}