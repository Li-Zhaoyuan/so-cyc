package com.dubailizards.so_cyc.control;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SortedList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dubailizards.so_cyc.boundary.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

//https://geo.data.gov.sg/hawkercentre/2021/09/01/geojson/hawkercentre.geojson

public class APIManager {
    private static APIManager instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private APIManager(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized APIManager getInstance(Context context) {
        if (instance == null) {
            instance = new APIManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void AddLayerFromURL(Context context, ArrayList<GeoJsonLayer> list, GoogleMap gMap, String url)
    {
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject geoJsonData = response;
                        GeoJsonLayer layer = new GeoJsonLayer(gMap, geoJsonData);
                        list.add(layer);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error occured fetching from " + url, Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        this.requestQueue.add(request);

        return;
    }

    public void RequestJSONObject(Context context, String url, final APIListener listener)
    {
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
        });

        // Add the request to the RequestQueue.
        this.requestQueue.add(request);

        return;
    }

//    public void FetchData(Context context)
//    {
//        // Instantiate the RequestQueue.
//        String url = "https://geo.data.gov.sg/bicyclerack/2021/07/12/geojson/bicyclerack.geojson";
//
//        // Request a string response from the provided URL.
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Display the first 500 characters of the response string.
//
//                        JSONObject geoJsonData = response;
//                        GeoJsonLayer layer = new GeoJsonLayer(gMap, geoJsonData);
//                        layer.addLayerToMap();
//
//                        GeoJsonPointStyle pointStyle = layer.getDefaultPointStyle();
//                        //pointStyle.setIcon();
//                        pointStyle.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//                        pointStyle.setTitle("Bicycle Rack");
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        this.requestQueue.add(request);
//
//        return;
//    }
}