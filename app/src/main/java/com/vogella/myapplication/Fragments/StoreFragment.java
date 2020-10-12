package com.vogella.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.vogella.myapplication.Adapters.StorePageAdapter;
import com.vogella.myapplication.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private StorePageAdapter pagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_top_icon,
            R.drawable.ic_dumbell_icon,
            R.drawable.ic_porthion_icon,
            R.drawable.ic_trainer_icon};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.store_view_pager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MerchFragment());
        fragments.add(new EquipmentFragment());
        fragments.add(new SuplementFragment());
        fragments.add(new TrainerFragment());


        ArrayList<String> tabNames = new ArrayList<>();
        tabNames.add("Merch");
        tabNames.add("Tools");
        tabNames.add("Supplements");
        tabNames.add("Trainer");



        pagerAdapter = new StorePageAdapter(getChildFragmentManager(), fragments, tabNames, fragments.size());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

}
