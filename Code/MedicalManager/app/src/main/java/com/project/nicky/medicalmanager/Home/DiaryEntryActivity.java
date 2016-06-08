package com.project.nicky.medicalmanager.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DiaryEntryActivity extends AppCompatActivity implements BackgroundResponse {

    private int day, month, year;
    private TextView dateText;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);

        Intent intent = getIntent();
        day = intent.getIntExtra("day", 1);
        month = intent.getIntExtra("month", 1);
        year = intent.getIntExtra("year", 2016);
        dateText = (TextView)findViewById(R.id.dateText);
        dateText.setText(day + " / " + month + " / " + year);

        listView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(arrayAdapter);

        backgroundTask = new BackgroundTask(this);
        backgroundTask.backgroundResponse = this;
        UserInformation userInformation = new UserInformation(getApplicationContext());
        String username = userInformation.getUsername();
        String userType = userInformation.getUserType();
        String type = "getEvents";
        String date = year + "-" + month + "-" + day;
        backgroundTask.execute(type, username, userType, date);
    }

    @Override
    public void processResponse(String result) {
        arrayAdapter.clear();
        if(result != null){
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    String name = listItem.getString("name");
                    String time = listItem.getString("time");
                    arrayAdapter.add(name + " - " + time);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
