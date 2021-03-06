package me.guillem.athm2app.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Locale;

import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.FilterHelper;
import me.guillem.athm2app.Utils.Utils;
import me.guillem.athm2app.Views.DetailObra;



/**
 * * Created by Guillem on 30/11/20.
 */
public class RecyclerAdapterCardsVisits extends RecyclerView.Adapter<RecyclerAdapterCardsVisits.ViewHolder> {

    private Context context;
    public List<Visita> llistavisites;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();



    interface ItemClickListener {
        void onItemClick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tiol, descripcio, data;
        private ItemClickListener itemClickListener;
        private ImageView  image, user;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcio = itemView.findViewById(R.id.descrip);
            tiol = itemView.findViewById(R.id.titol);
            data = itemView.findViewById(R.id.data);
            image = itemView.findViewById(R.id.image);
            user = itemView.findViewById(R.id.user);


            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }

        void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

    }

    public RecyclerAdapterCardsVisits(Context context, List<Visita> llistavisites) {
        this.context = context;
        this.llistavisites = llistavisites;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_item_visita,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Visita v = llistavisites.get(position);

        String url = v.getMedia().get(0);
        File f = new File(url);
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
        if(bmp!=null){
            holder.image.setImageBitmap(bmp);
        }else{
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.default_visit));
        }

        Glide.with(context).load(mAuth.getCurrentUser().getPhotoUrl()).into(holder.user);

        holder.data.setText(v.getData_visita());
        holder.tiol.setText(v.getNom_visit());
        holder.descripcio.setText(v.getDescripcio());

        //Obre detailactivity quan es clickat
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                /*Utils.sendObraToActivity(RecyclerAdapterCardsHome.this.context, v,
                        DetailObra.class);*/
                System.out.println("Ha fet click al item: "+ pos);
            }
        });


    }

    @Override
    public int getItemCount() {
        return llistavisites.size();
    }


}

