package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

/**
 * Created by zerocool on 9/22/15.
 */
public class LogFragment extends VegaFragment implements IActivity, IDataBind, Thread.UncaughtExceptionHandler {

    private TextView logTextView;
    private View bottomBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_log, container, false);
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
        logTextView = (TextView) fragment.findViewById(R.id.fragment_log_logTextView);

        bottomBar = fragment.findViewById(R.id.fragment_log_bottombar);

        RadioButton exportRadioButton = (RadioButton) bottomBar.findViewById(R.id
                .bottombar_log_fragment_exportRadioButton);
        exportRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Exporing ...", Toast.LENGTH_SHORT).show();
            }
        });

        RadioButton backRadioButton = (RadioButton) bottomBar.findViewById(R.id
                .bottombar_log_fragment_backRadioButton);
        backRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
    }

    @Override
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

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
