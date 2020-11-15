package me.guillem.athm2app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button button;
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    RecyclerAdapterCardsHome recyclerAdapter;
    public List<Obra> DataCache = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obra obra = new Obra("Carrer Padilla 20", "Acondicionament de pis", "REF2203", "EN CURS", "Juan","Municipal", "01-09-2020","01-11-2021","30 dies", "01-11-2021",41.4254078, 2.200993,"3");
        //myRef.child("Obra").push().setValue(obra);
        initView();

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_cards);
        Log.e("PAS1","nono");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Log.e("PAS2","nono");

        myRef.child("Obra").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataCache.clear();
                Log.e("PAS3","nono");
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Log.e("PAS4","nono");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Obra obra = ds.getValue(Obra.class);
                        obra.setKey(ds.getKey());
                        DataCache.add(obra);
                    }
                    System.out.println(DataCache.size());
                    recyclerAdapter = new RecyclerAdapterCardsHome(MainActivity.this,DataCache);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
/*
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(recyclerAdapter);*/

                }else {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Buscar..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void afegirDes(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    }