package com.polycom.vega.resthelper;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by zerocool on 9/23/15.
 */
public class RestHelper {
    private static RestHelper instance;

    private RestHelper() {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    public static RestHelper getInstance() {
        return (instance == null ? instance = new RestHelper() : instance);
    }
}
