package com.agarwal.vinod.govindkigali.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.google.android.gms.auth.api.Auth;
import com.agarwal.vinod.govindkigali.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationScreen extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private Button signOutButton;
    private TextView nameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_screen);
        signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        nameTextView = (TextView) findViewById(R.id.name_text_view);
        emailTextView = (TextView) findViewById(R.id.email_text_view);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("25611614140-msaag3cb2igdcbt62dsdqgdpdgc3k9qf.apps.googleusercontent.com")
//                .requestIdToken("25611614140-msaag3cb2igdcbt62dsdqgdpdgc3k9qf.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                signInButton.setVisibility(View.GONE);
//                signOutButton.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (user.getDisplayName() != null)
                        nameTextView.setText("HI " + user.getDisplayName().toString());
//                    emailTextView.setText(user.getEmail().toString());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        /*
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                signInButton.setVisibility(View.VISIBLE);
                                signOutButton.setVisibility(View.GONE);
                                emailTextView.setText(" ".toString());
                                nameTextView.setText(" ".toString());
                            }
                        });
            }
        });*/

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + requestCode);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "onActivityResult: " + result.toString());
            Log.d(TAG, "onActivityResult: Yo " + result.getStatus().getStatusMessage());
            Log.d(TAG, "onActivityResult: Yo " + result.getStatus().hasResolution());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.d(TAG, "onActivityResult: " + "google_signin_failed");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    PrefManager prefManager = new PrefManager(getApplicationContext());

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(AuthenticationScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            Uri photoUrl = user.getPhotoUrl();
                            Log.d(TAG, "onComplete: " + name + email + photoUrl);

                            prefManager.setLoggedInViagmail(true);
                            signInButton.setVisibility(View.INVISIBLE);

                            Toast.makeText(AuthenticationScreen.this, name+"\n"+user.getEmail()+"\nLogged In", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AuthenticationScreen.this, SignInScreen.class);
                            startActivity(intent);
                            finish();
                            //  updateUI(user);
                        }
                    }
                });
    }

}
