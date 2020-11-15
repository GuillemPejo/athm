package me.guillem.athm2app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapterCardsHome extends RecyclerView.Adapter<RecyclerAdapterCardsHome.ItemViewHolder>{

    List<Obra> llistadobres;
    Context context;


    public RecyclerAdapterCardsHome(Context context, List<Obra> llistadobres) {
        this.context = context;
        this.llistadobres = llistadobres;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.adreça.setText(llistadobres.get(position).getAdreça());
        holder.tiol.setText(llistadobres.get(position).getTitol());
        holder.ref.setText(llistadobres.get(position).getRef());
        holder.responsable.setText(llistadobres.get(position).getTecnic());
        if(llistadobres.get(position).getEstat().equals("PENDENT")){
            holder.barra.setBackgroundColor(Color.parseColor("#FFC107"));
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailObra.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("obra", llistadobres.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
            return llistadobres.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tiol, adreça, ref, responsable;
        FrameLayout barra;
        ImageButton button;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            adreça = itemView.findViewById(R.id.adreça);
            tiol = itemView.findViewById(R.id.titol);
            ref = itemView.findViewById(R.id.referencia);
            responsable = itemView.findViewById(R.id.responsable);
            barra = itemView.findViewById(R.id.barra);
            button = itemView.findViewById(R.id.imgbt2);


        }

    }



}
