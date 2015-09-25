package com.polycom.vega.resthelper;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.interfaces.EndCallListener;
import com.polycom.vega.interfaces.PairLitenser;
import com.polycom.vega.interfaces.PlaceACallListener;
import com.polycom.vega.prototype.HttpsTrustHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by zerocool on 9/23/15.
 */
public class RestHelper {
    private static RestHelper instance;
    private PairLitenser pairLitenser;
    private PlaceACallListener placeACallListener;
    private EndCallListener endCallListener;

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

    public void setPlaceACallListener(PlaceACallListener placeACallListener) {
        this.placeACallListener = placeACallListener;
    }

    public void setEndCallListener(EndCallListener endCallListener) {
        this.endCallListener = endCallListener;
    }

    private String generateUrl(Context context, String restPath) throws Exception {
        String serverUrl = ((VegaApplication) context.getApplicationContext()).getServerUrl();

        if (TextUtils.isEmpty(serverUrl)) {
            throw new Exception("Server url is empty.");
        }

        return String.format("%s%s", serverUrl, restPath);
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
                if (pairLitenser != null) {
                    pairLitenser.onPairError(error);
                }
            }
        });

        Volley.newRequestQueue(context).add(request);
    }

    public void PlaceACall(Context context, JSONObject json) throws Exception {
        if (context == null) {
            throw new IllegalArgumentException("Context is null.");
        } else if (json == null) {
            throw new IllegalArgumentException("Json is null");
        }

        String url = generateUrl(context, RestMap.getInstance().getRestPath("PlaceACall"));

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (placeACallListener != null) {
                    placeACallListener.onCallPlaced(response);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (placeACallListener != null) {
                    placeACallListener.onPlaceACallError(error);
                }
            }
        };

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, json, responseListener, errorListener);

        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    public void EndCall(Context context, int conferenceIndex) throws Exception {
        String url = String.format(generateUrl(context, "/rest/conferences/0/connections/"), conferenceIndex);

        try {
            JSONObject json = new JSONObject("{\"action\":\"hangup\"}");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (endCallListener != null) {
                        endCallListener.onCallEnded(response);
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (endCallListener != null) {
                        endCallListener.onEndCallError(error);
                    }
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, json, responseListener, errorListener);

            Volley.newRequestQueue(context).add(jsonObjectRequest);
        } catch (JSONException e) {
            throw e;
        }
    }
}
