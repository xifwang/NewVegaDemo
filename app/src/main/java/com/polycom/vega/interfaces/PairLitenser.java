package com.polycom.vega.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by Michael on 9/24/2015.
 */
public interface PairLitenser {
    void onPaired(String response);

    void onPairError(VolleyError error);
}
