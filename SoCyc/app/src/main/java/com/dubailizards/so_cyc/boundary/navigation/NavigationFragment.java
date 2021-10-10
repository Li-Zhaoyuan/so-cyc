package com.dubailizards.so_cyc.boundary.navigation;

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

import com.dubailizards.so_cyc.databinding.FragmentNavigationBinding;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Navigation Screen
 *  Displays interactive map making use of Google Maps
 */
public class NavigationFragment extends Fragment {

    /**
     *  private NavigationViewModel variable
     *  Object that holds logical code relating to this fragment
     */
    private NavigationViewModel navigationViewModel;

    /**
     *  private FragmentNavigationBinding variable
     *  Auto generated class type that represents the binding between XML layout file and data objects
     */
    private FragmentNavigationBinding binding;

    /**
     *  protected void function, Overridden Constructor of a Fragment
     *  Initializes the Fragment, and sets up necessary parameters
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        navigationViewModel =
                new ViewModelProvider(this).get(NavigationViewModel.class);

        binding = FragmentNavigationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNavigation;
        navigationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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