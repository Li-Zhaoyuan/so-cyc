package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.BaseActivity;
import com.dubailizards.so_cyc.control.DatabaseManager;
import com.dubailizards.so_cyc.entity.EventDetails;
import com.dubailizards.so_cyc.entity.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents a screen of the Dashboard
 *  Displays interactive UI elements in regards to the details of events
 *  User can choose to join or leave the event
 */
public class EventDetailFragment extends Fragment {

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
        view = inflater.inflate(R.layout.fragment_eventdetail, container, false);
        ((BaseActivity)getActivity()).setActionBarTitle("Event Details"); // Rename the title

        // Set up listeners for buttons
        // Join Button
        view.findViewById(R.id.btn_EDjoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                UpdateJoinEvent();
            }
        });

        // Leave Button
        view.findViewById(R.id.btn_EDleave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                UpdateLeaveEvent();
            }
        });

        // TODO: Get the status of whether user has already joined this event
        // Hide the join and cancel button if user is already in event
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("EventDetailFragment", "DocumentSnapshot data: " + document.getData());
                        ArrayList arr = (ArrayList)(document.getData().get("eventIDs"));
                        //do stuff with array
                        if(arr != null) {
                            if(arr.contains(details.getEventHostID()))
                            {
                                SetupEventDetailFragment(details, true);

                                Log.d("EventDetailFragment", "In event");
                            }
                            else
                            {
                                SetupEventDetailFragment(details, false);
                                Log.d("EventDetailFragment", "Not in event");
                            }
                        }
                        else
                        {
                            SetupEventDetailFragment(details, false);
                            Log.d("EventDetailFragment", "Not in event");
                        }
                    } else {
                        Log.d("EventDetailFragment", "No such document, not in event");
                        SetupEventDetailFragment(details,false);
                    }

                } else {
                    Log.d("EventDetailFragment", "get failed with ", task.getException());
                }
            }
        });

        //SetupEventDetailFragment(details,false);
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

    public void SetEventDetails(EventDetails event){
        details = event;
    }

    /**
     *  public void function, Setup Event Details fragment
     *  @param event the passed EventDetails object that the fragment will use to display information
     *  @param userInEvent a boolean parameter defining whether the user is currently part of this event
     *  On call, update the event details on the screen, hide the join or leave button depending on whether the user is not in or in the event respectively
     */
    public void SetupEventDetailFragment(EventDetails event, boolean userInEvent){
        // copy the passed details to this class
        details = event;
        if (userInEvent) // User is already in event
        {
            view.findViewById(R.id.btn_EDjoin).setVisibility(View.GONE);
            view.findViewById(R.id.btn_EDleave).setVisibility(View.VISIBLE);
        }
        else
        {
            view.findViewById(R.id.btn_EDleave).setVisibility(View.GONE);
            view.findViewById(R.id.btn_EDjoin).setVisibility(View.VISIBLE);
        }

        // Setup event details
        // Name
        EditText text = view.findViewById(R.id.txtField_EDEventName);
        text.setText(event.getEventTitle());
        // Location
        text = view.findViewById(R.id.txtField_EDEventLocation);
        text.setText(event.getEventAddress());

        // Date
        text = view.findViewById(R.id.txtField_EDDate);
        text.setText(event.getEventDate());
        // Start Time
        text = view.findViewById(R.id.txtField_EDStartTime);
        text.setText(event.getEventStartTime());
        // End Time
        text = view.findViewById(R.id.txtField_EDEndTime);
        text.setText(event.getEventEndTime());

        // Desc
        text = view.findViewById(R.id.txtField_EDDescription);
        text.setText(event.getEventDescription());

    }

    /**
     *  private void function, Joins the event, updates the server
     *  On call, update the server that the current user intends to join the specified event
     */
    private void UpdateJoinEvent(){
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference ref = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("EventDeatilFragment", "DocumentSnapshot data: " + document.getData());
                        ref.update("eventIDs", FieldValue.arrayUnion(details.getEventHostID()));
                    } else {
                        Log.d("EventDeatilFragment", "No such document");
                        Map<String, Object> event = new HashMap<>();
                        event.put("eventIDs", Arrays.asList(details.getEventHostID()));
                        DatabaseManager.GetInstance().AddData("JoinedEvent",fbuser.getUid(),event);
                    }

                } else {
                    Log.d("EventDeatilFragment", "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     *  private void function, Leaves the event, updates the server
     *  On call, update the server that the current user intends to leave the specified event
     */
    private void UpdateLeaveEvent(){
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference ref = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());

        ref.update("eventIDs", FieldValue.arrayRemove(details.getEventHostID()));
    }
}