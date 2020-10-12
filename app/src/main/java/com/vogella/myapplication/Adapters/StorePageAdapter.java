package com.vogella.myapplication.Adapters;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class StorePageAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> tabNames;
        private int size;


        public StorePageAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> tabNames, int size) {
            super(fm);
            this.fragments = fragments;
            this.tabNames = tabNames;
            this.size = size;
        }

        @Override
        public Fragment getItem(int i) {
            if (fragments != null) {
                return fragments.get(i);
            }
            return null;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (tabNames != null) {
                return tabNames.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return size;
        }
}

