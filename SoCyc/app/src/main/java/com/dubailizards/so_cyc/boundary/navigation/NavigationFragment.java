package com.dubailizards.so_cyc.boundary.navigation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.navigation.directionhelpers.FetchURL;
import com.dubailizards.so_cyc.boundary.navigation.directionhelpers.TaskLoadedCallback;
import com.dubailizards.so_cyc.control.APIManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.collections.GroundOverlayManager;
import com.google.maps.android.collections.MarkerManager;
import com.google.maps.android.collections.PolygonManager;
import com.google.maps.android.collections.PolylineManager;
import com.google.maps.android.data.Feature;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONObject;

import java.util.HashMap;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Navigation Screen
 *  Displays interactive map making use of Google Maps
 */
public class NavigationFragment extends Fragment {

    /**
     * a private enum variable.
     * Represents the different types of layers the map can have
     */
    private enum LAYERTYPE{
        layer_BikeRack,
        layer_NationalPark,
        layer_HawkerCentre,

        layer_num
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;
    /**
     * a private Polyline variable.
     * Holds latest path found, decided by user when markers are tapped on.
     */
    private Polyline currentPolyline;

    private ImageButton btnPark;
    private ImageButton btnBike;
    private ImageButton btnHawker;
    private Button btnRemoveLine;

    private GoogleMap gMap = null;
    private MarkerManager markerManager;
    private GroundOverlayManager groundOverlayManager;
    private PolygonManager polygonManager;
    private PolylineManager polylineManager;
    private LatLng userLocation;
    private LatLng destination;

    /**
     * a private HashMap<LAYERTYPE, GeoJsonLayer> variable.
     * Holds all markers data to be used to overlay the google map
     */
    private HashMap<LAYERTYPE, GeoJsonLayer> markersLayers = new HashMap<LAYERTYPE, GeoJsonLayer>();
    /**
     * a private TaskLoadedCallback variable.
     * Used to store the method used to draw path found on google map. This variable will be used as parameter passed into async functions to be called afterwards.
     */
    private TaskLoadedCallback pathFoundCallback = new TaskLoadedCallback() {
        @Override
        public void onTaskDone(Object... values) {
            if (currentPolyline != null)
                currentPolyline.remove();
            currentPolyline = gMap.addPolyline((PolylineOptions) values[0]);
            currentPolyline.setColor(getContext().getResources().getColor(R.color.red_a400));
        }
    };

