package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.BaseActivity;
import com.dubailizards.so_cyc.control.DatabaseManager;
import com.dubailizards.so_cyc.entity.CustomListViewAdapter;
import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
     *  private View variable
     *  An entity that holds view of the current fragment
     */
    private View view;

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
        view = inflater.inflate(R.layout.fragment_publicevent, container, false);
        ((BaseActivity)getActivity()).setActionBarTitle("Browse Public Events"); // Rename the title

        // Initiallize event detail list
        eventDetailsList = new ArrayList<EventDetails>();

        // Draw public events
        DisplayEventDetails();
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
     *  private void function, Gets List of all public events
     *  Writes the list of events into an array of EventDetails Type
     */
    private void GetPublicEventList(){
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
     *  private void function, Displays the list of public events
     *  When called, convert the list of events into a list view of all public events
     */
    private void DisplayEventDetails(){
        // Get the events that this user has joined from the DB
        GetPublicEventList();
        // From the list of events generate the rows for the listview
        ListView lv = view.findViewById(R.id.list_publiclist);

        CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(), eventDetailsList.toArray(new EventDetails[eventDetailsList.size()]));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Row press logic to open event details UI
                Toast.makeText(getActivity().getApplicationContext(), "Row "+ position, Toast.LENGTH_SHORT).show();
                DisplayEventDetailUI(eventDetailsList.get(position));
            }
        });
    }

    /**
     *  private void function, Displays the event detail UI
     *  @param ed the passed event details object to set up the detail screen
     *  When called, open the EventDetailFragment, to show the event detail screen
     */
    private void DisplayEventDetailUI(EventDetails ed){
        EventDetailFragment n = new EventDetailFragment();
        n.SetEventDetails(ed);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
    }
}