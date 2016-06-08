package com.project.nicky.medicalmanager.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.nicky.medicalmanager.Home.HomeActivity;
import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AvailableAppointmentsActivity extends AppCompatActivity implements BackgroundResponse {

    private CalendarView calendarView;
    private TextView dateText;
    private ListView timeList;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Integer> idList = new ArrayList<Integer>();
    private String doctorId = "";
    private BackgroundTask backgroundTask;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_appointments);

        Intent intent = getIntent();
        doctorId = intent.getStringExtra("doctorId");

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        dateText = (TextView)findViewById(R.id.dateText);
        timeList = (ListView)findViewById(R.id.timeList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);
        timeList.setAdapter(arrayAdapter);

        backgroundTask = new BackgroundTask(AvailableAppointmentsActivity.this);
        backgroundTask.backgroundResponse = AvailableAppointmentsActivity.this;
        String type = "getAppointments";
        calendar = Calendar.getInstance();
        dateText.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));
        String date = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        backgroundTask.execute(type, date, doctorId);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                dateText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                backgroundTask = new BackgroundTask(AvailableAppointmentsActivity.this);
                backgroundTask.backgroundResponse = AvailableAppointmentsActivity.this;
                String type = "getAppointments";
                String date = year + "-" + (month+1) + "-" + dayOfMonth;
                backgroundTask.execute(type, date, doctorId);
            }
        });

        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = "bookAppointment";
                backgroundTask = new BackgroundTask(AvailableAppointmentsActivity.this);
                backgroundTask.backgroundResponse = AvailableAppointmentsActivity.this;
                String appointmentId = idList.get(position).toString();
                UserInformation userInformation = new UserInformation(getApplicationContext());
                String userId = userInformation.getId().toString();
                backgroundTask.execute(type, appointmentId, userId);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void processResponse(String result) {
        arrayAdapter.clear();
        idList.clear();
        if(result != null){
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    String name = listItem.getString("time");
                    int id = listItem.getInt("id");
                    arrayAdapter.add(name);
                    idList.add(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
