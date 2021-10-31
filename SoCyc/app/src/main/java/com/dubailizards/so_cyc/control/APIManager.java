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
import com.dubailizards.so_cyc.boundary.navigation.directionhelpers.TaskLoadedCallback;
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
    /**
     * a private APIManager variable.
     * Used to hold a single instance of this class for Singleton architecture
     */
    private static APIManager instance;
    /**
     * a private RequestQueue variable.
     * Holds all requests in FIFO pattern
     * Take note all requests added into this queue will automatically be executed asynchronous
     */
    private RequestQueue requestQueue;
    private static Context ctx;

    /**
     * a private APIManager constructor.
     * Ensures this class can not be created elsewhere to maintain Singleton architecture
     */
    private APIManager(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * a public static APIManager get function.
     * provides the only way to access this APIManager class, to maintain Singleton architecture
     * @param context takes in the context required for common functions in Android Studio
     */
    public static synchronized APIManager getInstance(Context context) {
        if (instance == null) {
            instance = new APIManager(context);
        }
        return instance;
    }

    /**
     * a public requestQueue get function.
     * returns the request queue held by this class
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * a public <T> void add to queue function.
     * adds request into queue for sequential requests
     * @param req a Request template to different types of objects to be added. This imitates function overloading
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * a public void request function
     * adds a JSONObject request from url into requestQueue and indicate the listener function to run when done
     * Take note that the actual request is asynchronous and will be executed automatically after being added into requestQueue
     * @param context context required for many common functions in Android Studio
     * @param url string of url for the JSONObject to be requested from
     * @param listener an interface containing the function to run after request is completed
     */
    public void RequestJSONObject(Context context, String url, final TaskLoadedCallback listener)
    {
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onTaskDone(response);
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
}