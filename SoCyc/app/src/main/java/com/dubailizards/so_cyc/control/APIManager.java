package com.dubailizards.so_cyc.control;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dubailizards.so_cyc.boundary.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class APIManager {
    private static APIManager instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    public GoogleMap gMap;

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

    public void FetchData(Context context)
    {
        // Instantiate the RequestQueue.
        String url = "https://geo.data.gov.sg/bicyclerack/2021/07/12/geojson/bicyclerack.geojson";

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, "APIManager->OnResponse()", Toast.LENGTH_SHORT).show();

                        JSONObject geoJsonData = response;
                        GeoJsonLayer layer = new GeoJsonLayer(gMap, geoJsonData);
                        layer.addLayerToMap();

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        this.requestQueue.add(request);

        return;
    }
}

//public class BicycleRackAPI
//{
//    public static void FetchData(Context context)
//    {
//        // Instantiate the RequestQueue.
//        String url = "https://geo.data.gov.sg/bicyclerack/2021/07/12/geojson/bicyclerack.geojson";
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//                        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        APIManager.getInstance(context).addToRequestQueue(stringRequest);
//
//        return;
//    }
//}