package me.guillem.athm2app.Views;

import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_obra);


        ViewPager viewPager = findViewById(R.id.view_pager);

        TabLayout tabs = findViewById(R.id.tabs);
        TabItem tabinfo = findViewById(R.id.info_tab);
        TabItem tabvisitas = findViewById(R.id.visitas_tab);
        TabItem tabdocs = findViewById(R.id.docs_tab);

        tabs.setupWithViewPager(viewPager);

        Bundle objetoenviado = getIntent().getExtras();
        Obra obra = (Obra) objetoenviado.getSerializable("obra");

        PagerAdapter pagerAdapter = new PageTabsAdapter(getSupportFragmentManager(),
                tabs.getTabCount(), obra);
        viewPager.setAdapter(pagerAdapter);

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
            titol.setText(receivedObra.getTitol());
            adresa.setText(receivedObra.getAdreça());

        }
    }

}