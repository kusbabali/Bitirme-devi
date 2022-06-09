package com.example.fuel.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fuel.Adapter.StationAdapter;
import com.example.fuel.Model.StationModel;
import com.example.fuel.R;
import com.example.fuel.databinding.ActivityCheapFuelBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CheapFuelActivity extends AppCompatActivity implements StationAdapter.StationClickListener {

    private ActivityCheapFuelBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String currentType = "Benzin", currentAddress = "Serdivan", currentBrand;
    private boolean firstTime = false;

    CollectionReference colRef = FirebaseFirestore.getInstance()
            .collection("Sakarya")
            .document(currentAddress)
            .collection(currentType);

    DocumentReference docRef;

    List<String> categoriesBrand;


    //RecyclerView Settings
    private ArrayList<StationModel> modelArrayList;
    private StationAdapter adapter;

    private void init(){
        modelArrayList = new ArrayList<>();


        //spinner1
        List<String> categories = new ArrayList<>();
        categories.add("Serdivan");
        categories.add("Erenler");
        categories.add("Adapazarı");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner1.setAdapter(adapter);
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentAddress = adapterView.getSelectedItem().toString();

                colRef = FirebaseFirestore.getInstance()
                        .collection("Sakarya")
                        .document(currentAddress)
                        .collection(currentType);

                modelArrayList.clear();


                //verinin 2 defa getirilmemesi ve her değiştirildiğinde(spinner) yeniden veri gelmesi
                if (firstTime == true){
                    categoriesBrand.clear();
                    categoriesBrand.add(0,"marka seçiniz");
                    addCategoriesBrand();
                    getFirebase();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner2
        List<String> categoriesGas = new ArrayList<>();
        categoriesGas.add("Benzin");
        categoriesGas.add("Lpg");
        categoriesGas.add("Dizel");

        ArrayAdapter<String> adapterGas;
        adapterGas = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesGas);

        adapterGas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner2.setAdapter(adapterGas);
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                currentType = adapterView.getSelectedItem().toString();
                colRef = FirebaseFirestore.getInstance()
                        .collection("Sakarya")
                        .document(currentAddress)
                        .collection(currentType);

                modelArrayList.clear();

                if (firstTime == true){

                    categoriesBrand.clear();
                    categoriesBrand.add(0,"marka seçiniz");
                    addCategoriesBrand();

                    getFirebase();
                }


                Toast.makeText(CheapFuelActivity.this, currentType + currentAddress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner3
        categoriesBrand = new ArrayList<>();
        categoriesBrand.add(0,"marka seçiniz");
        addCategoriesBrand();




    }

    private void addCategoriesBrand() {

            colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {


                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            String brand = task.getResult().getDocuments().get(i).getId();//document isimler veriliyor
                            categoriesBrand.add(brand);

                            ArrayAdapter<String> adapterBrand;
                            adapterBrand = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categoriesBrand );

                            adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            binding.spinner3.setAdapter(adapterBrand);
                            binding.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    if (!adapterView.getSelectedItem().toString().equals("marka seçiniz")){
                                        currentBrand = adapterView.getSelectedItem().toString();
                                        docRef = FirebaseFirestore.getInstance()
                                                .collection("Sakarya")
                                                .document(currentAddress)
                                                .collection(currentType)
                                                .document(currentBrand);

                                        modelArrayList.clear();

                                        if (firstTime == true){
                                            getFirebaseBrand();
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CheapFuelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }


    private void getFirebaseBrand() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    Collection<Object> objects = task.getResult().getData().values();
                    Object[] abc = task.getResult().getData().keySet().toArray();

                    for (int i = 0; i < abc.length; i++) {
                        if (!abc[i].toString().contains("L_")){
                            String mahalle = abc[i].toString();
                            String value = objects.toArray()[i].toString();


                            StationModel model = new StationModel(currentBrand, mahalle, value);
                            modelArrayList.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();


                }else{
                    Toast.makeText(CheapFuelActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheapFuelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();
        getFirebase();


        adapter = new StationAdapter(modelArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerView1.setLayoutManager(layoutManager);
        binding.recyclerView1.setAdapter(adapter);


    }


    private void getFirebase() {
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){


                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                           String brand = task.getResult().getDocuments().get(i).getId();//document isimler veriliyor


                            db.collection("Sakarya")
                                    .document(currentAddress)
                                    .collection(currentType)
                                    .document(brand)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                //price, brand, adress

                                                Set<String> adress = task.getResult().getData().keySet();

                                                Collection<Object> priceArray = task.getResult().getData().values();

                                                Object[] dizi2 = priceArray.toArray();//ücret dizi
                                                Object[] dizi = adress.toArray();//adress dizi


                                                //System.out.println(dizi2.length);
                                                for (int j = 0; j < dizi2.length; j++) {

                                                    String mahalleismi = dizi[j].toString();
                                                    String priceList = dizi2[j].toString();


                                                    if (!priceList.contains("GeoPoint")){

                                                        StationModel model = new StationModel(brand, mahalleismi, priceList);
                                                        modelArrayList.add(model);

                                                    }



                                                    //System.out.println("isim ->" +mahalleismi+ " price -> " + priceList);
                                                }
                                                adapter.notifyDataSetChanged();

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CheapFuelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CheapFuelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        firstTime = true;
    }


    @Override
    public void onStationClick(int position) {

        Intent intent = new Intent(getApplicationContext(), DetailCheapActivity.class);
        intent.putExtra("docName", modelArrayList.get(position).getBrand());
        intent.putExtra("adress", modelArrayList.get(position).getStationAdress());
        intent.putExtra("district",currentAddress);
        intent.putExtra("type",currentType);
        startActivity(intent);

    }
}