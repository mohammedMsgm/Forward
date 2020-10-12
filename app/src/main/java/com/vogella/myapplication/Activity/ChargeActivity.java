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
        binding.bodyBuildingchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PayChargeActivity.class).putExtra("type", "bodyBuilding"));
            }
        });
        binding.calisthenicsCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "ليس متوفرا بعد", Toast.LENGTH_SHORT).show();
            }
                //startActivity(new Intent(context, PayChargeActivity.class).putExtra("type", "cardio"));            }
        });
        binding.cardioCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "ليس متوفرا بعد", Toast.LENGTH_SHORT).show();

            }
             //   startActivity(new Intent(context, PayChargeActivity.class).putExtra("type", "calisthenics"));            }
        });
        binding.losingWeightCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "ليس متوفرا بعد", Toast.LENGTH_SHORT).show();

            }
              //  startActivity(new Intent(context, PayChargeActivity.class).putExtra("type", "losingWeight"));            }
        });
    }
}