package com.example.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileno;
    private Button getOtpBtn;
    private ProgressBar progressBar;
    private TextView skip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobileno = findViewById(R.id.mobileno);
        getOtpBtn = findViewById(R.id.getOtpButton);
        progressBar = findViewById(R.id.progressbar);
        skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this,PatientsDetailsActivity.class);
                startActivity(intent);
            }
        });


        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mobileno.getText().toString().trim().isEmpty()) {
                    if (mobileno.getText().toString().trim().length() == 10) {
                        progressBar.setVisibility(View.VISIBLE);
                        getOtpBtn.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobileno.getText().toString(), 60, TimeUnit.SECONDS,
                                LoginActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        getOtpBtn.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        getOtpBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        getOtpBtn.setVisibility(View.VISIBLE);

                                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                                        intent.putExtra("mobile", mobileno.getText().toString());
                                        intent.putExtra("backendotp", backendotp);
                                        startActivity(intent);
                                    }
                                });


                    } else {
                        Toast.makeText(LoginActivity.this, "Please Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new SharedPreferencesUtils(this).getLoginFlag()){
            startActivity(new Intent(LoginActivity.this,PatientsDetailsActivity.class));
        }
    }
}