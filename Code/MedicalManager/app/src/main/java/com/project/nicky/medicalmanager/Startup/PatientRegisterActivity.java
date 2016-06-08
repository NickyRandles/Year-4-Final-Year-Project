package com.project.nicky.medicalmanager.Startup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.nicky.medicalmanager.R;

public class PatientRegisterActivity extends AppCompatActivity implements BackgroundResponse {

    private EditText firstNameField, lastNameField, usernameField, passwordField, addressLineField, cityField, countyField;
    private Spinner countrySpinner;
    private ArrayAdapter arrayAdapter;
    private String firstName, lastName, username, password, addressLine, city, county, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        firstNameField = (EditText)findViewById(R.id.firstNameField);
        lastNameField = (EditText)findViewById(R.id.lastNameField);
        usernameField = (EditText)findViewById(R.id.usernameField);
        passwordField = (EditText)findViewById(R.id.passwordField);
        addressLineField = (EditText)findViewById(R.id.addressLineField);
        cityField = (EditText)findViewById(R.id.cityField);
        countyField = (EditText)findViewById(R.id.countyField);

        countrySpinner = (Spinner)findViewById(R.id.countrySpinner);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.counties, android.R.layout.simple_dropdown_item_1line);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(arrayAdapter);

    }

    public void registerButtonClicked(View view) {
        firstName = firstNameField.getText().toString();
        lastName = lastNameField.getText().toString();
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        addressLine = addressLineField.getText().toString();
        city = cityField.getText().toString();
        county = countyField.getText().toString();
        country = countrySpinner.getSelectedItem().toString();

        Validation validation = new Validation();
        validation.validate(Validation.LETTERS, firstName, firstNameField, "First name cannot be empty", "First name must be letters only");
        validation.validate(Validation.LETTERS, lastName, lastNameField, "Last name cannot be empty", "Last name must be letters only");
        validation.validate(Validation.LETTERS_AND_NUMBERS, username, usernameField, "Username cannot be empty", "Username must be letters and numbers only");
        validation.length(username, usernameField, "Username must be between 6 and 15 characters", 6, 15);
        validation.checkIfEmpty(password, passwordField, "Password cannot be empty");
        validation.length(password, passwordField, "Password must be between 6 and 20 characters", 6, 20);
        validation.checkIfEmpty(addressLine, addressLineField, "Address line cannot be empty");
        validation.checkIfEmpty(city, cityField, "City cannot be empty");
        validation.checkIfEmpty(county, countyField, "County cannot be empty");

        if(validation.counter == 0){
            String type = "registerPatient";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.backgroundResponse = this;
            backgroundTask.execute(type, firstName, lastName, username, password, addressLine, city, county, country);
        }
    }

    @Override
    public void processResponse(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if(result.equals("Registration successful")){
            UserInformation userInformation = new UserInformation(this);
            userInformation.save(username);
        }
    }
}
