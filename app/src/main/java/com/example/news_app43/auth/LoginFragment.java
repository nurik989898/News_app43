package com.example.news_app43.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.news_app43.MainActivity;
import com.example.news_app43.R;
import com.example.news_app43.databinding.FragmentLoginBinding;
import com.example.news_app43.ui.home.HomeFragment;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class LoginFragment extends Fragment {
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FragmentLoginBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private String mVerificationId;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "MAIN_TAG";
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.codeA.setVisibility(View.VISIBLE);
        binding.codeB.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(requireContext());
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
           pd.dismiss();
                Toast.makeText(requireContext(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent: " + verificationId);
                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                binding.codeA.setVisibility(View.GONE);
                binding.codeB.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Verification code Sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
        binding.Btnnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        String phone = binding.Edittype.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(requireContext(),"Please enter phone number..",Toast.LENGTH_SHORT).show();
        }else{
            startPhoneVerification(phone);
        }
            }
        });
        binding.Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.Edittype.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(requireContext(),"Please enter phone number..",Toast.LENGTH_SHORT).show();
                }else{
                    resendVerification(phone,forceResendingToken);
                }
            }
        });
        binding.BtnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        String code = binding.EditCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)){
            Toast.makeText(requireContext(), "Please enter your code...", Toast.LENGTH_SHORT).show();
        }else{
            verifyPhoneNumberWithCode(mVerificationId, code);
        }
            }
        });
    }
    private void startPhoneVerification(String phone) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void resendVerification(String phone,PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending Code");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
    pd.setMessage("Verify Code");
    pd.show();
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
    signIn(credential);
    }

    private void signIn(PhoneAuthCredential credential) {
        pd.setMessage("Logging IN");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(requireContext(), "Logged In as"+phone, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireContext(), HomeFragment.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(requireContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}