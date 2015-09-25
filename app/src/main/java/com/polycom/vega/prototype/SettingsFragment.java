package com.polycom.vega.prototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.IDataBind;
import com.polycom.vega.interfaces.IView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xwcheng on 8/17/2015.
 */
public class SettingsFragment extends VegaFragment implements IView, IDataBind {
    private Timer timer;
    private TextView titleTextView;
    private TextView errorTextView;
    private BroadcastReceiver broadcastReceiver;
    private HashMap<String, String> eventMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        initComponent();
        initComponentState();
        registerNotification();
        dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {
        this.titleTextView = (TextView) fragment.findViewById(R.id.settingsflagment_titleTextView);
        this.errorTextView = (TextView) fragment.findViewById(R.id.settingfragment_errorTextView);

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
    public void dataBind() {
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

        StringRequest request = new StringRequest(application.getServerUrl() + restName, new Response.Listener<String>() {
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
//                timer.cancel();
                Toast.makeText(getActivity().getApplicationContext(), (error.networkResponse != null ? error.networkResponse.statusCode + "" : error.getMessage()), Toast.LENGTH_SHORT).show();
                RingtoneManager.getRingtone(getActivity().getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).play();
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
