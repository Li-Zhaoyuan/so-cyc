package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.BaseActivity;
import com.dubailizards.so_cyc.control.DatabaseManager;
import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents a screen of the Dashboard
 *  Displays interactive UI elements in regards to managing the user's hosted event
 */
public class ManageEventFragment extends Fragment {

    /**
     *  private EventDetails variable
     *  An entity that holds event details
     */
    private EventDetails details;

    /**
     *  private View variable
     *  An entity that holds view of the current fragment
     */
    private View view;

    /**
     *  protected void function, Overridden Constructor of a Fragment
     *  Initializes the Fragment, and sets up necessary parameters
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_manageevent, container, false);
        ((BaseActivity)getActivity()).setActionBarTitle("Manage Event"); // Rename the title
        return view;
    }

    /**
     *  protected void function, Overridden Destructor of a Fragment
     *  Terminates the Fragment, deallocating variables accordingly
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     *  private void function, Updates the server to change in event details
     *  On call, update the server that the event the current user is hosting has changes
     *  Save the changes to the database
     */
    private void UpdateEventData(){
        // Get data from fields
        // Name
        EditText text = view.findViewById(R.id.txtField_EDEventName);
        details.setEventTitle(text.getText().toString());
        // Location
        text = view.findViewById(R.id.txtField_EDEventLocation);
        details.setEventAddress(text.getText().toString());
        // Date
        text = view.findViewById(R.id.txtField_EDDate);
        details.setEventDate(text.getText().toString());
        // Start Time
        text = view.findViewById(R.id.txtField_EDStartTime);
        details.setEventStartTime(text.getText().toString());
        // End Time
        text = view.findViewById(R.id.txtField_EDEndTime);
        details.setEventEndTime(text.getText().toString());
        // Desc
        text = view.findViewById(R.id.txtField_EDDescription);
        details.setEventDescription(text.getText().toString());

        // Get the user
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        // Store User Data
        details.setEventHostID(fbuser.getUid());
        details.setHostDisplayName(account.getDisplayName());
        details.setProfilePictureURL(account.getPhotoUrl().toString());

        // Failed the validity check
        if (!details.CheckValidity()){
            Toast.makeText(getActivity().getApplicationContext(), "Invalid Inputs Found", Toast.LENGTH_SHORT).show();
            return;
        }
        // Push to server if inputs valid
        DatabaseManager.GetInstance().AddData("EventDetails", fbuser.getUid(), details);
    }
}