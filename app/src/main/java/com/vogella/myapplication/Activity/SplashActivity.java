package com.vogella.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.vogella.myapplication.MainActivity;
import com.vogella.myapplication.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2500)
                .withBackgroundResource(R.color.colorAccent)
                .withAfterLogoText(" " +
                        "Street Workout Gym")
                .withLogo(R.drawable.ic_lion);
        config.getAfterLogoTextView().setTextColor(Color.parseColor("#434343"));
        config.getAfterLogoTextView().setPaddingRelative(15,15,15,15);
        config.getAfterLogoTextView().setTextSize(30);


        //config.getFooterTextView().setCompoundDrawablePadding(15);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}

