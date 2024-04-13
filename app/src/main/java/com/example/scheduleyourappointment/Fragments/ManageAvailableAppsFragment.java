package com.example.scheduleyourappointment.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scheduleyourappointment.R;
import com.example.scheduleyourappointment.adapters.AvailableAdapter;
import com.example.scheduleyourappointment.model.Appointment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ManageAvailableAppsFragment extends Fragment implements AvailableAdapter.OnAvailableAppointmentListener {

    RecyclerView availableAppsres;
    AvailableAdapter availableAdapter;
    LinearLayoutManager gridLayoutManager;
    private Button addBtn, homeBtn, manageHomeBtn;
    DatabaseReference myReference;
    ArrayList<Appointment> availableAppointments;
    Appointment appointmentToDelete;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    public ManageAvailableAppsFragment() {
        // Required empty public constructor
    }

    public static ManageAvailableAppsFragment newInstance(String param1, String param2) {
        ManageAvailableAppsFragment fragment = new ManageAvailableAppsFragment();
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
        View view = inflater.inflate(R.layout.manage_available_apps_lable, container, false);

        addBtn = view.findViewById(R.id.addButton);
        homeBtn = view.findViewById(R.id.homeButton);
        manageHomeBtn = view.findViewById(R.id.managerHomeButton);
        availableAppsres = view.findViewById(R.id.availableAppsres);

        availableAppointments = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);

        availableAdapter = new AvailableAdapter(requireContext(), availableAppointments, this);

        availableAppsres.setLayoutManager(gridLayoutManager);
        availableAppsres.setAdapter(availableAdapter);


        myReference = FirebaseDatabase.getInstance().getReference("availableAppointments");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableAppointments.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Appointment appointment = itemSnapshot.getValue(Appointment.class);
                    availableAppointments.add(appointment);
                }
                availableAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_manageAvailableAppsFragment_to_addAppointmentFragment);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_manageAvailableAppsFragment_to_loginFragment);
            }
        });

        manageHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_manageAvailableAppsFragment_to_managerHomePageFragment);
            }
        });

        return view;
    }


    @Override
    public void OnAvailableAppointmentLongClick(int position) {

        appointmentToDelete = availableAppointments.get(position);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("availableAppointments").child(appointmentToDelete.getId());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // remove the value at reference
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        availableAppointments.remove(position);
        availableAdapter.notifyItemRemoved(position);

        Snackbar.make(availableAppsres, "The deleted appointment:" + appointmentToDelete.getDate() + ",at " + appointmentToDelete.getTime() + "o'clock", Snackbar.LENGTH_LONG)
                .setAction("Undelete it!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        availableAppointments.add(position, appointmentToDelete);
                        AddAppointmentFragment.writeAvailableApps(appointmentToDelete, appointmentToDelete.getId());
                        availableAdapter.notifyDataSetChanged();
                    }
                }).show();
    }

    @Override
    public void OnAvailableAppointmentClick(int position) {
    }
}
