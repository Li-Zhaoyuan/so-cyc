package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

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
                UpdateJoinEvent(details.getEventHostID());
            }
        });

        // Leave Button
        view.findViewById(R.id.btn_EDleave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                UpdateLeaveEvent(details.getEventHostID());
            }
        });

        // Cancel Button
        view.findViewById(R.id.btn_EDcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //DashboardFragment n = new DashboardFragment();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
            }
        });

        // TODO: Get the status of whether user has already joined this event
        // Hide the join and cancel button if user is already in event
        SetupEventDetailFragment(details,false);
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
            view.findViewById(R.id.btn_EDjoin).setVisibility(View.GONE);
        else view.findViewById(R.id.btn_EDleave).setVisibility(View.GONE);

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
     *  @param eventID the ID of the event the user intends to join
     *  On call, update the server that the current user intends to join the specified event
     */
    private void UpdateJoinEvent(String eventID){
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference ref = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());

        ref.update("eventIDs", FieldValue.arrayUnion(eventID));
    }

    /**
     *  private void function, Leaves the event, updates the server
     *  @param eventID the ID of the event the user intends to join
     *  On call, update the server that the current user intends to leave the specified event
     */
    private void UpdateLeaveEvent(String eventID){
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference ref = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());

        ref.update("eventIDs", FieldValue.arrayRemove(eventID));
    }
}