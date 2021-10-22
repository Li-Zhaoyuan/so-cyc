package com.dubailizards.so_cyc.control;

import com.android.volley.VolleyError;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONObject;

public interface APIListener {
    public void onResponse(JSONObject response);
}
