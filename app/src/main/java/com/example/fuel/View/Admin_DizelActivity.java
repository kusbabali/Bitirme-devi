package com.example.fuel.View;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Toast;

        import com.example.fuel.R;

        import com.example.fuel.databinding.ActivityAdminBenzinBinding;
        import com.example.fuel.databinding.ActivityAdminDizelBinding;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.HashMap;

public class Admin_DizelActivity extends AppCompatActivity {

    @NonNull ActivityAdminDizelBinding binding;
    FirebaseFirestore db;

    //firebase documentBenzin
    DocumentReference AdapazarıRef  = FirebaseFirestore.getInstance()
            .collection("Sakarya")
            .document("Adapazarı")
            .collection("Dizel")
            .document("Shell");

    DocumentReference SerdivanRef  = FirebaseFirestore.getInstance()
            .collection("Sakarya")
            .document("Serdivan")
            .collection("Dizel")
            .document("Shell");

    DocumentReference ErenlerRef  = FirebaseFirestore.getInstance()
            .collection("Sakarya")
            .document("Erenler")
            .collection("Dizel")
            .document("Shell");

    ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        binding = ActivityAdminDizelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getFirebaseData();

        binding.DizelUpdateButton.setOnClickListener(v ->{
            UpdateFirebase();
        });

    }

    private void UpdateFirebase() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("updating content's...");
        dialog.show();
        HashMap<String, Object> AdapazarihashMap = new HashMap<>();
        AdapazarihashMap.put("AdnanMenderes",binding.dizelAdapazariAdnanMenderes.getText().toString());
        AdapazarihashMap.put("Gunesler",binding.dizelAdapazariGunesler.getText().toString());
        AdapazarihashMap.put("Karasuyolu",binding.dizelAdapazariKarasuyolu.getText().toString());
        AdapazarihashMap.put("Sabahattınzaim",binding.dizelAdapazariSabattinzaim.getText().toString());
        AdapazarıRef.update(AdapazarihashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    HashMap<String, Object> SerdivanhashMap = new HashMap<>();
                    SerdivanhashMap.put("Arabacıalanı",binding.dizelSerdivanArabacialani.getText().toString());
                    SerdivanhashMap.put("Bahçelievler", binding.dizelSerdivanBahcelievler.getText().toString());
                    SerdivanhashMap.put("Bahçelievler2",binding.dizelSerdivanBahcelievler2.getText().toString());
                    SerdivanhashMap.put("Esentepe",binding.dizelSerdivanEsentepe.getText().toString());

                    SerdivanRef.update(SerdivanhashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                HashMap<String, Object> ErenlerhashMap = new HashMap<>();
                                ErenlerhashMap.put("Yanyol",binding.dizelErenlerYanyol.getText().toString());
                                ErenlerRef.update(ErenlerhashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            if (dialog.isShowing()){
                                                dialog.dismiss();

                                            }
                                            Toast.makeText(Admin_DizelActivity.this, "Success Updates", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Admin_DizelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Admin_DizelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin_DizelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });








    }

    private void getFirebaseData() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AdapazarıRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String adnanMenderes = task.getResult().getString("AdnanMenderes");
                    String gunesler = task.getResult().getString("Gunesler");
                    String karasuyolu = task.getResult().getString("Karasuyolu");
                    String sabahattınzaim = task.getResult().getString("Sabahattınzaim");
                    binding.dizelAdapazariAdnanMenderes.setText(adnanMenderes);
                    binding.dizelAdapazariGunesler.setText(gunesler);
                    binding.dizelAdapazariKarasuyolu.setText(karasuyolu);
                    binding.dizelAdapazariSabattinzaim.setText(sabahattınzaim);

                    SerdivanRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()){
                                String arabacıalanı = task.getResult().getString("Arabacıalanı");
                                String bahçelievler = task.getResult().getString("Bahçelievler");
                                String bahçelievler2 = task.getResult().getString("Bahçelievler2");
                                String esentepe = task.getResult().getString("Esentepe");
                                binding.dizelSerdivanArabacialani.setText(arabacıalanı);
                                binding.dizelSerdivanBahcelievler.setText(bahçelievler);
                                binding.dizelSerdivanBahcelievler2.setText(bahçelievler2);
                                binding.dizelSerdivanEsentepe.setText(esentepe);

                                ErenlerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            String Yanyol = task.getResult().getString("Yanyol");
                                            binding.dizelErenlerYanyol.setText(Yanyol);

                                            dialog.dismiss();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Admin_DizelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Admin_DizelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin_DizelActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}


