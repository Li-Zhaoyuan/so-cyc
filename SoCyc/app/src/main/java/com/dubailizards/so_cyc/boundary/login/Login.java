package com.dubailizards.so_cyc.boundary.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dubailizards.so_cyc.boundary.BaseActivity;
import com.dubailizards.so_cyc.boundary.MainActivity;
import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.TestingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    /**
     *  A method of Login class
     *  Check to see if the user is currently signed in
     */
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(), Profile.class); // TODO: Change this to BaseActivity, once logout can be done from there
            startActivity(intent);
        }
    }

    /**
     *  A method of Login class
     *  onCreate method, get the shared instance of the FirebaseAuth object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        createRequest();

        findViewById(R.id.google_singIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Find my skip button
        Button Btn_SkipLogin = findViewById(R.id.skip_login);
        // Link this button with a listener to transit screens if clicked
        Btn_SkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchActivities(1);
            }
        });

        // Find my map test button
        Button Btn_MapTest = findViewById(R.id.map_test);
        // Link this button with a listener to transit screens if clicked
        Btn_MapTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchActivities(0);
            }
        });
    }

    /**
     * A private void function, for debug purposes
     * @param selection is the screen transition choice
     * Transits the current activity to the MainActivity Class
     */
    private void SwitchActivities(int selection) {
        Intent switchActivityIntent;
        switch(selection){
            case 1:
                switchActivityIntent = new Intent(this, BaseActivity.class);
                break;
            default:
                switchActivityIntent = new Intent(this, TestingActivity.class);
                break;
        }
        startActivity(switchActivityIntent);
    }

    /**
     *  A method of Login class
     *  Integrating Google Sign-In and configure of GoogleSignInOptions object.
     */
    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     *  A method of Login class
     *  Ensure GoogleSignInClient is always sign out, get new signInIntent.
     */
    private void signIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     *  A method of Login class
     *  Pass the server's client ID to the requestIdToken method. To find the OAuth 2.0 client ID.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *  A method of Login class
     *  After user successfully signs in, get an ID token from the GoogleSignInAccount object.
     *  Exchange it for a Firebase credential, and authenticate with Firebase using the Firebase credential.
     */
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
