package me.guillem.athm2app.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.Utils;
import me.guillem.athm2app.Views.DetailObra;

public class RecyclerAdapterCardsMap extends RecyclerView.Adapter<RecyclerAdapterCardsMap.ViewHolder>{

    private Context context;
    public List<Obra> llistadobres;

    interface ItemClickListener {
        void onItemClick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tiol, adreça, estat;
        private Button button;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adreça = itemView.findViewById(R.id.adreça);
            tiol = itemView.findViewById(R.id.titol);
            estat = itemView.findViewById(R.id.estat);
            button = itemView.findViewById(R.id.button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }

        void setItemClickListener(RecyclerAdapterCardsMap.ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

    }

    public RecyclerAdapterCardsMap(Context context, List<Obra> llistadObres){
        this.context = context;
        this.llistadobres = llistadObres;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardmap_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Obra o = llistadobres.get(position);

        holder.adreça.setText(o.getAdreça());
        holder.tiol.setText(o.getTitol());
        holder.estat.setText(o.getEstat());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendObraToActivity(RecyclerAdapterCardsMap.this.context, o,
                        DetailObra.class);
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                Utils.sendObraToActivity(RecyclerAdapterCardsMap.this.context, o,
                        DetailObra.class);
            }
        });


    }

    @Override
    public int getItemCount() {
        return llistadobres.size();
    }



}
