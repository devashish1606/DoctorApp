package com.example.doctor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class PatientsDetailsActivity extends AppCompatActivity {
    CardView bookApp, prevApp, emerPrevApp, emerApp,prescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_details);
        bookApp=findViewById(R.id.bookAppointment);
        prevApp=findViewById(R.id.prevAppointment);
        emerPrevApp=findViewById(R.id.emerPrevApp);
        emerApp=findViewById(R.id.emergencyApp);
        prescription = findViewById(R.id.prescription);




        bookApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookAppointment = "fromAppointment";
                Intent i= new Intent(PatientsDetailsActivity.this, BookAppointment.class);
                i.putExtra("appointment",bookAppointment);
                startActivity(i);
            }
        });
        prevApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prevAppointment = "fromAppointment";
                Intent i= new Intent(PatientsDetailsActivity.this, PreviousAppointment.class);
                i.putExtra("prevappointment",prevAppointment);
                startActivity(i);
            }
        });

        emerApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emerAppointment = "fromEmrgency";
                Intent i= new Intent(PatientsDetailsActivity.this, EmergencyAppointment.class);
                i.putExtra("appointment",emerAppointment);
                startActivity(i);
            }
        });

        emerPrevApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prescription = "fromEmrgency";
                Intent i= new Intent(PatientsDetailsActivity.this, PreviousAppointment.class);
                i.putExtra("prevappointment",prescription);
                startActivity(i);
            }
        });
        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prescription = "fromEmrgency";
                Intent i= new Intent(PatientsDetailsActivity.this, PrescriptionActivity.class);
                i.putExtra("prevappointment",prescription);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Exit..");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        alertDialogBuilder.setMessage("Are you sure you want to exit?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}