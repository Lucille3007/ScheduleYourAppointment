package com.example.scheduleyourappointment.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.scheduleyourappointment.R;
import com.example.scheduleyourappointment.model.Appointment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAppointmentFragment extends Fragment {

    private TextInputEditText id, date, time;
    private Button addBtn, toAvailableAppsButton;
    Appointment appointmentToAdd;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;


    public AddAppointmentFragment() {
        // Required empty public constructor
    }

    public static AddAppointmentFragment newInstance(String param1, String param2) {
        AddAppointmentFragment fragment = new AddAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_appointment_layout, container, false);


        toAvailableAppsButton = view.findViewById(R.id.toAvailableAppsButton);
        addBtn = view.findViewById(R.id.addButton);
        id = view.findViewById(R.id.idInput);
        date = view.findViewById(R.id.dateInput);
        time = view.findViewById(R.id.timeInput);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isAllFieldsChecked = checkInput();

                if(isAllFieldsChecked){

                    String idStr = id.getText().toString();
                    String dateStr = date.getText().toString();
                    String timeStr = time.getText().toString();

                    appointmentToAdd = new Appointment(dateStr,timeStr,idStr);

                    writeAvailableApps(appointmentToAdd,idStr);
                    Toast.makeText(requireContext(), "Thanks for adding a new appointment", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_addAppointmentFragment_to_manageAvailableAppsFragment);
                }


            }
        });


        toAvailableAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_addAppointmentFragment_to_manageAvailableAppsFragment);
            }
        });



        return view;
    }


    public static void writeAvailableApps(Appointment appointment, String id) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("availableAppointments").child(id);

        myRef.setValue(appointment);
    }

    private Boolean checkInput() {
        if (id.length() == 0) {
            id.setError("This field is required");
            return false;
        }
        if (date.length() == 0) {
            date.setError("This field is required");
            return false;
        } else if (date.length() != 10 ) {
            date.setError("Check your formatted date");
            return false;
        }
        if (time.length() == 0) {
            time.setError("This field is required");
            return false;
        } else if (time.length() != 5) {
            time.setError("Check your formatted time");
            return false;
        }

        // after all validation return true.
        return true;
    }
}