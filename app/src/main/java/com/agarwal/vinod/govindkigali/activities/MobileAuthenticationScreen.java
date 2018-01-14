package com.agarwal.vinod.govindkigali.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileAuthenticationScreen extends AppCompatActivity {

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    EditText contact_et, verification_code_et;
    Button verification_btn;
    private FirebaseAuth mAuth;
    private int btn_type = 0;
    private static final String TAG = "MobileAuthentication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_authentication_screen);

        contact_et = findViewById(R.id.contact);
        verification_code_et = findViewById(R.id.verify);
        verification_btn = findViewById(R.id.verification_btn);
        mAuth = FirebaseAuth.getInstance();

        verification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_type == 0) {
                    contact_et.setEnabled(false);
                    verification_btn.setEnabled(false);

                    String mobile_number = contact_et.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile_number, 60, TimeUnit.SECONDS, MobileAuthenticationScreen.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                            signInWithPhoneAuthCredential(phoneAuthCredential);

                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {

                        }

                        @Override
                        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                            btn_type = 1;
                            mVerificationId = verificationId;
                            mResendToken = token;

                            contact_et.setVisibility(View.INVISIBLE);
                            verification_code_et.setVisibility(View.VISIBLE);
                            verification_btn.setText("Verify Code");
                            verification_btn.setEnabled(true);

                        }
                    });
                } else {

                    verification_btn.setEnabled(false);
                    String verification_code = verification_code_et.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verification_code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, "onComplete: " + user.getPhoneNumber() + user.getDisplayName() + user.getProviderId());
                            Toast.makeText(MobileAuthenticationScreen.this, user.getPhoneNumber() + "Has Been Verified", Toast.LENGTH_SHORT).show();

                            prefManager.setMobileNumberRegistered(true);

                            Intent intent = new Intent(MobileAuthenticationScreen.this, SignInScreen.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
