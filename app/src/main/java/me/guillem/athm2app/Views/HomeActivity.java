package me.guillem.athm2app.Views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;

import io.supercharge.shimmerlayout.ShimmerLayout;
import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.FirebaseCRUD;
import me.guillem.athm2app.Utils.Utils;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private boolean clicked = false;

    private Animation fromBottom, rotateOpen, rotateClose, toBottom;
    private TextView tnv, tno, data_user;
    private RecyclerView rv;
    private ProgressBar mProgressBar;
    private FrameLayout layer_transparent;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton button_new, button_newvisit, button_newobra;

    private final Context c = HomeActivity.this;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    private FirebaseCRUD recurs_crud = new FirebaseCRUD();
    private RecyclerAdapterCardsHome adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animations
        fromBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_button_anim);
        rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_open_anim);
        toBottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.to_button_anim);
        rotateClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_close_button);

        //Floating button
        layer_transparent = findViewById(R.id.shadow_layer);
        disableFrameLayer();
        button_new = findViewById(R.id.button_add_new);
        button_newobra = findViewById(R.id.button_new_obra);
        button_newvisit = findViewById(R.id.button_new_visit);
        tno = findViewById(R.id.tvobra);
        tnv = findViewById(R.id.tvvisita);

        //Get username
        mAuth = FirebaseAuth.getInstance();
        data_user = findViewById(R.id.data_user);
        sayHello();


        mProgressBar = findViewById(R.id.mProgressBarLoad);
        mProgressBar.setIndeterminate(true);

        //RecyclerView
        rv = findViewById(R.id.recycler_cards);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), layoutManager.getOrientation());
        //rv.addItemDecoration(dividerItemDecoration);
        adapter=new RecyclerAdapterCardsHome(this,Utils.DataCache);
        rv.setAdapter(adapter);

        bindDades();

/*      Visita v1 = new Visita("12-12-12","13:20","Ramon","Restauracio de pis","La infrastructura bla bla", "-MLhWLAAAAArlOKFjNXj");
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

    //Say hello to Google Username depending de hour
    private void sayHello() {
        String username = "";
        if (mAuth != null) username = mAuth.getCurrentUser().getDisplayName();

        String msg = null;
        String bondia = "Bon dia " + username + " !";
        String bonatarda = "Bona tarda " + username + " !";
        String bonanit = "Bona nit " + username + " !";

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        System.out.println(c);
        System.out.println(timeOfDay);

        if (timeOfDay >= 5 && timeOfDay < 13) {
            msg = bondia;
        } else if (timeOfDay >= 13 && timeOfDay < 20) {
            msg = bonatarda;
        } else if ((timeOfDay >= 20 && timeOfDay < 24) || (timeOfDay >= 0 && timeOfDay < 5)) {
            msg = bonanit;
        }

        data_user.setText(msg);
    }

    private void bindDades(){
        recurs_crud.select(this,Utils.getDatabaseRefence(),mProgressBar,rv,adapter);
    }

    //Button to see MapView
    public void afegirDes(View view) {
        //Intent intent = new Intent(this, MapsActivity.class);
        Intent intent = new Intent(this, TestingDrive.class);
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
        addnew_click(getCurrentFocus());
        searchView.setQueryHint("Buscar");
        searchView.setLayoutTransition(new LayoutTransition());
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
            rv.setEnabled(false);

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
            rv.setEnabled(true);


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

    protected void onResume() {
        super.onResume();
        bindDades();
    }

}
