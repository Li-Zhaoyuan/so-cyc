package com.dubailizards.so_cyc.boundary;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.dubailizards.so_cyc.databinding.ActivityMainBinding;
import com.dubailizards.so_cyc.boundary.login.LoginActivity;

/**
 *  Boundary Class, Main Activity UI, required for android
 *  Transitions to next screen on launch, enables a splash screen if required
 */
public class MainActivity extends AppCompatActivity {
    /**
     *  protected void function, Overridden Constructor of an Activity
     *  Initializes the activity, and sets up necessary parameters
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(binding.getRoot());

        // Insert splash screen here

        // Transit to login
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}