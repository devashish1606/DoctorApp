package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class BookAppointment extends AppCompatActivity {

    // creating variables for
    // EditText and buttons.
    private EditText name, mobile, age, address,app_date,describeProblem,gender;
    private Button sendDatabtn,bookCall;
    private LinearLayout horizontalLine;
    private DatePicker datePicker;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_all_details);

        name=findViewById(R.id.personName);
        mobile = findViewById(R.id.pPhone);
        age = findViewById(R.id.page);
        address = findViewById(R.id.paddress);
        describeProblem=findViewById(R.id.describe);
        gender=findViewById(R.id.gender);

//        horizontalLine = findViewById(R.id.horizotalLine);
        app_date = findViewById(R.id.appointment_date);
        datePicker = findViewById(R.id.date_picker);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        sendDatabtn = findViewById(R.id.buttonSubmit);

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

                    Toast.makeText(BookAppointment.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {

//                        Appointment appointment =new Appointment(uid,nameString,mobileString,ageString,addressString,apointment_date,describeString);
//                        databaseReference = firebaseDatabase.getReference("Appointments");
//                        databaseReference
//                                .child(uid).setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                name.setText("");
//                                mobile.setText("");
//                                age.setText("");
//                                address.setText("");
//                                describeProblem.setText("");
//
//                                Toast.makeText(BookAppointment.this, "Appointment Booked Successful", Toast.LENGTH_SHORT).show();
//
//                                Intent i = new Intent(BookAppointment.this,PatientsDetailsActivity.class);
//                                startActivity(i);
//
//                            }
//                        });

                    addDataToFirestore(uid,nameString,mobileString,ageString,addressString,apointment_date,describeString,genderString);

                }
            }
        });
    }

    private void addDataToFirestore(String uid, String nameString, String mobileString, String ageString, String addressString, String apointment_date, String describeString, String genderString) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbCourses = firebaseFirestore.collection("Patients_Details").document(uid).collection("Appointments");

        // adding our data to our courses object class.
        Appointment courses = new Appointment(uid,nameString,mobileString,ageString,addressString,apointment_date,describeString,genderString);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(BookAppointment.this, "Appointment Booked Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(BookAppointment.this,PatientsDetailsActivity.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(BookAppointment.this, "Something went wrong!! \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}