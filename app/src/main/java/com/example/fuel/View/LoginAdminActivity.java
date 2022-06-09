package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fuel.databinding.ActivityLoginAdminBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class LoginAdminActivity extends AppCompatActivity {

    ActivityLoginAdminBinding binding;

    FirebaseFirestore db;

    String userName, password;

    public void init(){
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginAdminBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();

        binding.AdminLoginPageButton.setOnClickListener(v->{
            CheckLoginRequest();
        });


    }

    private void CheckLoginRequest() {

        userName = binding.userNameAdminLogin.getText().toString();
        password = binding.passwordAdminLogin.getText().toString();

        if (userName.equals("")){
            Toast.makeText(this, "Lütfen bir kullanıcı adı giriniz", Toast.LENGTH_SHORT).show();
        }else if(password.equals("")){
            Toast.makeText(this, "Lütfen bir Şifre giriniz.", Toast.LENGTH_SHORT).show();
        }else{
            db.collection("FuelStation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){
                        String userNameLowerCase = userName.toLowerCase(Locale.ROOT);

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            String documentRef = documentSnapshot.getReference().getId().toLowerCase(Locale.ROOT);

                            //Document Control
                            if (documentRef.equals(userNameLowerCase)){

                                if (documentSnapshot.get("username").equals(userName) &&
                                        documentSnapshot.get("password").equals(password)){
                                    Toast.makeText(LoginAdminActivity.this, "Hoşgeldiniz", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginAdminActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginAdminActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            });
        }
    }
}