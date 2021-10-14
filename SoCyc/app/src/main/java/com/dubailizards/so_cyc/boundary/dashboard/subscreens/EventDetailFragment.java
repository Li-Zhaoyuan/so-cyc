package com.dubailizards.so_cyc.boundary.dashboard.subscreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.entity.EventDetails;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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
     *  private void function, Joins the event, updates the server
     *  @param eventID the ID of the event the user intends to join
     *  @param account is the user's account
     *  On call, update the server that the current user intends to join the specified event
     */
    private void UpdateJoinEvent(int eventID, GoogleSignInAccount account){
        // TODO: Update the database that user has joined event
    }

    /**
     *  private void function, Leaves the event, updates the server
     *  @param eventID the ID of the event the user intends to join
     *  @param account is the user's account
     *  On call, update the server that the current user intends to leave the specified event
     */
    private void UpdateLeaveEvent(int eventID, GoogleSignInAccount account){
        // TODO: Update the database that user has left event
    }
}