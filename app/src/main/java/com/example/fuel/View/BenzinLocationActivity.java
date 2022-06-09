package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuel.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BenzinLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    private FusedLocationProviderClient fusedLocationProviderClient;


    //nearby places settings
    private String[] places_array =new String[]{"aygaz", "bp", "energy", "go", "kadoil", "lukoil", "milangaz", "moil", "opet" ,"po", "shell", "total", "tp", "aytemiz"};
    private Double currentlat, currentlong, latitude, longitude;
    private int radius = 2000;
    private String url;
    private RequestQueue queue;

    private SharedPreferences sharedPreferences ;

    /*url system =>
    https://maps.googleapis.com/maps/api/place/nearbysearch/json
    ?location=40.78953612267259,30.405997005089173
    &radius=2000
    &types=benzin_istasyon
    &name=shell
    &key=AIzaSyDEEISUqJC-eJfNq_nvf1_P5n-qvjLh20E
     */

    LocationManager mLocationManager;

    int LOCATION_REFRESH_TIME = 1500; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentlat = location.getLatitude();
            currentlong = location.getLongitude();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            gMap.animateCamera(cameraUpdate);
            gMap.clear();
            for (int i = 0; i < places_array.length; i++) {
                getNearbyPlaces(i);
            }
        }
    };

    private void init(){
        sharedPreferences = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        radius = sharedPreferences.getInt("radius",2000);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        queue = Volley.newRequestQueue(this);

        checkPermissionLocation();
        //getLastLocation();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benzin_location);

        init();
    }

    private void getNearbyPlaces(int i) {


            url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"+
                    "?location="+currentlat+","+currentlong+
                    "&radius="+radius+
                    "&types=benzin_istasyon" +
                    "&name="+places_array[i]+
                    "&key=AIzaSyDEEISUqJC-eJfNq_nvf1_P5n-qvjLh20E";




            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject object = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        latitude = Double.valueOf(object.getString("lat"));
                        longitude = Double.valueOf(object.getString("lng"));

                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title(places_array[i]);
                        gMap.addMarker(markerOptions);

                        /*
                        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String position = marker.getTitle();

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String mahalle = addresses.get(0).getSubLocality();
                                String ilce = addresses.get(0).getSubAdminArea();
                                System.out.println("ilçe "+ilce);//ilçe
                                System.out.println(mahalle);



                                DocumentReference doc = FirebaseFirestore.getInstance()
                                        .collection("Sakarya")
                                        .document(ilce)
                                        .collection("Benzin")
                                        .document(places_array[i]);


                                Toast.makeText(BenzinLocationActivity.this, position, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                        */

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(BenzinLocationActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });



            queue.add(request);
            MarkerListener();
    }

    private void checkPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BenzinLocationActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(BenzinLocationActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(BenzinLocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(BenzinLocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(BenzinLocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(BenzinLocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            Toast.makeText(this, "Permission has Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(BenzinLocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        init();
                    }
                } else {
                    //permission denied
                }
                return;
            }
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setTrafficEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);


        checkPermissionLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           
            return;
        }
        gMap.setMyLocationEnabled(true);


    }
    public void MarkerListener(){

    }
}