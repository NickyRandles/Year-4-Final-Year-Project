package com.project.nicky.medicalmanager.Home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.project.nicky.medicalmanager.Doctor.AddAppointmentsActivity;
import com.project.nicky.medicalmanager.Patient.AddMedicationsActivity;
import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.UserInformation;

public class PlannerFragment extends Fragment {

     private ImageButton appointmentButton, medicationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        appointmentButton = (ImageButton)view.findViewById(R.id.appointmentButton);
        medicationButton = (ImageButton)view.findViewById(R.id.medicationButton);

        UserInformation userInformation = new UserInformation(getActivity());
        String userType = userInformation.getUserType();
        switch (userType){
            case "patient": appointmentButton.setVisibility(View.INVISIBLE);break;
            case "doctor": medicationButton.setVisibility(View.INVISIBLE);break;
        }
        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMedicationsActivity.class);
                startActivity(intent);
            }
        });
        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddAppointmentsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
