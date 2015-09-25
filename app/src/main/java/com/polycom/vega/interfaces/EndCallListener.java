package com.polycom.vega.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by xwcheng on 9/25/2015.
 */
public interface EndCallListener {
    void onCallEnded(JSONObject response);

    void onEndCallError(VolleyError error);
}
