package me.guillem.athm2app.Model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.guillem.athm2app.Views.DetailObra;
import me.guillem.athm2app.R;

public class RecyclerAdapterCardsMap extends RecyclerView.Adapter<RecyclerAdapterCardsMap.ViewHolder>{
    List<Obra> llistadObres;
    Context context;

    public RecyclerAdapterCardsMap(Context context, List<Obra> llistadObres){
        this.context = context;
        this.llistadObres = llistadObres;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardmap_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.adreça.setText(llistadObres.get(position).getAdreça());
        holder.tiol.setText(llistadObres.get(position).getTitol());
        holder.estat.setText(llistadObres.get(position).getEstat());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailObra.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("obra", llistadObres.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return llistadObres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tiol, adreça, estat;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adreça = itemView.findViewById(R.id.adreça);
            tiol = itemView.findViewById(R.id.titol);
            estat = itemView.findViewById(R.id.estat);
            button = itemView.findViewById(R.id.button);
        }
    }

}
