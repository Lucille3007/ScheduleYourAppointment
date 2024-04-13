package com.example.scheduleyourappointment.Fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.scheduleyourappointment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    View view;
    private FirebaseAuth auth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private TextInputEditText otp, phone;
    private Button generateOTPBtn, scheduleBtn, openHoursBtn, managerBtn;
    private ProgressBar bar;
    final Bundle bundle = new Bundle();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        view =  inflater.inflate(R.layout.login_layout, container, false);

        auth = FirebaseAuth.getInstance();
        otp = view.findViewById(R.id.otpInput);
        phone = view.findViewById(R.id.phoneInput);
        scheduleBtn = view.findViewById(R.id.enterButton);
        openHoursBtn = view.findViewById(R.id.seeOpenHoursButton);
        managerBtn = view.findViewById(R.id.managerButton);
        generateOTPBtn = view.findViewById(R.id.generateOTPButton);
        bar = view.findViewById(R.id.progressBar);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                final String code = credential.getSmsCode();

                if(code!= null) {
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(requireContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                super.onCodeSent(verificationId,token);
                mVerificationId = verificationId;
                Toast.makeText(requireContext(), "Code Sent", Toast.LENGTH_LONG).show();
                scheduleBtn.setEnabled(true);
                bar.setVisibility(View.INVISIBLE);
            }
        };

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userPhone = phone.getText().toString();

                if(userPhone.isEmpty()) {
                    phone.setError("Phone cannot be empty");
                }
                else {
                    bar.setVisibility(View.VISIBLE);
                    sendVerificationCode(userPhone);
                }
            }
        });


        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpInput = otp.getText().toString();

                if(otpInput.isEmpty()) {
                    otp.setError("OTP cannot be empty");
                }
                else {
                    verifyCode(otpInput);
                }
            }
        });

        openHoursBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_openHoursFragment);
            }
        });

        managerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_managerLoginFragment);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!= null) {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_scheduleFragment);
        }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVerificationCode(String userPhone) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+972"+userPhone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

                    String userPhone = phone.getText().toString();
                    if(userPhone.isEmpty()) {
                        bundle.putString("phone", "n");
                    }
                    else{
                        bundle.putString("phone", userPhone);
                    }
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_scheduleFragment,bundle);
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(requireContext(), "Verification code invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}