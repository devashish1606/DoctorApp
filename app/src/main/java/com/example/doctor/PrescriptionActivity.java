package com.example.doctor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PrescriptionActivity extends AppCompatActivity {

    Button button, adddNewPres;
    ImageView imageView;
    CardView cardView2;
    public Uri imageUri;
    LinearLayout linearLayoutPriscription;
    RecyclerView gridRecyclerView;
    EditText suggestedBy;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    String imageUrl;

    PreviousPriscriptionAdapter previousPriscriptionAdapter;
    ArrayList<PreviousPrescriptionModel> prescriptionModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        button = findViewById(R.id.addPrescription);
        adddNewPres = findViewById(R.id.addNewPres);
        cardView2 = findViewById(R.id.cardView2);
        imageView = findViewById(R.id.prescriptionImage);
        linearLayoutPriscription = findViewById(R.id.linearLayoutPrescription);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        suggestedBy = findViewById(R.id.prescribedBy);
        gridRecyclerView = findViewById(R.id.gridRecycler);
        gridRecyclerView.setHasFixedSize(true);

        prescriptionModelArrayList = new ArrayList<>();
        gridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        gridRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        previousPriscriptionAdapter = new PreviousPriscriptionAdapter(this, prescriptionModelArrayList);
        gridRecyclerView.setAdapter(previousPriscriptionAdapter);
//        EventChangeListener();
        getListItems();

        adddNewPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView2.setVisibility(View.VISIBLE);
                adddNewPres.setVisibility(View.GONE);
            }
        });

        linearLayoutPriscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (suggestedBy.getText().toString().isEmpty()) {
                    Toast.makeText(PrescriptionActivity.this, "Please fill your Doctor Suggestion", Toast.LENGTH_SHORT).show();

                } else {
                    uploadPicture();
                    cardView2.setVisibility(View.GONE);
                    adddNewPres.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private void uploadPicture() {
        String desc = suggestedBy.getText().toString().trim();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference presRef = storageReference.child(uid + "image/" + randomKey);
        presRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PrescriptionActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
            }
        }).
//        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                presRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        imageUrl = uri;
//
//                        Log.d("url", String.valueOf(imageUrl));
//
//                    }
//                });
//                Map<String, Object> postMap = new HashMap<>();
//                postMap.put("image_url", imageUrl);
//                postMap.put("suggested_by", desc);
//                postMap.put("user_id", uid);
//
//                postMap.put("timestamp", FieldValue.serverTimestamp());
//
//
//                firebaseFirestore.collection("Patients_Details").document(uid).collection("Prescriptions").add(postMap)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//
//                            }
//                        });
//
//            }
//        });

//
        addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                presRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                    @Override
////                    public void onSuccess(Uri uri) {
////                        imageUrl = uri;
////
////                        Log.d("url", String.valueOf(imageUrl));
////
////                    }
////                });
                presRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        imageUrl = String.valueOf(task.getResult());

                    }
                });

                String desc = suggestedBy.getText().toString().trim();
//                String downloadUri = String.valueOf(task.getResult());


                Map<String, Object> postMap = new HashMap<>();
                postMap.put("image_url", imageUrl);
                postMap.put("suggested_by", desc);
                postMap.put("user_id", uid);

                postMap.put("timestamp", FieldValue.serverTimestamp());


                firebaseFirestore.collection("Patients_Details").document(uid).collection("Prescriptions").add(postMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                            }
                        });
//            (postMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//
//                    }
//                });


            }
        });

    }

    private void EventChangeListener() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Prescriptions...");
        progressDialog.show();

        firebaseFirestore.collection("Patients_Details").document(uid).collection("Prescriptions").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                PreviousPrescriptionModel prescriptionModelList = (documentChange.getDocument().toObject(PreviousPrescriptionModel.class));
                                prescriptionModelArrayList.add(prescriptionModelList);
                            }
                        }
                        previousPriscriptionAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                    }
                });


    }

    private void getListItems() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Prescriptions...");
        progressDialog.show();


        firebaseFirestore.collection("Patients_Details").document(uid).collection("Prescriptions").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<PreviousPrescriptionModel> types = documentSnapshots.toObjects(PreviousPrescriptionModel.class);

                            // Add all to your list
                            prescriptionModelArrayList.addAll(types);
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            previousPriscriptionAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onSuccess: " + prescriptionModelArrayList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}