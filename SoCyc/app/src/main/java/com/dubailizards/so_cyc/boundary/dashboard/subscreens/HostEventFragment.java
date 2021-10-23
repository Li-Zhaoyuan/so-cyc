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
 *  Displays interactive UI elements in regards to allowing the user to host an event
 */
public class HostEventFragment extends Fragment {

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
        view = inflater.inflate(R.layout.fragment_hostevent, container, false);
        ((BaseActivity)getActivity()).setActionBarTitle("Host Event"); // Rename the title

        // Initiallize Event Details Object
        details = new EventDetails();

        // Bind the buttons to functions
        // Create Event
        view.findViewById(R.id.btn_HEcreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                CreateEvent();
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
     *  private void function, Creates an event on the database
     *  On call, update the server that the current user is hosting an event
     *  Get event details from the UI fields and update the local EventDetails
     *  Save the EventDetails to the database
     */
    private void CreateEvent(){
        // Get data from fields
        // Name
        EditText text = view.findViewById(R.id.txtField_HEEventName);
        details.setEventTitle(text.getText().toString());
        // Location
        text = view.findViewById(R.id.txtField_HEEventLocation);
        details.setEventAddress(text.getText().toString());
        // Date
        text = view.findViewById(R.id.txtField_HEDate);
        details.setEventDate(text.getText().toString());
        // Start Time
        text = view.findViewById(R.id.txtField_HEStartTime);
        details.setEventStartTime(text.getText().toString());
        // End Time
        text = view.findViewById(R.id.txtField_HEEndTime);
        details.setEventEndTime(text.getText().toString());
        // Desc
        text = view.findViewById(R.id.txtField_HEDescription);
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
    }
}