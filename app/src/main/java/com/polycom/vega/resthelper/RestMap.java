package com.polycom.vega.resthelper;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Michael on 9/24/2015.
 */
public class RestMap {
    private static RestMap instance;
    private HashMap<String, String> map;

    private RestMap() {
        map = new HashMap<String, String>();
        map.put("PlaceACall", "/rest/conferences?_dc=1439978043968");
        map.put("EndCall_Format", "/rest/conferences/0/connections/%d");
    }

    public static RestMap getInstance() {
        return (instance == null ? instance = new RestMap() : instance);
    }

    public String getRestPath(String key) throws IllegalArgumentException {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key is empty.");
        }

        return map.get(key);
    }
}