    /**
     *  protected void function, Overridden Constructor of a Fragment
     *  Initializes the Fragment, and sets up necessary parameters
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Initialize view
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        //Check Permission
        checkPermission();

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //When map is loaded
                gMap = googleMap;
                markerManager = new MarkerManager(gMap);
                groundOverlayManager = new GroundOverlayManager(gMap);
                polygonManager = new PolygonManager(gMap);
                polylineManager = new PolylineManager(gMap);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                gMap.setMyLocationEnabled(true);
                gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    //Function will be called when user's location changed
                    @Override
                    public void onMyLocationChange(Location location) {
                        //Get user's new location
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        //If destination is set to null means that user have not choose a marker to navigate to
                        //thus no need for the calling of FetchURL function to be called sine no update to route
                        if (destination != null) {
                            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo nInfo = cm.getActiveNetworkInfo();
                            boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                            if(connected == false) {
                                Toast.makeText(getContext(), "No internet connection detected", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String url = getUrl(userLocation, destination, "walking");
                            new FetchURL(pathFoundCallback).execute(url, "walking");
                        }

                        //Map displayed on phone to follow user's new location
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                userLocation, 15);
                        gMap.animateCamera(cameraUpdate);

                    }
                });
                APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/bicyclerack/2021/07/12/geojson/bicyclerack.geojson",
                        new TaskLoadedCallback() {
                            @Override
                            public void onTaskDone(Object... values) {
                                CreateLayer((JSONObject)values[0], LAYERTYPE.layer_BikeRack, BitmapDescriptorFactory.HUE_YELLOW, true);
                            }
                        });
                APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/nationalparks/2020/04/24/geojson/nationalparks.geojson",
                        new TaskLoadedCallback() {
                            @Override
                            public void onTaskDone(Object... values) {
                                CreateLayer((JSONObject)values[0], LAYERTYPE.layer_NationalPark, BitmapDescriptorFactory.HUE_GREEN, false);
                            }
                        });
                APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/hawkercentre/2021/09/01/geojson/hawkercentre.geojson",
                        new TaskLoadedCallback() {
                            @Override
                            public void onTaskDone(Object... values) {
                                CreateLayer((JSONObject)values[0], LAYERTYPE.layer_HawkerCentre, BitmapDescriptorFactory.HUE_CYAN, false);
                            }
                        });
            }
        });

        btnRemoveLine = view.findViewById(R.id.remove_line);

        btnBike = (ImageButton) view.findViewById(R.id.toggle_bike);;
        btnPark = (ImageButton) view.findViewById(R.id.toggle_park);;
        btnHawker = (ImageButton) view.findViewById(R.id.toggle_hawker);;

        btnBike.setBackgroundColor(Color.YELLOW);

        btnBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markersLayers.containsKey(LAYERTYPE.layer_BikeRack))
                {
                    if (markersLayers.get(LAYERTYPE.layer_BikeRack).isLayerOnMap()) {
                        btnBike.setBackgroundColor(Color.WHITE);
                        markersLayers.get(LAYERTYPE.layer_BikeRack).removeLayerFromMap();
                    } else {
                        btnBike.setBackgroundColor(Color.YELLOW);
                        markersLayers.get(LAYERTYPE.layer_BikeRack).addLayerToMap();
                    }
                }
                else
                {
                    APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                        "https://geo.data.gov.sg/bicyclerack/2021/07/12/geojson/bicyclerack.geojson",
                            new TaskLoadedCallback() {
                                @Override
                                public void onTaskDone(Object... values) {
                                    CreateLayer((JSONObject)values[0], LAYERTYPE.layer_BikeRack, BitmapDescriptorFactory.HUE_YELLOW, true);
                            }
                        });
                }
            }
        });

         btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markersLayers.containsKey(LAYERTYPE.layer_NationalPark))
                {
                    if (markersLayers.get(LAYERTYPE.layer_NationalPark).isLayerOnMap()) {
                        btnPark.setBackgroundColor(Color.WHITE);
                        markersLayers.get(LAYERTYPE.layer_NationalPark).removeLayerFromMap();
                    } else {
                        btnPark.setBackgroundColor(Color.GREEN);
                        markersLayers.get(LAYERTYPE.layer_NationalPark).addLayerToMap();
                    }
                }
                else
                {
                    APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                            "https://geo.data.gov.sg/nationalparks/2020/04/24/geojson/nationalparks.geojson",
                            new TaskLoadedCallback() {
                                @Override
                                public void onTaskDone(Object... values) {
                                    CreateLayer((JSONObject)values[0], LAYERTYPE.layer_NationalPark, BitmapDescriptorFactory.HUE_GREEN, false);
                                }
                            });
                }
            }
        });

        btnHawker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markersLayers.containsKey(LAYERTYPE.layer_HawkerCentre))
                {
                    if(markersLayers.get(LAYERTYPE.layer_HawkerCentre).isLayerOnMap())
                    {
                        btnHawker.setBackgroundColor(Color.WHITE);
                        markersLayers.get(LAYERTYPE.layer_HawkerCentre).removeLayerFromMap();
                    }
                    else
                    {
                        btnHawker.setBackgroundColor(getContext().getResources().getColor(R.color.teal_200));
                        markersLayers.get(LAYERTYPE.layer_HawkerCentre).addLayerToMap();
                    }
                }
                else
                {
                    APIManager.getInstance(getActivity()).RequestJSONObject(getActivity(),
                            "https://geo.data.gov.sg/hawkercentre/2021/09/01/geojson/hawkercentre.geojson",
                            new TaskLoadedCallback() {
                                @Override
                                public void onTaskDone(Object... values) {
                                    CreateLayer((JSONObject)values[0], LAYERTYPE.layer_HawkerCentre, BitmapDescriptorFactory.HUE_CYAN, false);
                                }
                            });
                }
            }
        });

        //Function to remove route (if it exists) from google map upon "Clear Path" button being clicked
        btnRemoveLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPolyline != null)
                    currentPolyline.remove();
                    destination = null;
            }
        });



        //Return view
        return view;
    }
    /**
     * a private void function, a common set of instruction created to reduce repititions in this class
     * Adds data in the form of a HashMap<LAYERTYPE, GeoJsonLayer> to the markersLayers local variable
     * @param obj contains GeoJsonLayer data to be added into markersLayers
     * @param layerType represents the key for the obj added to the markersLayers
     * @param color is the desired color for markers contained in obj
     * @param displayOnCreate flags if current markersLayer should be added as shown or hidden
     */
    private void CreateLayer(JSONObject obj, LAYERTYPE layerType, float color, boolean displayOnCreate)
    {
        if (gMap == null)
            return;
        if (markersLayers.containsKey(layerType))
            return;

        GeoJsonLayer layer = new GeoJsonLayer(gMap, obj, markerManager, polygonManager, polylineManager, groundOverlayManager);
        markersLayers.put(layerType, layer);

        GeoJsonPointStyle pointStyle = layer.getDefaultPointStyle();
        pointStyle.setIcon(BitmapDescriptorFactory.defaultMarker(color));

        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if(connected == false) {
                    Toast.makeText(getContext(), "No internet connection detected", Toast.LENGTH_SHORT).show();
                    return;
                }

                GeoJsonPoint point = (GeoJsonPoint)feature.getGeometry();

                if (gMap.getMyLocation() != null)
                {
                    destination = point.getCoordinates();
                    String url = getUrl(userLocation, destination, "walking");
                    new FetchURL(pathFoundCallback).execute(url, "walking");
                }
                else
                {
                    Toast.makeText(getContext(), "Unable to find your location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (displayOnCreate)
            layer.addLayerToMap();
    }

    /**
     * a private void function, used to ensure location permission is allowed
     * will check if location permission is allowed, if not request user for permission
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    /**
     * a private string function, returns a string to be used for URL reqeust
     * constructs a URL for specific requests by appending paramters into required URL format
     * @param origin user position in LatLng
     * @param dest user destination in LatLng
     * @param directionMode determines what kind route to connect origin and dest, inputs include "WALKING", "DRIVING", etc.
     */
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



    /**
     *  protected void function, Overridden Destructor of a Fragment
     *  Terminates the Fragment, deallocating variables accordingly
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gMap = null;
    }
}