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

import com.android.volley.VolleyError;
import com.polycom.vega.fundamental.CallingInformationObject;
import com.polycom.vega.fundamental.ExceptionHandler;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.EndCallListener;
import com.polycom.vega.interfaces.IDataBind;
import com.polycom.vega.interfaces.IView;
import com.polycom.vega.resthelper.RestHelper;

import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xwcheng on 9/15/2015.
 */
public class InCallFragment extends VegaFragment implements IView, IDataBind, EndCallListener {
    private TextView consumingTimeTextView;
    private TextView usernameTextView;
    private CallingInformationObject callingInfo;
    private View.OnClickListener endCallImageButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                endCall();
            } catch (Exception e) {
                ExceptionHandler.getInstance().handle(context, e);
            }
        }
    };
    private View.OnClickListener backImageButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        fragment = inflater.inflate(R.layout.fragment_incall, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

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

    private void endCall() throws Exception {
        RestHelper.getInstance().setEndCallListener(this);
        RestHelper.getInstance().EndCall(context, callingInfo.getConferenceIndex());
    }

    @Override
    public void onCallEnded(JSONObject response) {

    }

    @Override
    public void onEndCallError(VolleyError error) {
        Toast.makeText(context, "Call has been ended.", Toast.LENGTH_SHORT).show();
        application.setCallingInfo(null);
        fragmentManager.popBackStack();
    }
}
