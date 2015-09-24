package com.polycom.vega.fundamental;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by xwcheng on 9/23/2015.
 */
public class VegaActivity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        ex.printStackTrace();
    }
}
