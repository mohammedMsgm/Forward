package com.vogella.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.R;

import java.util.Locale;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        /*if(currentUser != null){
            FirebaseFirestore.getInstance().document("users/" + currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()){
                        String language = (String) task.getResult().get("language");
                        if(language == null){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent refresh = new Intent(SplashActivity.this, MainActivity.class);
                                    finish();
                                    startActivity(refresh);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }, 2500);
                        }else {
                            setLocale(language);
                        }
                    }
                }
            });
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent refresh = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                    startActivity(refresh);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2500);
        }*/

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()

                .withBackgroundResource(R.color.colorAccent)
                .withAfterLogoText(" " +
                        "Street Workout Gym")
                .withLogo(R.drawable.ic_lion).withTargetActivity(MainActivity.class);
        config.getAfterLogoTextView().setTextColor(Color.parseColor("#434343"));
        config.getAfterLogoTextView().setPaddingRelative(15,15,15,15);
        config.getAfterLogoTextView().setTextSize(30);


        //config.getFooterTextView().setCompoundDrawablePadding(15);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        //Toast.makeText(this, conf.locale.toLanguageTag(), Toast.LENGTH_SHORT).show();
        String s = conf.locale.getLanguage();
        boolean b = !(s.equals(lang));
        if(b){
            Intent refresh = new Intent(this, MainActivity.class);

            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(refresh);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2500);

        }else {
            Intent refresh = new Intent(this, MainActivity.class);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(refresh);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2500);
        }

    }
}

