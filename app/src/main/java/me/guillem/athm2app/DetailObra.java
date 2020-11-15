package me.guillem.athm2app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import me.guillem.athm2app.ui.InfoFragment;

public class DetailObra extends AppCompatActivity {

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



        TextView t1 = findViewById(R.id.titol);
        TextView t2 = findViewById(R.id.aaa);

        t1.setText(obra.getTitol());
        t2.setText(obra.getAdreça());


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
}