package com.vogella.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.vogella.myapplication.Fragments.MonthOrYearFragment;

public class PayChargeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_charge);
        loadFragment(new MonthOrYearFragment());
    }
    public boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.containerPayCharge, fragment).addToBackStack("tag")
                    .commit();
            return true;
        }
        return false;
    }
}
