package me.guillem.athm2app.Model;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.FilterHelper;
import me.guillem.athm2app.Utils.Utils;
import me.guillem.athm2app.Views.DetailObra;

import static me.guillem.athm2app.Utils.Utils.searchString;

public class RecyclerAdapterCardsHome extends RecyclerView.Adapter<RecyclerAdapterCardsHome.ViewHolder> implements Filterable {

    private Context context;
    public List<Obra> llistadobres;
    private List<Obra> filterObra;
    private FilterHelper filterHelper;

    interface ItemClickListener {
        void onItemClick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tiol, adreça, ref, responsable;
        private FrameLayout barra;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adreça = itemView.findViewById(R.id.adreça);
            tiol = itemView.findViewById(R.id.titol);
            ref = itemView.findViewById(R.id.referencia);
            responsable = itemView.findViewById(R.id.responsable);
            barra = itemView.findViewById(R.id.barra);
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

    public RecyclerAdapterCardsHome(Context context, List<Obra> llistadobres) {
        this.context = context;
        this.llistadobres = llistadobres;
        this.filterObra = llistadobres;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Obra o = llistadobres.get(position);

        holder.adreça.setText(o.getAdreça());
        holder.tiol.setText(o.getTitol());
        holder.ref.setText(o.getRef());
        holder.responsable.setText(o.getTecnic());
        if(o.getEstat().equals("PENDENT")){
            holder.barra.setBackgroundColor(Color.parseColor("#FFC107"));
        }else if(o.getEstat().equals("EN CURS")) {
            holder.barra.setBackgroundColor(Color.parseColor("#4fca30"));
        }else{
            holder.barra.setBackgroundColor(Color.parseColor("#081346"));
        }

        //Obtenim el nom i la categoria
        String descrip_obra = o.getTitol().toLowerCase(Locale.getDefault());
        String resp_obra = o.getTecnic().toLowerCase(Locale.getDefault());


        //Ressaltem el nom mentres s'estigui buscan
        if (descrip_obra.contains(searchString) && !(searchString.isEmpty())) {
            int startPos = descrip_obra.indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(holder.tiol.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tiol.setText(spanString);
        } else {
            //Utils.show(ctx, "Search string empty");
        }

        //Ressaltem la categoria mentres s'estigui buscan
        if (resp_obra.contains(searchString) && !(searchString.isEmpty())) {

            int startPos = resp_obra.indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(holder.responsable.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.responsable.setText(spanString);
        }

        //Obre detailactivity quan es clickat
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                Utils.sendObraToActivity(RecyclerAdapterCardsHome.this.context, o,
                        DetailObra.class);
            }
        });


    }

    @Override
    public int getItemCount() {
            return llistadobres.size();
    }

    @Override
    public Filter getFilter() {
        if(filterHelper==null){
            filterHelper=FilterHelper.newInstance(filterObra,this);
        }
        return filterHelper;
    }
}

