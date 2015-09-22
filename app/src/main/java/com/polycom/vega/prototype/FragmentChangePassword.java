package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

/**
 * Created by xwcheng on 9/21/2015.
 */
public class FragmentChangePassword extends VegaFragment implements IActivity, Thread.UncaughtExceptionHandler {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_changepassword, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        Thread.currentThread().setUncaughtExceptionHandler(this);

        initComponent();
        initComponentState();
        initAnimation();

        return fragment;
    }

    @Override
    public void initComponent() {
        currentPasswordEditText = (EditText) fragment.findViewById(R.id.fragment_changepassword_currentPasswordEditText);
        newPasswordEditText = (EditText) fragment.findViewById(R.id.fragment_changepassword_newPasswordEditText);
        confirmNewPasswordEditText = (EditText) fragment.findViewById(R.id.fragment_changepassword_confirmNewPasswordEditText);
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
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
