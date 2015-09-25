package com.polycom.vega.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Michael on 9/24/2015.
 */
public interface PlaceACallListener {
    void onCallPlaced(JSONObject response);

    void onPlaceACallError(VolleyError error);
}
