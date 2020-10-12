package com.vogella.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;


import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.vogella.myapplication.Activity.SignInActivity;
import com.vogella.myapplication.Activity.SignUpActivity;
import com.vogella.myapplication.Fragments.CheckFragment;
import com.vogella.myapplication.Fragments.HomeFragment;
import com.vogella.myapplication.Fragments.ProfileFragment;
import com.vogella.myapplication.Fragments.QrFragment;
import com.vogella.myapplication.Fragments.StoreFragment;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
                loadFragment(new HomeFragment());
            } else {
                newString= extras.getString("loadedFragment");
                loadFragment(new CheckFragment());
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("loadedFragment");
        }
        Calendar instance = Calendar.getInstance();
        Date date = new Date();
        date.setTime(date.getTime() + (20 * 1000));
        instance.setTime(date);
        startAlarm(instance, 1);
        BottomNavigationView btm = findViewById(R.id.bottom_navigation_bar);

        btm.setItemIconTintList(null);
        btm.setSelectedItemId(R.id.home_icon);
        btm.setSelected(true);
        //loadFragment(new HomeFragment());
        btm.setSelectedItemId(R.id.home_icon);
        btm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.qr_icon: return loadFragment(new QrFragment());
                    case R.id.check_icon: return loadFragment(new CheckFragment());
                    case R.id.home_icon: return loadFragment(new HomeFragment());
                    case R.id.profile_icon: return loadFragment(new ProfileFragment());
                    case R.id.store_icon: return loadFragment(new StoreFragment());
                }
                return false;
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }
    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    public void openSignUp (View view) {
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
    }
    public void openSignIn(View v){
        startActivity(new Intent(this, SignInActivity.class));
    }
    public void openStore(View v){loadFragment(new StoreFragment());}

    void startAlarm(Calendar c, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}