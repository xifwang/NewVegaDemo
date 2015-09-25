package com.polycom.vega.fundamental;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Michael on 9/24/2015.
 */
public class ExceptionHandler {
    private static ExceptionHandler exceptionHandler;

    public static ExceptionHandler getInstance() {
        return (exceptionHandler == null ? exceptionHandler = new ExceptionHandler() : exceptionHandler);
    }

    public void handle(Context context, Exception ex) {
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        ex.printStackTrace();
    }
}
