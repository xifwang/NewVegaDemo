package com.polycom.vega.prototype;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.CallingInformationObject;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xwcheng on 9/15/2015.
 */
public class InCallFragment extends VegaFragment implements IActivity, Thread.UncaughtExceptionHandler, IDataBind {
    private TextView consumingTimeTextView;
    private TextView usernameTextView;
    private CallingInformationObject callingInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_incall, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        getActivity().findViewById(R.id.activity_main_incall_header).setVisibility(View.GONE);

        Thread.currentThread().setUncaughtExceptionHandler(this);

        initComponent();
        initComponentState();
        initAnimation();
        dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {
        View header = fragment.findViewById(R.id.fragment_incall_header);
        consumingTimeTextView = (TextView) header.findViewById(R.id.header_option_item_layout_options_titleTextView);

        fragmentManager.beginTransaction().replace(R.id.fragment_incall_contentViewLayout, new LocalGsControlFragment()).commit();

        ImageButton backImageButton = (ImageButton) header.findViewById(R.id.header_option_item_layout_back_icon_imageButton);
        backImageButton.setOnClickListener(backImageButton_OnClickListener);

        usernameTextView = (TextView) fragment.findViewById(R.id.fragment_incall_userNameTextView);

        ImageButton endCallImageButton = (ImageButton) fragment.findViewById(R.id.fragment_incall_endCallImageButton);
        endCallImageButton.setOnClickListener(endCallImageButton_OnClickListener);
    }

    private View.OnClickListener endCallImageButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            endCall();
        }
    };

    private View.OnClickListener backImageButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    public void initComponentState() {
    }

    @Override
    public void initAnimation() {
    }

    @Override
    public void registerNotification() {

    }

    @Override
    public void dataBind() {
        callingInfo = application.getCallingInfo();

        // Description: Open this fragment as a new call.
        if (callingInfo == null) {
            callingInfo = getArguments().getParcelable("callingInfo");
            application.setCallingInfo(callingInfo);
        }

        if (callingInfo != null) {
            usernameTextView.setText(callingInfo.getContact().getDisplayName());

            try {
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        long totalSeconds = (new Date().getTime() - callingInfo.getStartTime().getTime()) / 1000;
                        long hours = totalSeconds / 3600;
                        long minutes = (totalSeconds - hours * 3600) / 60;
                        long seconds = totalSeconds - hours * 3600 - minutes * 60;

                        consumingTimeTextView.setText(String.format("%d : %d : %d", hours, minutes, seconds));
                    }
                };

                new Timer(true).scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                }, 0, 1000);
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void endCall() {
        if (callingInfo == null) {
            return;
        }

        String url = application.getServerUrl() + "/rest/conferences/0/connections/" + callingInfo.getConferenceIndex();

        try {
            JSONObject json = new JSONObject("{\"action\":\"hangup\"}");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Call has been ended.", Toast.LENGTH_SHORT).show();
                    application.setCallingInfo(null);
                    fragmentManager.popBackStack();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, json, responseListener, errorListener);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
