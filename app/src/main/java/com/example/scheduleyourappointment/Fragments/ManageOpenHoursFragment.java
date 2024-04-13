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
import com.example.scheduleyourappointment.model.OpeningHours;
import com.example.scheduleyourappointment.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ManageOpenHoursFragment extends Fragment {

    private TextInputEditText startWeekInput, endWeekInput, startFridayInput, endFridayInput;
    private Button updateBtn, homePageBtn, manageHomePageBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ManageOpenHoursFragment() {
        // Required empty public constructor
    }
    public static ManageOpenHoursFragment newInstance(String param1, String param2) {
        ManageOpenHoursFragment fragment = new ManageOpenHoursFragment();
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
        View view = inflater.inflate(R.layout.manage_open_hours_layout, container, false);


        homePageBtn = view.findViewById(R.id.homeButton);
        manageHomePageBtn = view.findViewById(R.id.managerHomeButton);
        updateBtn = view.findViewById(R.id.updateButton);
        startWeekInput = view.findViewById(R.id.startWeekHoursInput);
        endWeekInput = view.findViewById(R.id.endWeekHoursInput);
        startFridayInput = view.findViewById(R.id.startFridayHoursInput);
        endFridayInput = view.findViewById(R.id.endFridayHoursInput);



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startWeekHours = startWeekInput.getText().toString();
                String endWeekHours = endWeekInput.getText().toString();
                String startFridayHours = startFridayInput.getText().toString();
                String endFridayHours = endFridayInput.getText().toString();


                writeOpenHoursToDB(startWeekHours, endWeekHours, startFridayHours, endFridayHours);

                Toast.makeText(requireContext(), "Thanks for update the opening hours", Toast.LENGTH_SHORT).show();
            }
        });


        manageHomePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_manageOpenHoursFragment_to_managerHomePageFragment);
            }
        });

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_manageOpenHoursFragment_to_loginFragment);
            }
        });

        return view;
    }

    public void writeOpenHoursToDB(String startWeekHours, String endWeekHours, String startFridayHours, String endFridayHours) {

        OpeningHours openingHours = new OpeningHours(startWeekHours, endWeekHours, startFridayHours, endFridayHours);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("openingHours");

        myRef.setValue(openingHours);
    }
}