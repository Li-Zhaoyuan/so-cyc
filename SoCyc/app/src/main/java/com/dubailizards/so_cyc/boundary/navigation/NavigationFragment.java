package com.dubailizards.so_cyc.boundary.navigation;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.SortedList;

import com.android.volley.VolleyError;
import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.BaseActivity;
import com.dubailizards.so_cyc.boundary.MainActivity;
import com.dubailizards.so_cyc.boundary.navigation.directionhelpers.FetchURL;
import com.dubailizards.so_cyc.boundary.navigation.directionhelpers.TaskLoadedCallback;
import com.dubailizards.so_cyc.control.APIListener;
import com.dubailizards.so_cyc.control.APIManager;
import com.dubailizards.so_cyc.databinding.FragmentNavigationBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.errorprone.annotations.ForOverride;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Navigation Screen
 *  Displays interactive map making use of Google Maps
 */
public class NavigationFragment extends Fragment implements TaskLoadedCallback {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;

    private Button btnGetDirection;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    GoogleMap gMap = null;

    ArrayList<GeoJsonLayer> markersLayers = new ArrayList<GeoJsonLayer>();

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
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        //Assign Button
        btnGetDirection = view.findViewById(R.id.btnGetDirection);

        //Check Permission
        checkPermission();

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //When map is loaded
                gMap = googleMap;
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                gMap.setMyLocationEnabled(true);
                gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                latlng, 100);
                        gMap.animateCamera(cameraUpdate);

                    }
                });
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
                APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/hawkercentre/2021/09/01/geojson/hawkercentre.geojson",
                        new APIListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CreateLayer(response, BitmapDescriptorFactory.HUE_ORANGE, true);
                            }
                        });
            }
        });

        //1.3490,103.8391
        //1.3522,103.8372
        place1 = new MarkerOptions().position(new LatLng(1.3490, 103.8391)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(1.3522, 103.8372)).title("Location 2");


        btnGetDirection.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
               new FetchURL(getActivity()).execute(url, "driving");
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
    }

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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAA1Vc_cXpp-FWbiH52ZxlUy2F-zt8f5CA";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = gMap.addPolyline((PolylineOptions) values[0]);
    }

    /**
     *  protected void function, Overridden Destructor of a Fragment
     *  Terminates the Fragment, deallocating variables accordingly
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        gMap = null;
    }
}