package me.guillem.athm2app.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.FirebaseCRUD;
import me.guillem.athm2app.Utils.Utils;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private RecyclerView rv;
    private boolean clicked = false;
    public MaterialCardView card;
    public ProgressBar mProgressBar;
    public FrameLayout layer_transparent;
    private FirebaseCRUD recurs_crud = new FirebaseCRUD();
    private LinearLayoutManager layoutManager;
    protected RecyclerAdapterCardsHome adapter;
    private FloatingActionButton button_new, button_newvisit, button_newobra;
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private Animation fromBottom, rotateOpen, rotateClose, toBottom;
    private TextView tnv, tno;
    private final Context c = HomeActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        card = findViewById(R.id.card);
        layer_transparent = findViewById(R.id.shadow_layer);
        button_new = findViewById(R.id.button_add_new);
        button_newobra = findViewById(R.id.button_new_obra);
        button_newvisit = findViewById(R.id.button_new_visit);
        fromBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_button_anim);
        rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_open_anim);
        toBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.to_button_anim);
        rotateClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_close_button);
        tno = findViewById(R.id.tvobra);
        tnv = findViewById(R.id.tvvisita);


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

        disableFrameLayer();
/*
        button_newvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectObra(c);
            }
        });*/



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
                Utils.sendObraToActivity(this,null, CRUDObras.class);
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

    public void newobra_click(View view) {
        Toast.makeText(this, "New Obra Click", Toast.LENGTH_SHORT).show();
    }
    public void newvisit_click(View view) {
        Toast.makeText(this, "New Visit Click", Toast.LENGTH_SHORT).show();
        Utils.selectObra(c);
    }
    public void addnew_click(View view) {

        if(!clicked){
            //SET VISIBILITY
            button_newobra.setVisibility(View.VISIBLE);
            button_newvisit.setVisibility(View.VISIBLE);
            layer_transparent.setVisibility(View.VISIBLE);
            tno.setVisibility(View.VISIBLE);
            tnv.setVisibility(View.VISIBLE);

            //SET ANIMATION
            button_new.startAnimation(rotateOpen);
            button_newobra.startAnimation(fromBottom);
            button_newvisit.startAnimation(fromBottom);
            tno.startAnimation(fromBottom);
            tnv.startAnimation(fromBottom);

            //CLICKABLE
            button_newobra.setClickable(true);
            button_newvisit.setClickable(true);

            clicked = true;

        }else{
            //SET VISIBILITY
            button_newobra.setVisibility(View.INVISIBLE);
            button_newvisit.setVisibility(View.INVISIBLE);
            layer_transparent.setVisibility(View.INVISIBLE);
            tno.setVisibility(View.INVISIBLE);
            tnv.setVisibility(View.INVISIBLE);

            //SET ANIMATION
            button_new.startAnimation(rotateClose);
            button_newobra.startAnimation(toBottom);
            button_newvisit.startAnimation(toBottom);
            tno.startAnimation(toBottom);
            tnv.startAnimation(toBottom);

            //CLICKABLE
            button_newobra.setClickable(false);
            button_newvisit.setClickable(false);

            clicked = false;
        }
    }

    private void disableFrameLayer() {
        layer_transparent.setOnTouchListener(new FrameLayout.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addnew_click(v);
                return false;
            }

        });
    }
}
