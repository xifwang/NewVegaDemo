package com.polycom.vega.resthelper;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.interfaces.PairLitenser;
import com.polycom.vega.prototype.HttpsTrustHelper;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by zerocool on 9/23/15.
 */
public class RestHelper {
    private static RestHelper instance;
    private PairLitenser pairLitenser;

    private RestHelper() {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        try {
            HttpsTrustHelper.allowAllSSL();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static RestHelper getInstance() {
        return (instance == null ? instance = new RestHelper() : instance);
    }

    public void setPairLitenser(PairLitenser pairLitenser) {
        this.pairLitenser = pairLitenser;
    }

    public void Pair(final Context context, String url) throws IllegalArgumentException {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Pairing URL is empty.");
        }

        if (!url.startsWith("https://")) {
            url = "https://" + url;
        }

        final String finalUrl = url;
        StringRequest request = new StringRequest(url + "/rest/system", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((VegaApplication) context.getApplicationContext()).setServerUrl(finalUrl);

                if (pairLitenser != null) {
                    pairLitenser.onPaired(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, (error.networkResponse != null ? error.networkResponse.statusCode + "" : error.getMessage()), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(context).add(request);
    }
}
