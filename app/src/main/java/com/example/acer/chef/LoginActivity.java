package com.example.acer.chef;
import android.annotation.SuppressLint;
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

import org.w3c.dom.Text;

/**
 * Created by Acer E1 571G on 29.12.2017.
 */

public class LoginActivity extends AppCompatActivity {

    ImageView logo;
    EditText email, sifre;
    Button giris_buton;
    TextView tvKayit_1, tvKayit_2;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialogLogin, progressDialogInternetKontrol;
    InternetKontrol internetKontrol = new InternetKontrol(this);

    @SuppressLint("CutPasteId")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        progressDialogInternetKontrol = new ProgressDialog(this);

        if (!internetKontrol.isConnected(getApplicationContext())) {
            progressDialogInternetKontrol.setMessage("İnternet bağlantınızı kontrol ediniz!");
            progressDialogInternetKontrol.setTitle("Bağlantı Hatası");
            progressDialogInternetKontrol.show();
            progressDialogInternetKontrol.setCanceledOnTouchOutside(true);
        }

        logo = (ImageView) findViewById(R.id.login_logo);
        email = (EditText) findViewById(R.id.etEmail);
        sifre = (EditText) findViewById(R.id.etSifre);
        giris_buton = (Button) findViewById(R.id.buttonGiris);
        tvKayit_1 = (TextView) findViewById(R.id.tvkayit1);
        tvKayit_2 = (TextView) findViewById(R.id.tvkayit2);

        tvKayit_2.setPaintFlags(tvKayit_2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //text underline

        progressDialogLogin = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        giris_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == giris_buton){
                    F_GirisKontrol();
                }
            }
        });

       tvKayit_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == tvKayit_2){
                    Intent intent;
                    intent = new Intent(getApplicationContext() , SignupActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in , R.anim.left_out);
                }
            }
        });
    }

    void F_GirisKontrol(){

        String kullaniciMail = email.getText().toString().trim();
        String kullaniciSifre = sifre.getText().toString().trim();

        if(TextUtils.isEmpty( kullaniciMail ) || TextUtils.isEmpty( kullaniciSifre )) {
            Toast.makeText(this, "Lütfen bilgilerinizi kotrol ediniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialogLogin.setMessage("Giriş Yapılıyor...");
        progressDialogLogin.show();

        firebaseAuth.signInWithEmailAndPassword( kullaniciMail , kullaniciSifre )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            finish();
                            progressDialogLogin.dismiss();
                            startActivity(new Intent(LoginActivity.this , MainActivity.class));
                        }
                        else {
                            progressDialogLogin.dismiss();
                            Toast.makeText(LoginActivity.this, "Giriş Başarısız! Lütfen Bilgilerinizi kontrol ediniz.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
