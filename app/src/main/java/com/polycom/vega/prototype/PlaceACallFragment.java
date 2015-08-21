package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class PlaceACallFragment extends Fragment implements Thread.UncaughtExceptionHandler, IActivity, IDataBind {
    private LinearLayout fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = (LinearLayout) inflater.inflate(R.layout.fragment_placeacall, container, false);

        Toast.makeText(getActivity().getApplicationContext(), "Here!", Toast.LENGTH_SHORT).show();

        this.initComponent();
        this.dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {

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
