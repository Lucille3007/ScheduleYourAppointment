package com.example.scheduleyourappointment.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import com.example.scheduleyourappointment.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ManagerLoginFragment extends Fragment {
    View view;

    private FirebaseAuth mAuth;
    private Button enterManagerBtn;
    private TextInputEditText mail, password;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ManagerLoginFragment() {
        // Required empty public constructor
    }

    public static ManagerLoginFragment newInstance(String param1, String param2) {
        ManagerLoginFragment fragment = new ManagerLoginFragment();
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
        view =  inflater.inflate(R.layout.manager_login_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        mail = view.findViewById(R.id.mailInput);
        password = view.findViewById(R.id.passwordInput);
        enterManagerBtn = view.findViewById(R.id.enterManagerButton);



        enterManagerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mail.getText().toString().trim();
                String pass = password.getText().toString();

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(requireActivity(),new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(requireContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(view).navigate(R.id.action_managerLoginFragment_to_managerHomePageFragment);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });
            }
        });


        return view;
    }

}