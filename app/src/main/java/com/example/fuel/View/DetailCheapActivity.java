package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fuel.R;
import com.example.fuel.databinding.ActivityAdminBenzinBinding;
import com.example.fuel.databinding.ActivityDetailCheapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class DetailCheapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityDetailCheapBinding binding;

    //Location settings
    private GoogleMap map;
    private String adress;
    private GeoPoint geoPoint;

    private DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = ActivityDetailCheapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");//tipi ex: benzin
        adress = intent.getStringExtra("adress");//mahalle ex: istiklal
        String docName = intent.getStringExtra("docName");//marka ex: aygaz
        String district = intent.getStringExtra("district");//ilçe ex: serdivan

        ref = FirebaseFirestore.getInstance()
                .collection("Sakarya")
                .document(district)
                .collection(type)
                .document(docName);

        binding.type.setText(type);
        binding.adres.setText(adress);
        binding.district.setText(district);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        getLocationInfo();

    }

    private void getLocationInfo() {
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String currentAdress = "L_" + adress;
                    geoPoint = (GeoPoint) task.getResult().get(currentAdress);

                    setMapOptions();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailCheapActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMapOptions() {
        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng).title("İstasyon burada"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

    }
}