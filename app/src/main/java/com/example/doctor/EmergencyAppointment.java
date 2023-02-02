package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmergencyAppointment extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private EditText name, mobile, age, address,app_date,describeProblem,gender;
    private Button sendDatabtn,bookCall;
    private  DatePicker datePicker;
    String mobNo ="9079298775";

    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_appointment);
        name=findViewById(R.id.personName);
        mobile = findViewById(R.id.pPhone);
        age = findViewById(R.id.page);
        address = findViewById(R.id.paddress);
        describeProblem=findViewById(R.id.describe);
        gender=findViewById(R.id.gender);
        bookCall=findViewById(R.id.bookCall);
        app_date = findViewById(R.id.appointment_date);
        datePicker = findViewById(R.id.date_picker);
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        bookCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });


        app_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    String appointment_date = (day+ "-" + month + "-" + year);
                    app_date.setText(appointment_date);
                    datePicker.setVisibility(View.GONE);

                }
            });
        }

        firebaseDatabase = FirebaseDatabase.getInstance();

        sendDatabtn = findViewById(R.id.buttonSubmit);
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String nameString = name.getText().toString();
                String mobileString = mobile.getText().toString();
                String ageString = age.getText().toString();
                String addressString = address.getText().toString();
                String describeString = describeProblem.getText().toString();
                String genderString = gender.getText().toString();
                String apointment_date = app_date.getText().toString();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



                if (TextUtils.isEmpty(nameString) && TextUtils.isEmpty(mobileString) && TextUtils.isEmpty(addressString) && TextUtils.isEmpty(ageString)) {

                    Toast.makeText(EmergencyAppointment.this, "Please Add All Data.", Toast.LENGTH_SHORT).show();
                } else {

//                    Appointment appointment =new Appointment(uid,nameString,mobileString,ageString,addressString,apointment_date,describeString);
//                    databaseReference = firebaseDatabase.getReference("Emergency Appointments");
//                    databaseReference
//                            .child(uid).setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            name.setText("");
//                            mobile.setText("");
//                            age.setText("");
//                            address.setText("");
//                            describeProblem.setText("");
//
//                            Toast.makeText(EmergencyAppointment.this, "Emergency Appointment Booked Successful", Toast.LENGTH_SHORT).show();
//
//                            Intent i = new Intent(EmergencyAppointment.this,PatientsDetailsActivity.class);
//                            startActivity(i);
//
//                        }
//                    });
                    addDataToFirestore(uid,nameString,mobileString,ageString,addressString,apointment_date,describeString,genderString);
                }
            }
        });

    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(EmergencyAppointment.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(EmergencyAppointment.this,new String[]
                    {Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else{
            String dial = "tel:" + mobNo;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void addDataToFirestore(String uid, String nameString, String mobileString,String ageString,String addressString,String apointment_date, String describeString,  String genderString) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbEmergencyApp = firebaseFirestore.collection("Patients_Details").document(uid).collection("Emergency_Appointments");

        // adding our data to our courses object class.
        Appointment emergencyapp = new Appointment(uid,nameString,mobileString,ageString,addressString,apointment_date,describeString,genderString);

        // below method is use to add data to Firebase Firestore.
        dbEmergencyApp.add(emergencyapp).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(EmergencyAppointment.this, "Emergency Appointment Booked Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EmergencyAppointment.this,PatientsDetailsActivity.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(EmergencyAppointment.this, "Something went wrong!! \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}