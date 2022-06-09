package com.example.fuel.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fuel.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.BenzinButton.setOnClickListener(v->{
            Intent intent = new Intent(AdminActivity.this, Admin_BenzinActivity.class);
            startActivity(intent);

        });
        binding.DizelButton.setOnClickListener(v->{
            Intent intent = new Intent(AdminActivity.this, Admin_DizelActivity.class);
            startActivity(intent);
        });
        binding.LpgButton.setOnClickListener(v->{
            Intent intent = new Intent(AdminActivity.this, Admin_LpgActivity.class);
            startActivity(intent);
        });
    }
}