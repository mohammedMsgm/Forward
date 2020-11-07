package com.vogella.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            return inflater.inflate(R.layout.pleas_sign_in_or_up, container, false);
        }else {
            return inflater.inflate(R.layout.fragment_store, container, false);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
        CounterFab floatButton = view.findViewById(R.id.counter_fab);
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

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new BasketFragment());
            }
        });
            FirebaseFirestore.getInstance().collection("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/events")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            if (!queryDocumentSnapshots.isEmpty() || e != null) {
                                int count = 0;
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    count++;
                                }
                                floatButton.setCount(count);
                            } else {
                                floatButton.setCount(0);
                            }
                        }
                    });

    }}

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }
    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment).addToBackStack( "tag" )
                    .commit();
            return true;
        }
        return false;
    }

}
