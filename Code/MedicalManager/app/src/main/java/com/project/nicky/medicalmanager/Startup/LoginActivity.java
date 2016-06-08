package com.project.nicky.medicalmanager.Startup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.nicky.medicalmanager.R;

public class LoginActivity extends AppCompatActivity implements BackgroundResponse {

    private TextView usernameField, passwordField;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (TextView)findViewById(R.id.usernameField);
        passwordField = (TextView)findViewById(R.id.passwordField);
    }

    public void LoginButtonClicked(View view) {
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();

        String type = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.backgroundResponse = this;
        backgroundTask.execute(type, username, password);
    }

    @Override
    public void processResponse(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if(result.equals("Login successful")){
            UserInformation userInformation = new UserInformation(this);
            userInformation.save(username);
        }
    }
}
