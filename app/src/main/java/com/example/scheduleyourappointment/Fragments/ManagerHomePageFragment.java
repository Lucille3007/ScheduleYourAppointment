package com.example.scheduleyourappointment.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scheduleyourappointment.R;

public class ManagerHomePageFragment extends Fragment {

    private Button openHoursBtn,availableAppsBtn, homeBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ManagerHomePageFragment() {
        // Required empty public constructor
    }

    public static ManagerHomePageFragment newInstance(String param1, String param2) {
        ManagerHomePageFragment fragment = new ManagerHomePageFragment();
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
        View view =  inflater.inflate(R.layout.manager_home_page_layout, container, false);

        openHoursBtn = view.findViewById(R.id.editHoursButton);
        availableAppsBtn = view.findViewById(R.id.editAvailableButton);
        homeBtn = view.findViewById(R.id.homeButton);

        openHoursBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_managerHomePageFragment_to_manageOpenHoursFragment);
            }
        });

        availableAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_managerHomePageFragment_to_manageAvailableAppsFragment);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_managerHomePageFragment_to_loginFragment);
            }
        });



        return view;
    }
}