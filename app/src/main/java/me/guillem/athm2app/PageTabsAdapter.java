package me.guillem.athm2app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.ui.DocsFragment;
import me.guillem.athm2app.ui.InfoFragment;
import me.guillem.athm2app.ui.VisitsFragment;

public class PageTabsAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    Obra obra;

    public  PageTabsAdapter(FragmentManager fm, int numOfTabs, Obra obra){
        super(fm);
        this.numOfTabs = numOfTabs;
        this.obra = obra;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;

        Bundle bundle = new Bundle();
        bundle.putSerializable("key", obra);

        switch (position){
            case 0:
                fragment=new InfoFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment=new VisitsFragment();
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment=new DocsFragment();
                break;
            default:
                fragment=null;
                break;
        }
        return  fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        String section = null;

        switch (position) {
            case 0:
                section = "INFO";
                break;
            case 1:
                section = "VISITES";
                break;
            case 2:
                section = "DOCS";
                break;
        }
        return section;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
