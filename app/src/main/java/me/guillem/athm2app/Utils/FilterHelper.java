package me.guillem.athm2app.Utils;

import android.widget.Filter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;

public class FilterHelper extends Filter {
    static List<Obra> currentList;
    static RecyclerAdapterCardsHome adapter;

    @NotNull
    @Contract("_, _ -> new")
    public static FilterHelper newInstance(List<Obra> currentList, RecyclerAdapterCardsHome adapter) {
        FilterHelper.adapter = adapter;
        FilterHelper.currentList = currentList;
        return new FilterHelper();
    }

    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults=new FilterResults();

        if(constraint != null && constraint.length()>0)
        {
            constraint=constraint.toString().toUpperCase();

            //Guarda els items trobats
            ArrayList<Obra> foundFilters=new ArrayList<>();

            String titol,resp;

            //Itera la llista
            for (int i=0;i<currentList.size();i++)
            {
                titol= currentList.get(i).getTitol();
                resp= currentList.get(i).getTecnic();

                //Busca
                if(titol.toUpperCase().contains(constraint)){
                    foundFilters.add(currentList.get(i));
                }else if(resp.toUpperCase().contains(constraint)){
                    foundFilters.add(currentList.get(i));
                }
            }

            //Estableix els resultats a la llista de filtres
            filterResults.count=foundFilters.size();
            filterResults.values=foundFilters;
        }else
        {   //Si no troba cap item, la llista segueix intacte
            filterResults.count=currentList.size();
            filterResults.values=currentList;
        }

        return filterResults;
    }

    protected void publishResults(CharSequence charSequence, @NotNull FilterResults filterResults) {

        adapter.llistadobres= (ArrayList<Obra>) filterResults.values;
        adapter.notifyDataSetChanged();
    }

}