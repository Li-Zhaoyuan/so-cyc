package com.dubailizards.so_cyc.boundary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.navigation.NavigationFragment;
import com.dubailizards.so_cyc.boundary.navigation.directionhelpers.TaskLoadedCallback;
import com.dubailizards.so_cyc.databinding.ActivityBaseBinding;
import com.dubailizards.so_cyc.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 *  Boundary Class, UI that updates based on bottom navigation bar
 *  Loads screen Fragments depending on the pressed button of navigation bar
 */
public class BaseActivity extends AppCompatActivity{

    /**
     *  private ActivityBaseBinding variable
     *  Auto generated class type that represents the binding between XML layout file and data objects
     */
    private ActivityBaseBinding binding;

    /**
     *  protected void function, Overridden Constructor of an Activity
     *  Initializes the activity, and sets up necessary parameters
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_dashboard, R.id.nav_navigation, R.id.nav_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     *  public void function, sets the action bar title to passed string
     *  @param title is the new title of the action bar
     */
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}