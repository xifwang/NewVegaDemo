package com.polycom.vega.fundamental;

import android.app.Application;

import com.polycom.vega.system.SystemObject;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class VegaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private String serverUrl;
    private SystemObject currentSystemInfo;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        serverUrl = serverUrl;
    }

    public SystemObject getCurrentSystemInfo() {
        return currentSystemInfo;
    }

    public void setCurrentSystemInfo(SystemObject currentSystemInfo) {
        currentSystemInfo = currentSystemInfo;
    }
}