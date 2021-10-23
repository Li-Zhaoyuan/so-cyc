package com.dubailizards.so_cyc.boundary.navigation;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.SortedList;

import com.android.volley.VolleyError;
import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.control.APIListener;
import com.dubailizards.so_cyc.control.APIManager;
import com.dubailizards.so_cyc.databinding.FragmentNavigationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Navigation Screen
 *  Displays interactive map making use of Google Maps
 */
public class NavigationFragment extends Fragment {

    GoogleMap gMap = null;

    ArrayList<GeoJsonLayer> markersLayers = new ArrayList<GeoJsonLayer>();

    private void CreateLayer(JSONObject obj, float color, boolean displayOnCreate)
    {
        if (gMap == null)
            return;
        GeoJsonLayer layer = new GeoJsonLayer(gMap, obj);
        markersLayers.add(layer);

        GeoJsonPointStyle pointStyle = layer.getDefaultPointStyle();
        pointStyle.setIcon(BitmapDescriptorFactory.defaultMarker(color));

        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                GeoJsonPoint point = (GeoJsonPoint)feature.getGeometry();
                Toast.makeText(getContext(), "Position is: " + point.getCoordinates().toString(), Toast.LENGTH_LONG).show();
            }
        });

        if (displayOnCreate)
            layer.addLayerToMap();
    }
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

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //When map is loaded
                gMap = googleMap;
                APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/bicyclerack/2021/07/12/geojson/bicyclerack.geojson",
                        new APIListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CreateLayer(response, BitmapDescriptorFactory.HUE_BLUE, true);
                            }
                        });
                APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/nationalparks/2020/04/24/geojson/nationalparks.geojson",
                        new APIListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CreateLayer(response, BitmapDescriptorFactory.HUE_GREEN, true);
                            }
                        });

//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
//                    @Override
//                    public void onMapClick(LatLng latLng){
//                        //When clicked on map
//                        //Initialize marker options
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        //Set position of marker
//                        markerOptions.position(latLng);
//                        //Remove all marker
//                        googleMap.clear();
//                        //Animating to zoom the marker
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                latLng,10
//                        ));
//                        //Add marker on map
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
            }
        });

        //Return view
        return view;

//                navigationViewModel =
//                new ViewModelProvider(this).get(NavigationViewModel.class);
//
//      binding = FragmentNavigationBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textNavigation;
//        navigationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
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