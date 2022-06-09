package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuel.R;
import com.example.fuel.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    //UI
    private EditText email,password,nameSurname, password2;
    private Button btnRegister;
    private TextView LoginText;

    //Variables
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email=findViewById(R.id.RegisterPageEmail);
        password=findViewById(R.id.RegisterPagePass);
        nameSurname=findViewById(R.id.RegisterPageName);
        btnRegister=findViewById(R.id.RegisterPageRegisterButton);
        LoginText = findViewById(R.id.RegisterPageLoginText);
        password2 = findViewById(R.id.RegisterPagePass2);

        btnRegister.setOnClickListener(view -> {

            Register();
        });
        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void Register() {
        String user = email.getText().toString();
        String pass = password.getText().toString();
        String name = nameSurname.getText().toString();

        if (name.equals("")){
            Toast.makeText(this, "Lütfen bir İsim giriniz", Toast.LENGTH_SHORT).show();
        }else if(user.equals("")){
            Toast.makeText(this, "Lütfen bir eposta adresi giriniz", Toast.LENGTH_SHORT).show();
        }else if (pass.equals("")){
            Toast.makeText(this, "Lütfen bir Şifre giriniz", Toast.LENGTH_SHORT).show();
        }else{
            FireBaseSignIn(user, pass, name);
        }
    }

    private void FireBaseSignIn(String user, String pass, String name) {

        mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userUID = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name_surname", name);


                    db.collection("Users").document(userUID).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



}