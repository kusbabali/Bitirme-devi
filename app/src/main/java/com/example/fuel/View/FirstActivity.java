package com.example.fuel.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fuel.databinding.ActivityFirstBinding;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstActivity extends AppCompatActivity {

    ActivityFirstBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.adminButton.setOnClickListener(v->{
            Intent intent = new Intent(FirstActivity.this, AdminActivity.class);
            startActivity(intent);

        });
        binding.userButton.setOnClickListener(v->{
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}