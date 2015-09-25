package com.polycom.vega.prototype;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.IDataBind;
import com.polycom.vega.interfaces.IView;
import com.polycom.vega.restobject.SystemObject;

/**
 * Created by chengw on 9/14/2015.
 */
public class SystemInfoFragment extends VegaFragment implements IView, IDataBind {
    private TextView gsName_textView = null;
    private TextView gsModel_textView = null;
    private TextView gsSwVersion_textView = null;
    private TextView gsSerialNum_textView = null;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        fragment = inflater.inflate(R.layout.fragment_systeminfo, container, false);
        context = fragment.getContext();

        initComponent();
        initComponentState();
        initAnimation();
        dataBind();

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
}
