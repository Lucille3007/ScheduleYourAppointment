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

import android.widget.Toast;

import com.example.scheduleyourappointment.adapters.AvailableAdapter;
import com.example.scheduleyourappointment.adapters.MyAdapter;
import com.example.scheduleyourappointment.R;
import com.example.scheduleyourappointment.model.Appointment;
import com.example.scheduleyourappointment.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ScheduleFragment extends Fragment implements MyAdapter.OnAppointmentListener, AvailableAdapter.OnAvailableAppointmentListener {
    private static final int AVAILABLERECYCLER = 123;
    View view;
    private FirebaseAuth auth;
    private Button logOutBtn,scheduleBtn;
    RecyclerView resAppointments, availableAppsRes;
    ArrayList<Appointment> myAppointments, availableAppointments;
    LinearLayoutManager linearLayoutManager1, gridLayoutManager;
    MyAdapter myAdapter;
    AvailableAdapter availableAdapter;
    Appointment chosenAppointment,appointmentToDelete;
    Integer indexChosenAppointment = -1;
    Integer flagAvailable = -1;
    Integer flagMyApps = -1;
    String phone, key;
    final Bundle bundle = new Bundle();
    DatabaseReference myReference, availableAppsReference;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.schedule_layout, container, false);


        auth = FirebaseAuth.getInstance();
        logOutBtn = view.findViewById(R.id.logOutButton);
        scheduleBtn = view.findViewById(R.id.scheduleButton);
        resAppointments = view.findViewById(R.id.resAppointments);
        availableAppsRes = view.findViewById(R.id.availableAppsRes);


        availableAppointments = new ArrayList<>();
        myAppointments = new ArrayList<>();


        linearLayoutManager1 = new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
        gridLayoutManager = new GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false);


        myAdapter = new MyAdapter(requireContext(),myAppointments, this);
        resAppointments.setLayoutManager(linearLayoutManager1);
        resAppointments.setAdapter(myAdapter);

        availableAdapter = new AvailableAdapter(requireContext(),availableAppointments,this);
        availableAppsRes.setLayoutManager(gridLayoutManager);
        availableAppsRes.setAdapter(availableAdapter);

// Reading My appointments from the DB
        if(getArguments() != null){
            phone = getArguments().getString("phone");

            myReference = FirebaseDatabase.getInstance().getReference("users").child(phone);
            myReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user != null) {
                        myAppointments.clear();
                        for ( Appointment appointment: user.getAppointments())
                        {
                            //appointment.setId(snapshot.getKey());
                            myAppointments.add(appointment);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

// Reading the available appointments from the DB
        availableAppsReference = FirebaseDatabase.getInstance().getReference("availableAppointments");
        availableAppsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableAppointments.clear();

                for ( DataSnapshot itemSnapshot: snapshot.getChildren())
                {
                    Appointment appointment = itemSnapshot.getValue(Appointment.class);
                    if(appointment!= null){
                        availableAppointments.add(appointment);
                    }
                }
                availableAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_to_loginFragment);
            }
        });

        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(indexChosenAppointment != -1) {

                    availableAppointments.remove(chosenAppointment);
                    availableAdapter.notifyItemRemoved(indexChosenAppointment);

                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("availableAppointments").child(chosenAppointment.getId());

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // remove the value at reference
                            dataSnapshot.getRef().removeValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });

                    flagAvailable = 1;

                    Snackbar.make(availableAppsRes,"The chosen appointment:"+ chosenAppointment.getDate() + ",at " + chosenAppointment.getTime() + "o'clock" ,Snackbar.LENGTH_LONG)
                            .setAction("Undelete it!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    availableAppointments.add(indexChosenAppointment,chosenAppointment);
                                    AddAppointmentFragment.writeAvailableApps(chosenAppointment,chosenAppointment.getId());
                                    availableAdapter.notifyDataSetChanged();
                                    flagAvailable = -1;
                                }
                            }).show();

                    if(flagAvailable == 1) {
                        myAppointments.add(chosenAppointment);
                        writeData(phone,myAppointments);
                        myAdapter.notifyDataSetChanged();

                        bundle.putString("phone", phone);
                        Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_self,bundle);
                    }
                }
                else {
                    scheduleBtn.setError("You need to choose an appointment");
                }
            }
        });
    return view;
    }



    public void writeData(String phone, ArrayList<Appointment> appointments) {

        User user = new User(phone,appointments);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);

        myRef.setValue(user);
    }

    @Override
    public void OnAppointmentLongClick(int position) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(phone);

        appointmentToDelete = myAppointments.get(position);
        key = appointmentToDelete.getId();

        myAppointments.remove(appointmentToDelete);
        myAdapter.notifyItemRemoved(position);

        Query query=databaseReference.child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // remove the value at reference
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        flagMyApps =1;

        Snackbar.make(resAppointments,"The deleted appointment:"+ appointmentToDelete.getDate() + ",at " + appointmentToDelete.getTime() + "o'clock" ,Snackbar.LENGTH_LONG)
                .setAction("Undelete it!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAppointments.add(position,appointmentToDelete);
                writeData(phone,myAppointments);
                myAdapter.notifyDataSetChanged();
                flagMyApps =-1;
            }
        }).show();
        if(flagMyApps == 1) {
            availableAppointments.add(appointmentToDelete);
            AddAppointmentFragment.writeAvailableApps(appointmentToDelete,appointmentToDelete.getId());
            availableAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void OnAvailableAppointmentLongClick(int position) {
    }

    @Override
    public void OnAvailableAppointmentClick(int position) {
        chosenAppointment = availableAppointments.get(position);
        indexChosenAppointment = position;
        Toast.makeText(requireContext(),"Chosen Appointment: " + chosenAppointment.toString(), Toast.LENGTH_LONG).show();
    }
}