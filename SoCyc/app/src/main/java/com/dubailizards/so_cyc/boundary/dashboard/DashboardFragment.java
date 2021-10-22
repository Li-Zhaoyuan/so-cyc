package com.dubailizards.so_cyc.boundary.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.EventDetailFragment;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.HostEventFragment;
import com.dubailizards.so_cyc.boundary.dashboard.subscreens.ManageEventFragment;
import com.dubailizards.so_cyc.databinding.FragmentDashboardBinding;
import com.dubailizards.so_cyc.entity.EventDetails;

import java.util.List;

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
        // Set up my entities in the view
        // Host Event
        Button btn_host = view.findViewById(R.id.btn_hostevent);
        btn_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayHostEventUI();
            }
        });
        // Manage Event
        Button btn_manage = view.findViewById(R.id.btn_manageevent);
        btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayManageEventUI();
            }
        });
        // View Event Details
        Button btn_details = view.findViewById(R.id.btn_eventdetails);
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DisplayEventDetailUI();
            }
        });
        // Set up list of events
        DisplayEventList();
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
    private void GetEventList(){
        // TODO: Get the list of events for this user
    }

    /**
     *  private void function, Displays events as UI
     *  Makes use of events in the list and draws them as UI elements
     */
    private void DisplayEventList(){
        // TODO: Draw the list of events as UI elements
        // From the list of events generate the rows for the listview
        ListView lv = view.findViewById(R.id.list_dashboardlist);
        String[] data = {"1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3","1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);
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
     *  private void function, Displays the event detail UI
     *  When called, open the EventDetailFragment, to show the event detail screen
     */
    private void DisplayEventDetailUI(){
        EventDetailFragment n = new EventDetailFragment();
        EventDetails temp = new EventDetails();
        temp.setEventTitle("Test");
        temp.setEventAddress("NTU");
        temp.setEventDate(1337);
        temp.setEventDescription("Test Desc");
        // TODO: Input the actual event details from database, Query DB for the event and insert to fragment
        n.SetEventDetails(temp);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, n, n.getTag()).addToBackStack(null).commit();
    }

    /**
     *  private void function, Displays a popup to show event details
     *  @param eventID is the ID of the event we want the details of
     */
    private void DisplayEventDetails(int eventID){
        // TODO: Implement popup of event details, opens EventDetailFragment
    }
}