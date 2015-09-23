package com.polycom.vega.fundamental;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by xwcheng on 9/15/2015.
 */
public class VegaFragment extends Fragment implements Thread.UncaughtExceptionHandler {
    protected VegaApplication application;
    protected Context context;
    protected View fragment;
    protected FragmentManager fragmentManager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        ex.printStackTrace();
    }
}
