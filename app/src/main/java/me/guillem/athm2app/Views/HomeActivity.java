package me.guillem.athm2app.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.FirebaseCRUD;
import me.guillem.athm2app.Utils.Utils;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private RecyclerView rv;
    public MaterialCardView card;
    public ProgressBar mProgressBar;
    private FirebaseCRUD recurs_crud = new FirebaseCRUD();
    private LinearLayoutManager layoutManager;
    RecyclerAdapterCardsHome adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        card = findViewById(R.id.card);


        mProgressBar = findViewById(R.id.mProgressBarLoad);
        mProgressBar.setIndeterminate(true);

        Utils.showProgressBar(mProgressBar);

        rv = findViewById(R.id.recycler_cards);

        layoutManager = new LinearLayoutManager(this);

        rv.setLayoutManager(layoutManager);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), layoutManager.getOrientation());
        //rv.addItemDecoration(dividerItemDecoration);
        adapter=new RecyclerAdapterCardsHome(this,Utils.DataCache);

        rv.setAdapter(adapter);

        bindDades();
    }



    private void bindDades(){
        recurs_crud.select(this,Utils.getDatabaseRefence(),mProgressBar,rv,adapter);
    }

    public void afegirDes(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.comerce_page_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
        searchView.setQueryHint("Buscar");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Utils.sendObraToActivity(this,null,CRUDActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {return false;}

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {return false;}

    @Override
    public boolean onQueryTextSubmit(String query) {return false;}

    @Override
    public boolean onQueryTextChange(String query) {
        Utils.searchString=query;
        RecyclerAdapterCardsHome adapter=new RecyclerAdapterCardsHome(this,Utils.DataCache);
        adapter.getFilter().filter(query);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        return false;
    }
}
