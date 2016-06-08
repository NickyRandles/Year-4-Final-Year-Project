package com.project.nicky.medicalmanager.Doctor;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import com.project.nicky.medicalmanager.Home.HomeActivity;
import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;
import com.project.nicky.medicalmanager.Startup.Validation;

import java.util.Calendar;

public class AddAppointmentsActivity extends AppCompatActivity implements BackgroundResponse {

    private EditText scheduleNameField;
    private Button startDateButton, startTimeButton, durationButton, amountButton;
    private RadioGroup durationRadioGroup;
    private RadioButton numberRadioButton;
    private Calendar calendar = Calendar.getInstance();
    private String name, startDate, startTime, minutesDuration, amount, daysDuration;
    private BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointments);

        scheduleNameField = (EditText)findViewById(R.id.scheduleNameField);
        startDateButton = (Button)findViewById(R.id.startDateButton);
        startTimeButton = (Button)findViewById(R.id.startTimeButton);
        durationButton = (Button)findViewById(R.id.durationButton);
        amountButton = (Button)findViewById(R.id.amountButton);
        durationRadioGroup = (RadioGroup)findViewById(R.id.durationRadioGroup);
        numberRadioButton = (RadioButton)findViewById(R.id.numberRadioButton);

        startDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        startDateButton.setText(startDate);
        startTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":0";
        startTimeButton.setText(startTime);
        minutesDuration = "15";
        durationButton.setText(minutesDuration);
        amount = "1";
        amountButton.setText(amount);
        daysDuration = "100";

        durationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.continuousRadioButton){
                    daysDuration = "100";
                }
                else if(checkedId == R.id.numberRadioButton){
                    final Dialog dialog = new Dialog(AddAppointmentsActivity.this);
                    dialog.setTitle("Amount of days");
                    dialog.setContentView(R.layout.dialog_number);
                    dialog.show();

                    final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(100);

                    Button okButton = (Button) dialog.findViewById(R.id.okButton);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            daysDuration = Integer.toString(numberPicker.getValue());
                            numberRadioButton.setText("Number of days: " + daysDuration);
                            dialog.hide();
                        }
                    });
                }
            }
        });
    }

    public void dateButtonClicked(View view) {
        new DatePickerDialog(AddAppointmentsActivity.this, listener, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int days = dayOfMonth;
            int months = monthOfYear;
            int years = year;
            startDate = years + "-" + months + "-" + days;
            startDateButton.setText(startDate);
        }
    };

    public void timeButtonClicked(View view) {
        final Dialog dialog = new Dialog(AddAppointmentsActivity.this);
        dialog.setTitle("Enter time");
        dialog.setContentView(R.layout.dialog_time);
        dialog.show();

        final NumberPicker hourPicker = (NumberPicker) dialog.findViewById(R.id.hourPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        final NumberPicker minutesPicker = (NumberPicker) dialog.findViewById(R.id.minutesPicker);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minutesPicker.getValue() < 10) {
                    startTime = hourPicker.getValue() + ":0" + minutesPicker.getValue() + ":0";
                } else {
                    startTime = hourPicker.getValue() + ":" + minutesPicker.getValue() + ":0";
                }
                startTimeButton.setText(startTime);
                dialog.hide();
            }
        });
    }

    public void durationButtonClicked(View view) {
        final Dialog dialog = new Dialog(AddAppointmentsActivity.this);
        dialog.setTitle("Duration of each appointment");
        dialog.setContentView(R.layout.dialog_number);
        dialog.show();

        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutesDuration = Integer.toString(numberPicker.getValue());
                durationButton.setText(minutesDuration);
                dialog.hide();
            }
        });
    }

    public void amountButtonClicked(View view) {
        final Dialog dialog = new Dialog(AddAppointmentsActivity.this);
        dialog.setTitle("Amount of Appointments");
        dialog.setContentView(R.layout.dialog_number);
        dialog.show();

        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);

        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.toString(numberPicker.getValue());
                amountButton.setText(amount);
                dialog.hide();
            }
        });
    }

    public void doneButtonClicked(View view) {
        name = scheduleNameField.getText().toString();
        Validation validation = new Validation();
        validation.checkIfEmpty(name, scheduleNameField, "Please enter a name for this schedule");
        if(validation.counter == 0){
            UserInformation userInformation = new UserInformation(this);
            String username = userInformation.getUsername();
            String type = "addAppointments";
            backgroundTask = new BackgroundTask(this);
            backgroundTask.backgroundResponse = this;
            backgroundTask.execute(type, username, name, startDate, startTime, minutesDuration, amount, daysDuration);
        }
    }

    @Override
    public void processResponse(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
