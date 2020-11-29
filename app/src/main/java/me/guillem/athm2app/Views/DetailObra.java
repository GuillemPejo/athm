package me.guillem.athm2app.Views;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.PageTabsAdapter;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.Utils;

public class DetailObra extends AppCompatActivity {

    private Obra receivedObra;
    TextView titol, adresa;
    ViewPager viewPager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_obra);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });*/


        viewPager = findViewById(R.id.view_pager);

        tabs = findViewById(R.id.tabs);
        TabItem tabinfo = findViewById(R.id.info_tab);
        TabItem tabvisitas = findViewById(R.id.visitas_tab);
        TabItem tabdocs = findViewById(R.id.docs_tab);

        tabs.setupWithViewPager(viewPager);

        titol = findViewById(R.id.titol);
        adresa = findViewById(R.id.aaa);

        receiveAndShowData();


/*        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        InfoFragment infoFragment = new InfoFragment();

        Bundle newBundle = new Bundle();
        newBundle.putString("key", "hola");
        infoFragment.setArguments(newBundle);
        transaction.add(R.id.view_pager, infoFragment);
        transaction.commit();*/


/*
        Bundle bundle = new Bundle();
        bundle.putSerializable("obra", obra);
        infoFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_pager, infoFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
*/



        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void receiveAndShowData() {
        receivedObra = Utils.receiveObra(getIntent(), DetailObra.this);

        if (receivedObra != null) {
            getSupportActionBar().setTitle(receivedObra.getTitol());
            //titol.setText(receivedObra.getTitol());
            adresa.setText(receivedObra.getAdreça());
            adresa.setText(receivedObra.getKey());
        }

        PagerAdapter pagerAdapter = new PageTabsAdapter(getSupportFragmentManager(),
                tabs.getTabCount(), receivedObra);
        viewPager.setAdapter(pagerAdapter);
    }


}