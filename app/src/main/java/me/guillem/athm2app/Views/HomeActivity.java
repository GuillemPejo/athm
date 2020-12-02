package me.guillem.athm2app.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;
import me.guillem.athm2app.Model.Visita;
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
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

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
/*
        Visita v1 = new Visita("12-12-12","13:20","Ramon","Restauracio de pis","La infrastructura bla bla", "-MLhWLAAAAArlOKFjNXj");
        mDatabaseRef.child("Visita").child("-MLhwvv4r32r313D8gsh").push().setValue(v1).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Utils.openActivity(a, ComercosActivity.class);
                            Utils.show(getApplication(),"Felicitats! S'ha afegit correctament");
                        }else{
                            Log.e("ERROR","Opss.. Alguna cosa ha anat malament");
                        }
                    }
                });

*/
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

    protected void onResume() {
        super.onResume();
        bindDades();
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
