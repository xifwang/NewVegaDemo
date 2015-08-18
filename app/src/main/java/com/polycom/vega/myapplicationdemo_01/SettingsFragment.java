package com.polycom.vega.myapplicationdemo_01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.Constants;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xwcheng on 8/17/2015.
 */
public class SettingsFragment extends Fragment implements IActivity, IDataBind {
    private View view;
    private Timer timer;
    private TextView titleTextView;
    private TextView errorTextView;
    private BroadcastReceiver broadcastReceiver;
    private HashMap<String, String> eventMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.initComponent();
        this.initComponentState();
        this.registerNotification();
        this.DataBind();

        return this.view;
    }

    @Override
    public void initComponent() {
        this.titleTextView = (TextView) this.view.findViewById(R.id.settingsflagment_titleTextView);
        this.errorTextView = (TextView) this.view.findViewById(R.id.settingfragment_errorTextView);

        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                titleTextView.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(Long.parseLong(intent.getStringExtra("rest_system_time")))).toString());
            }
        };
    }

    @Override
    public void initComponentState() {
    }

    @Override
    public void initAnimation() {

    }

    @Override
    public void registerNotification() {
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(this.broadcastReceiver, new IntentFilter("rest_system_time"));
    }

    @Override
    public void DataBind() {
        this.eventMap = new HashMap<String, String>();
        this.eventMap.put("/rest/system/time", "rest_system_time");

        TimerTask systemTimerTask = new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<String, String> entry : eventMap.entrySet()) {
                    getRestResponse(entry.getKey(), null, entry.getValue());
                }
            }
        };

        HttpsTrustHelper.allowAllSSL();

        this.timer = new Timer();

        timer.scheduleAtFixedRate(systemTimerTask, 0, 10000);
    }

    public void getRestResponse(String restName, String[] parms, final String broadcastName) {
        if (restName == null || restName.isEmpty()) {
            return;
        }

        StringRequest request = new StringRequest(Constants.getServerUrl() + restName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (broadcastName != null && !broadcastName.isEmpty()) {
                    Intent intent = new Intent();
                    intent.setAction("rest_system_time");
                    intent.putExtra(broadcastName, response);

                    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timer.cancel();
                errorTextView.setText(error.getMessage());
            }
        });

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(this.broadcastReceiver);

        if (this.timer != null) {
            this.timer.cancel();
        }

        super.onDestroy();
    }
}
