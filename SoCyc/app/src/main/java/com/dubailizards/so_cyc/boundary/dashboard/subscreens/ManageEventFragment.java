package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.BaseActivity;
import com.dubailizards.so_cyc.boundary.dashboard.DashboardFragment;
import com.dubailizards.so_cyc.control.DatabaseManager;
import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        // Initiallize Event Details Object
        details = new EventDetails();

        // Bind the buttons to functions
        // Update Event
        view.findViewById(R.id.btn_MEupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                UpdateEventData();
            }
        });

        // Delete Event
        view.findViewById(R.id.btn_MEcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DeleteEventData();
            }
        });
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
     *  private void function, Returns to dashboard fragment
     *  When called, open the DashboardFragment
     */
    private void DisplayDashboardUI(){
        DashboardFragment n = new DashboardFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), n, n.getTag()).addToBackStack(null).commit();
    }

    /**
     *  private void function, Updates the server to delete this event detail
     *  On call, update the server that the event the current user is hosting is to be deleted
     *  Delete the event on the server that is tied to this user's ID
     */
    private void DeleteEventData() {
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseManager.GetInstance().DeleteData("EventDetails", fbuser.getUid());
        Toast.makeText(getActivity().getApplicationContext(), "Event Deleted!", Toast.LENGTH_SHORT).show();
        // Return to dashboard on success
        DisplayDashboardUI();
    }

    /**
     *  private void function, Updates the server to change in event details
     *  On call, update the server that the event the current user is hosting has changes
     *  Save the changes to the database
     */
    private void UpdateEventData(){
        // Get data from fields
        // Name
        EditText text = view.findViewById(R.id.txtField_MEEventName);
        details.setEventTitle(text.getText().toString());
        // Location
        text = view.findViewById(R.id.txtField_MEEventLocation);
        details.setEventAddress(text.getText().toString());
        // Date
        text = view.findViewById(R.id.txtField_MEDate);
        details.setEventDate(text.getText().toString());
        // Start Time
        text = view.findViewById(R.id.txtField_MEStartTime);
        details.setEventStartTime(text.getText().toString());
        // End Time
        text = view.findViewById(R.id.txtField_MEEndTime);
        details.setEventEndTime(text.getText().toString());
        // Desc
        text = view.findViewById(R.id.txtField_MEDescription);
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
            Toast.makeText(getActivity().getApplicationContext(), "Please fill up all fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Push to server if inputs valid
        DatabaseManager.GetInstance().AddData("EventDetails", fbuser.getUid(), details);
        Toast.makeText(getActivity().getApplicationContext(), "Event Updated!", Toast.LENGTH_SHORT).show();

        DocumentReference ref = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("HostEventFragment", "DocumentSnapshot data: " + document.getData());
                        ref.update("eventIDs", FieldValue.arrayUnion(details.getEventHostID()));
                    } else {
                        Log.d("HostEventFragment", "No such document");
                        Map<String, Object> event = new HashMap<>();
                        event.put("eventIDs", Arrays.asList(details.getEventHostID()));
                        DatabaseManager.GetInstance().AddData("JoinedEvent",fbuser.getUid(),event);
                    }

                } else {
                    Log.d("HostEventFragment", "get failed with ", task.getException());
                }
            }
        });
        // Return to dashboard on success
        DisplayDashboardUI();
    }
}