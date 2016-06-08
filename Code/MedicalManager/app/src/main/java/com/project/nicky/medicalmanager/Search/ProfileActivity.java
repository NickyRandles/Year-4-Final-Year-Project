package com.project.nicky.medicalmanager.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.nicky.medicalmanager.Home.HomeActivity;
import com.project.nicky.medicalmanager.Patient.AvailableAppointmentsActivity;
import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements BackgroundResponse {

    private TextView nameText, backgroundText, qualificationsText, experienceText, addressText;
    private String id;
    private BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        id = intent.getStringExtra("profileId");

        nameText = (TextView)findViewById(R.id.nameText);
        backgroundText = (TextView)findViewById(R.id.backgroundText);
        qualificationsText = (TextView)findViewById(R.id.qualificationsText);
        experienceText = (TextView)findViewById(R.id.experienceText);
        addressText = (TextView)findViewById(R.id.addressText);

        String type = "getProfile";
        backgroundTask = new BackgroundTask(this);
        backgroundTask.backgroundResponse = this;
        backgroundTask.execute(type, id);
    }

    public void contactButtonClicked(View view) {
        String type = "addContact";
        UserInformation userInformation = new UserInformation(this);
        String loggedInUsername = userInformation.getUsername();
        backgroundTask = new BackgroundTask(this);
        backgroundTask.backgroundResponse = this;
        backgroundTask.execute(type, id, loggedInUsername);
    }

    public void appointmentButtonClicked(View view) {
        Intent intent = new Intent(this, AvailableAppointmentsActivity.class);
        intent.putExtra("doctorId", id);
        startActivity(intent);
    }

    @Override
    public void processResponse(String result) {
        if(result.equals("Contact added")){
            Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
        else if(result != null){
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    String firstName = listItem.getString("first_name");
                    String lastName = listItem.getString("last_name");
                    String background = listItem.getString("background");
                    String qualifications = listItem.getString("qualifications");
                    String experience = listItem.getString("experience");
                    String addressLine = listItem.getString("address_line_1_2");
                    String city = listItem.getString("city");
                    String county = listItem.getString("county");
                    String country = listItem.getString("country");
                    String fullAddress  = addressLine + ", " + city + ", " + county + ", " + country;
                    nameText.append(firstName + " " + lastName);
                    backgroundText.append(background);
                    qualificationsText.append(qualifications);
                    experienceText.append(experience + " years");
                    addressText.append(fullAddress);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
