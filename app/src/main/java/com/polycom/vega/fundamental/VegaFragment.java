package com.polycom.vega.fundamental;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
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

    public void toast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        } else if (context == null) {
            context = fragment.getContext();
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void handleException(Exception ex) {
        if (ex == null) {
            return;
        }

        toast(ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        ex.printStackTrace();
    }
}
