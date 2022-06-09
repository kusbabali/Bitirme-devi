package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import com.example.fuel.databinding.ActivityCheapFuelBinding;
import com.example.fuel.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private void init(){
        sharedPreferences = this.getSharedPreferences(this.getPackageName() ,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        db.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String name_surname = task.getResult().getString("name_surname");
                            binding.nameAndSurname.setText(name_surname);
                            String radius = String.valueOf(sharedPreferences.getInt("radius",2000));
                            binding.radius.setText(radius);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();


        binding.buttonUpdate.setOnClickListener(v->{
            String name_surname = binding.nameAndSurname.getText().toString();
            int radius = Integer.parseInt(binding.radius.getText().toString());

            Map<String , Object> map = new HashMap<>();
            map.put("name_surname", name_surname);
            db.collection("Users")
                    .document(mAuth.getCurrentUser().getUid())
                    .update(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                editor.putInt("radius",radius);
                                editor.commit();
                                Toast.makeText(ProfileActivity.this, "Succesfully update", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}