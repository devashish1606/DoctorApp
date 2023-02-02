package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PreviousAppointment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textViewRecycle;
//    DatabaseReference mdatabase;
    PrevAppAdapter prevAppAdapter;
    ArrayList<person> personArrayList;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_previous);
//        mdatabase = FirebaseDatabase.getInstance().getReference("Appointments");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        
        recyclerView = findViewById(R.id.preAppRecycler);
        textViewRecycle = findViewById(R.id.recycleText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        Intent intent = getIntent();
        String dataTransmited=intent.getStringExtra("prevappointment");
        String fromAppointment = "fromAppointment";
        String fromEmergency = "fromEmrgency";


        firebaseFirestore = FirebaseFirestore.getInstance();

        personArrayList = new ArrayList<>();
        prevAppAdapter = new PrevAppAdapter(this,personArrayList);
        recyclerView.setAdapter(prevAppAdapter);

        if (dataTransmited.equals(fromAppointment) ){
            EventChangeListener();
        }
        if (dataTransmited.equals(fromEmergency)){
            EventEmergencyAppointment();
        }
        


//        mdatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    person p = dataSnapshot.getValue(person.class);
//                    personArrayList.add(p);
//                }
//                prevAppAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void EventEmergencyAppointment() {
        textViewRecycle.setText("My Emergency Appointment");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore.collection("Patients_Details").document(uid).collection("Emergency_Appointments").orderBy("name", Query.Direction.ASCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED);
                            personArrayList.add(documentChange.getDocument().toObject(person.class));
                        }
                        prevAppAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void EventChangeListener() {
        textViewRecycle.setText("My Appointments");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore.collection("Patients_Details").document(uid).collection("Appointments").orderBy("name", Query.Direction.ASCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.e("Firestore Error",error.getMessage());
                    return;
                }

                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED);
                    personArrayList.add(documentChange.getDocument().toObject(person.class));
                }
                prevAppAdapter.notifyDataSetChanged();
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }
}