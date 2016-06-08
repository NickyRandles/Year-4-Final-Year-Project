package com.project.nicky.medicalmanager.Startup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.project.nicky.medicalmanager.Home.HomeActivity;
import com.project.nicky.medicalmanager.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        UserInformation userInformation = new UserInformation(this);
        Log.d("testing", Boolean.toString(userInformation.loggedIn()));
        Log.d("testing", userInformation.getUsername());
        if(userInformation.loggedIn()){
            finish();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void loginClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void registerClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
