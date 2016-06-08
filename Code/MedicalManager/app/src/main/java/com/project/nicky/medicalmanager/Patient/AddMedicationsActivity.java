package com.project.nicky.medicalmanager.Patient;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.project.nicky.medicalmanager.Home.AppointmentService;
import com.project.nicky.medicalmanager.Home.HomeActivity;
import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;

import java.util.Calendar;

public class AddMedicationsActivity extends AppCompatActivity implements BackgroundResponse {

    private EditText medicationNameField;
    private Button startDateButton, timeButton;
    private Calendar calendar = Calendar.getInstance();
    private RadioGroup daysRadioGroup;
    private String name, startDate, time, duration;
    private int hours, minutes;
    private BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medications);

        medicationNameField = (EditText)findViewById(R.id.medicationNameField);
        startDateButton = (Button)findViewById(R.id.startDateButton);
        startDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        startDateButton.setText(startDate);
        timeButton = (Button)findViewById(R.id.timeButton);
        time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":0";
        timeButton.setText(time);
        duration = "100";

        daysRadioGroup = (RadioGroup) findViewById(R.id.durationRadioGroup);
        daysRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton numberRadioButton = (RadioButton) findViewById(R.id.numberRadioButton);
                if (checkedId == R.id.continuousRadioButton) {
                    duration = "100";
                    numberRadioButton.setText("Number of days: ");
                } else if (checkedId == R.id.numberRadioButton) {
                    final Dialog dialog = new Dialog(AddMedicationsActivity.this);
                    dialog.setTitle("Number of days");
                    dialog.setContentView(R.layout.dialog_number);
                    dialog.show();

                    final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(100);

                    Button okButton = (Button) dialog.findViewById(R.id.okButton);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            duration = Integer.toString(numberPicker.getValue());
                            numberRadioButton.setText("Specific days of the week: " + duration);
                            dialog.hide();
                        }
                    });
                }
            }
        });
    }

    public void dateButtonClicked(View view) {
        new DatePickerDialog(AddMedicationsActivity.this, listener, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            startDate = year + "-" + monthOfYear + "-" + dayOfMonth;
            startDateButton.setText(startDate);
        }
    };

    public void timeButtonClicked(View view) {
        final Dialog dialog = new Dialog(AddMedicationsActivity.this);
        dialog.setTitle("Set Time");
        dialog.setContentView(R.layout.dialog_time);
        dialog.show();

        final NumberPicker hourPicker = (NumberPicker) dialog.findViewById(R.id.hourPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        final NumberPicker minutesPicker = (NumberPicker) dialog.findViewById(R.id.minutesPicker);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);

        final Button dayOkButton = (Button) dialog.findViewById(R.id.okButton);
        dayOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = hourPicker.getValue();
                minutes = minutesPicker.getValue();
                if (minutesPicker.getValue() < 10) {
                    time = hours + ":0" + minutes + ":0";
                } else {
                    time = hours + ":" + minutes + ":0";
                }
                timeButton.setText(time);
                dialog.hide();
            }
        });
    }

    public void doneButtonClicked(View view) {
        name = medicationNameField.getText().toString();
        UserInformation userInformation = new UserInformation(getApplicationContext());
        String username = userInformation.getUsername();

        backgroundTask = new BackgroundTask(this);
        backgroundTask.backgroundResponse = this;
        String type = "addMedication";
        backgroundTask.execute(type, username, name, startDate, time, duration);

        Intent appointmentIntent = new Intent(getApplicationContext(), AppointmentService.class);
        PendingIntent appointmentPendingIntent = PendingIntent.getService(getApplicationContext(), 0, appointmentIntent, 0);

        calendar.set(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, appointmentPendingIntent);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void processResponse(String result) {
    }
}
