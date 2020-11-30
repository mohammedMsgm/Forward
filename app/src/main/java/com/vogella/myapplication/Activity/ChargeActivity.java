package com.vogella.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vogella.myapplication.PayChargeActivity;
import com.vogella.myapplication.R;
import com.vogella.myapplication.databinding.ActivityChargeBinding;

public class ChargeActivity extends AppCompatActivity {
    ActivityChargeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context =this;
        binding = ActivityChargeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startActivity(new Intent(context, PayChargeActivity.class).putExtra("type", "bodyBuilding"));

    }
}