package com.example.acer.chef;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int RC_SIGN_IN = 1;


    ViewPager viewPager;

    LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);

    ProgressDialog progressDialog;
    TextView navKullaniciAdi,navKullaniciMail;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private RecyclerView recyclerView;
    private List<MPaylasimlar> Paylasim_listele;
    private RecyclerView.Adapter adapter;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FloatingActionButton fab;
    String kullaniciAdi ,kullaniciSoyadi,kullaniciMail;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Toast.makeText(getApplicationContext(),"Profile Tıklandı.", Toast.LENGTH_LONG).show();

        } else if (id == R.id.hakkimizda){

            Intent intent = new Intent(getApplicationContext(),About.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);

        }
        else if (id == R.id.nav_cikis) {

            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Çıkış yapmak istediğinizden emin misiniz?");
            builder.setCancelable(true);
            builder.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    mFirebaseAuth.signOut();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);

                }
            });

            builder.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Hiçbir şey yapma
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        } else if (id == R.id.nav_manage) {

            Intent intent = new Intent(getApplicationContext(),KullaniciProfil.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for Slider
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer slideTimer = new Timer();
        slideTimer.scheduleAtFixedRate(new SlideTimer(),4000,4000);

        // for posts list
        Paylasim_listele = new ArrayList<>();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EE3B24")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, PaylasimYap.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navKullaniciAdi = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navKullaniciAdi);
        navKullaniciMail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navKullaniciMail);

        recyclerView = (RecyclerView) findViewById(R.id.paylasimListele);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Veriler Yüklenirken Lütfen Bekleyin...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Paylasimlar");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    String id = dataSnapshot1.child("id").getValue().toString();
                    String paylasimAdi = dataSnapshot1.child("paylasimadi").getValue().toString();
                    String tarih = dataSnapshot1.child("tarih").getValue().toString();
                    String resimYolu = dataSnapshot1.child("resimyolu").getValue().toString();
                    String kategori = dataSnapshot1.child("kategori").getValue().toString();
                    String icindekiler = dataSnapshot1.child("icindekiler").getValue().toString();
                    String tarif = dataSnapshot1.child("tarif").getValue().toString();
                    int kisiSayisi = Integer.parseInt(dataSnapshot1.child("kisiSayisi").getValue().toString());
                    String hazirlanmaSuresi = dataSnapshot1.child("hazirlanmaSuresi").getValue().toString();
                    String pisirmeSuresi = dataSnapshot1.child("pisirmeSuresi").getValue().toString();
                    String k_ad_soyad = dataSnapshot1.child("k_ad_soyad").getValue().toString();

                    Paylasim_listele.add(new MPaylasimlar(id,paylasimAdi,tarih,resimYolu,kategori,icindekiler,tarif,kisiSayisi,hazirlanmaSuresi,pisirmeSuresi,k_ad_soyad));
                }

                adapter = new PaylasimAdapter(Paylasim_listele,R.layout.paylasim_node);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("KullaniciBilgileri");

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //resimyolu=kb.getResimyolu();

                kullaniciAdi = dataSnapshot.child("k_adi").getValue().toString();
                kullaniciSoyadi = dataSnapshot.child("k_soyadi").getValue().toString();
                kullaniciMail = dataSnapshot.child("k_mail").getValue().toString();

                navKullaniciAdi.setText(kullaniciAdi + " " + kullaniciSoyadi);
                navKullaniciMail.setText(kullaniciMail);

                //Glide.with(getApplicationContext()).load(kb.getResimyolu()).into(profil_resim);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null ){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            if (requestCode==RESULT_OK){
                Toast.makeText(this, "Hoş geldin" + mFirebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            }else if(resultCode==RESULT_CANCELED){
                finish();
            }
        }
    }

    public class SlideTimer extends TimerTask{

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    }else if(viewPager.getCurrentItem() == 2){
                        viewPager.setCurrentItem(3);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
