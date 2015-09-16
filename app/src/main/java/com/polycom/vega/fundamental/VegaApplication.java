package com.polycom.vega.fundamental;

import android.app.Application;

import com.polycom.vega.restobject.SystemObject;

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
    private CallingInformationObject callingInfo;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public SystemObject getCurrentSystemInfo() {
        return currentSystemInfo;
    }

    public void setCurrentSystemInfo(SystemObject currentSystemInfo) {
        this.currentSystemInfo = currentSystemInfo;
    }

    public CallingInformationObject getCallingInfo() {
        return callingInfo;
    }

    public void setCallingInfo(CallingInformationObject callingInfo) {
        this.callingInfo = callingInfo;
    }
}
