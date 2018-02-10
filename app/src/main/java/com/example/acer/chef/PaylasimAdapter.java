package com.example.acer.chef;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Acer E1 571G on 26.12.2017.
 */

public class PaylasimAdapter extends RecyclerView.Adapter<PaylasimAdapter.PaylasimViewHolder> {

    private List<MPaylasimlar> list;
    int itemLayout;

    public PaylasimAdapter(List<MPaylasimlar> list,int itemLayout) {

        this.list = list;
        this.itemLayout = itemLayout;
    }

    @Override
    public PaylasimViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        PaylasimViewHolder paylasimViewHolder = new PaylasimViewHolder(view);

        return  paylasimViewHolder;
    }

    @Override
    public void onBindViewHolder(PaylasimAdapter.PaylasimViewHolder holder, int position) {

        final MPaylasimlar PaylasimBilgileri = list.get(position);

        holder.yemekAdi.setText(PaylasimBilgileri.getPaylasimadi());
        holder.kullaniciAdi.setText(PaylasimBilgileri.getK_ad_soyad());
        holder.pisirmeSuresi.setText(PaylasimBilgileri.getPisirmeSuresi()+ " dk");

        Glide.with(holder.itemView.getContext()).load(PaylasimBilgileri.getResimyolu()).into(holder.paylasimFoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaylasimViewHolder extends RecyclerView.ViewHolder{

        TextView yemekAdi,kullaniciAdi,pisirmeSuresi;
        ImageView paylasimFoto;

        public PaylasimViewHolder(View itemView) {

            super(itemView);

            yemekAdi = itemView.findViewById(R.id.yemekAdi);
            kullaniciAdi = itemView.findViewById(R.id.kullaniciAdi);
            pisirmeSuresi = itemView.findViewById(R.id.pismeSuresi);
            paylasimFoto = itemView.findViewById(R.id.paylasimFoto);
        }
    }
}
