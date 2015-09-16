package com.polycom.vega.prototype;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.restobject.SystemObject;

/**
 * Created by chengw on 9/14/2015.
 */
public class SystemInfoFragment extends Fragment implements IActivity, IDataBind, Thread.UncaughtExceptionHandler {
    private View fragment;
    private Context context;

    private TextView gsName_textView = null;
    private TextView gsModel_textView = null;
    private TextView gsSwVersion_textView = null;
    private TextView gsSerialNum_textView = null;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            fragment = inflater.inflate(R.layout.fragment_systeminfo, container, false);
            context = fragment.getContext();

            initComponent();
            initComponentState();
            initAnimation();
            dataBind();
        } catch (Exception ex) {
            Toast.makeText(getView().getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return fragment;
    }

    @Override
    public void initComponent() {
        this.gsName_textView = (TextView) fragment.findViewById(R.id.sysInfo_gsName);
        this.gsModel_textView = (TextView) fragment.findViewById(R.id.sysInfo_model);
        this.gsSerialNum_textView = (TextView) fragment.findViewById(R.id.sysInfo_serialNumber);
        this.gsSwVersion_textView = (TextView) fragment.findViewById(R.id.sysInfo_swVersion);

/*
        this.gsName_textView = (TextView) findViewById(R.id.sysInfo_gsName);
        this.gsModel_textView = (TextView) findView
*/
        //Bundle getArguments()
        String strSysInfo = (String) getActivity().getIntent().getExtras().get("response");
        SystemObject systemInfo = JSON.parseObject(strSysInfo, SystemObject.class);

        Toast.makeText(context, strSysInfo != null ? strSysInfo.toString() : null, Toast.LENGTH_SHORT).show();

        populateSysInfoView(systemInfo);
    }

    private void populateSysInfoView(SystemObject sysInfo) {
        gsName_textView.setText(sysInfo.getSystemName());
        gsModel_textView.setText(sysInfo.getModel());
        gsSwVersion_textView.setText(sysInfo.getSoftwareVersion());
        gsSerialNum_textView.setText(sysInfo.getSerialNumber());

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

    }
}
