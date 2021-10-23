package com.dubailizards.so_cyc.boundary.dashboard;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.EventDetailFragment;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.HostEventFragment;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.ManageEventFragment;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.PublicEventFragment;
import com.dubailizards.so_cyc.control.DatabaseManager;
import com.dubailizards.so_cyc.databinding.FragmentDashboardBinding;
import com.dubailizards.so_cyc.entity.CustomListViewAdapter;
import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Dashboard
 *  Displays interactive UI elements in regards to events
 */
public class DashboardFragment extends Fragment {

    /**
     *  private FragmentDashboardBinding variable
     *  Auto generated class type that represents the binding between XML layout file and data objects
     */
    private FragmentDashboardBinding binding;

    /**
     *  private List<EventDetails> variable
     *  A list that holds event details
     */
    private List<EventDetails> eventDetailsList;

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
        // Setup Host Event Button
        // Get the view where UI entities are stored
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // Initiallize event detail list
        eventDetailsList = new ArrayList<EventDetails>();

        // Set up my entities in the view
        // Host Event
        view.findViewById(R.id.btn_hostevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayHostEventUI();
            }
        });
        // Manage Event
        view.findViewById(R.id.btn_manageevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayManageEventUI();
            }
        });
        // View Event Details
        view.findViewById(R.id.btn_eventdetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayHostEventDetails();
            }
        });
        // View Public Details
        view.findViewById(R.id.btn_publicevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayPublicEventUI();
            }
        });

        // Set up list of events
        GetPublicEventList();
        // On complete gets the list of joined events
        // On complete sets up the list view

        // Return the fragment's view
        return view;
    }

    /**
     *  protected void function, Overridden Destructor of a Fragment
     *  Terminates the Fragment, deallocating variables accordingly
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     *  private void function, Gets List of Events
     *  Writes the list of events into an array of EventDetails Type
     */
    private void GetJoinedEventList(){
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = DatabaseManager.GetInstance().GetFireStore().collection("JoinedEvent").document(fbuser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("DashboardFragment", "DocumentSnapshot data: " + document.getData());
                        ArrayList arr = (ArrayList)(document.getData().get("eventIDs"));
                        //do stuff with array
                        DisplayJoinedEventList(arr);

                    } else {
                        Log.d("DashboardFragment", "No such document");
                    }

                } else {
                    Log.d("DashboardFragment", "get failed with ", task.getException());
                }
            }
        });
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
                            GetJoinedEventList(); // Get the list of joined events
                        } else {
                            Log.d("DatabaseManager", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     *  private void function, Displays events as UI
     *  Makes use of events in the list and draws them as UI elements
     */
    private void DisplayJoinedEventList(List<String> joined){
        // Construct a list of event details that this current user has joined
        // From the list of events generate the rows for the listview
        if (joined.size() == 0)
            return; // Didn't join any

        // Find the events that the user has joined put them in a list
        List<EventDetails> joinedEvents = new ArrayList<EventDetails>();
        for (String join : joined){
            for (EventDetails event : eventDetailsList){
                if (event.getEventHostID().equals(join)){
                    Log.d("Check Join: ", "HIT");
                    joinedEvents.add(event);
                }
                Log.d("Check Join: ", "Miss " + event.getEventHostID() + " vs " + join);
            }
        }

        ListView lv = view.findViewById(R.id.list_dashboardlist);
        CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(), joinedEvents.toArray(new EventDetails[joinedEvents.size()]));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  // TODO Row press logic to open event details UI
                  Toast.makeText(getActivity().getApplicationContext(), "Row "+ position, Toast.LENGTH_SHORT).show();
                  DisplayEventDetailUI(joinedEvents.get(position));
              }
        });
    }
    /**
     *  private void function, Displays the host event UI
     *  When called, open the HostEventFragment, to show the host screen
     */
    private void DisplayHostEventUI(){
        HostEventFragment n = new HostEventFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
    }

    /**
     *  private void function, Displays the manage event UI
     *  When called, open the ManageEventFragment, to show the host screen
     */
    private void DisplayManageEventUI(){
        ManageEventFragment n = new ManageEventFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
    }

    /**
     *  private void function, Displays the manage event UI
     *  When called, open the ManageEventFragment, to show the host screen
     */
    private void DisplayPublicEventUI(){
        PublicEventFragment n = new PublicEventFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
    }

    /**
     *  private void function, Displays the event detail UI
     * @param ed the passed event details object to set up the detail screen
     *  When called, open the EventDetailFragment, to show the event detail screen
     */
    private void DisplayEventDetailUI(EventDetails ed){
        EventDetailFragment n = new EventDetailFragment();
        n.SetEventDetails(ed);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
    }

    /**
     *  private void function, Gets the host's event, open the event details for it
     *  When called, find the host's event, open the EventDetailFragment, to show the event details of the event
     */
    private void DisplayHostEventDetails(){
        // TODO: Find the event pertaining to host
        EventDetails temp = new EventDetails();
        temp.setEventTitle("Host's Event");
        temp.setEventHostID("???");
        temp.setEventAddress("NTU");
        temp.setEventDate("1337");
        temp.setEventStartTime("1200");
        temp.setEventEndTime("2200");
        temp.setEventDescription("Test Desc");
        eventDetailsList.add(temp);

        // Open the event details page for the event
        DisplayEventDetailUI(temp);
    }
}