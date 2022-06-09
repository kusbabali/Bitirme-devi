package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fuel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout benzinLocationLayout, cheapFuel, profile, offer;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logOut){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_option_menu, menu);
        return true;
    }

    public void init(){
        benzinLocationLayout = findViewById(R.id.BenzinLocationLayout);
        cheapFuel = findViewById(R.id.cheapFuel);
        profile = findViewById(R.id.profile_layout);
        offer = findViewById(R.id.offer_layout);

        mAuth=FirebaseAuth.getInstance();


        benzinLocationLayout.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, BenzinLocationActivity.class);
            startActivity(intent);
        });
        cheapFuel.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, CheapFuelActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        offer.setOnClickListener(v->{
            Toast.makeText(this, "no activity at the moment", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

             */
        });



        db.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                   String nameAndSurname = (String) task.getResult().get("name_surname");
                   setTitle("Hoşgeldin "+nameAndSurname);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //yerel uygulama hafızası
        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // b: => default
        if (sharedPreferences.getBoolean("firstStart",false)){
            editor.putInt("radius",2000);

            editor.putBoolean("firstStart",true);
            editor.commit();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();

        if (currentUser==null){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    public void logout(){
        mAuth.signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();

    }
}

