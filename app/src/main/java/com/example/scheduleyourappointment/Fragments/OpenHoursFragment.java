package com.example.scheduleyourappointment.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleyourappointment.R;
import com.example.scheduleyourappointment.model.OpeningHours;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OpenHoursFragment extends Fragment {

    private Button homeBtn;
    private TextView weekHoursTextView, fridayHoursTextView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public OpenHoursFragment() {
        // Required empty public constructor
    }

    public static OpenHoursFragment newInstance(String param1, String param2) {
        OpenHoursFragment fragment = new OpenHoursFragment();
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
        View view =  inflater.inflate(R.layout.open_hours_layout, container, false);

        homeBtn = view.findViewById(R.id.homeButton);
        weekHoursTextView = view.findViewById(R.id.weekHours);
        fridayHoursTextView = view.findViewById(R.id.fridayHours);

        readOpenHoursFromDB();


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_openHoursFragment_to_loginFragment);
            }
        });

        return view;
    }

    public void readOpenHoursFromDB() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("openingHours");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                OpeningHours openingHours = dataSnapshot.getValue(OpeningHours.class);

                if(openingHours != null) {
                    weekHoursTextView.setText(openingHours.getStartWeekHours() + " - " + openingHours.getEndWeekHours());
                    fridayHoursTextView.setText(openingHours.getStartFridayHours() + " - " + openingHours.getEndFridayHours());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}