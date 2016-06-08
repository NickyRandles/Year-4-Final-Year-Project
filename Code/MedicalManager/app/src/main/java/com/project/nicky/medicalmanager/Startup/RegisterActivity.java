package com.project.nicky.medicalmanager.Startup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.nicky.medicalmanager.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void patientButtonClicked(View view) {
        Intent intent = new Intent(this, PatientRegisterActivity.class);
        startActivity(intent);
    }

    public void doctorButtonClicked(View view) {
        Intent intent = new Intent(this, DoctorRegisterActivity.class);
        startActivity(intent);
    }
}
