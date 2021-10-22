package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.control.DatabaseManager;
import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents a screen of the Dashboard
 *  Displays interactive UI elements of a scrollable list of all public events
 */
public class PublicEventFragment extends Fragment {

    /**
     *  private EventDetails variable
     *  An entity that holds event details
     */
    private EventDetails details;

    /**
     *  private List<EventDetails> variable
     *  A list that holds event details
     */
    private List<EventDetails> eventDetailsList;

    /**
     *  protected void function, Overridden Constructor of a Fragment
     *  Initializes the Fragment, and sets up necessary parameters
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getView();
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
     *  private void function, Gets List of all public events
     *  Writes the list of events into an array of EventDetails Type
     */
    private void GetPublicEventList(){
        // TODO: Get the list of events for this user
        DatabaseManager.GetInstance().GetFireStore().collection("EventDetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DatabaseManager", document.getId() + " => " + document.getData());
                                //insert code here
                                //document.getId() -> ID of host
                                //document.getData() ->  EventDetails of the event
                                EventDetails ed = document.toObject(EventDetails.class);
                                eventDetailsList.add(ed);
                            }
                        } else {
                            Log.d("DatabaseManager", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    /**
     *  private void function, Displays a popup to show event details
     *  @param eventID is the ID of the event we want the details of
     */
    private void DisplayEventDetails(int eventID){
        // TODO: Implement popup of event details, opens EventDetailFragment
    }
}