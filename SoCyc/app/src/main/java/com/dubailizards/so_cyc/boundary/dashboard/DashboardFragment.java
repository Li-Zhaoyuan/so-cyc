package com.dubailizards.so_cyc.boundary.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dubailizards.so_cyc.databinding.FragmentDashboardBinding;

// TODO: Figure out how fragment transitions work to implement sub event menus

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Dashboard
 *  Displays interactive UI elements in regards to events
 */
public class DashboardFragment extends Fragment {

    /**
     *  private DashboardViewModel variable
     *  Object that holds logical code relating to this fragment
     */
    private DashboardViewModel dashboardViewModel;

    /**
     *  private FragmentDashboardBinding variable
     *  Auto generated class type that represents the binding between XML layout file and data objects
     */
    private FragmentDashboardBinding binding;

    /**
     *  protected void function, Overridden Constructor of a Fragment
     *  Initializes the Fragment, and sets up necessary parameters
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
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
}