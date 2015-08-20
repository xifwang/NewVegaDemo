package com.polycom.vega.fundamental;

import com.polycom.vega.system.SystemObject;

/**
 * Created by xwcheng on 8/17/2015.
 */
public class Constants {
    private static String serverUrl;
    private static SystemObject currentSystemInfo;

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        Constants.serverUrl = serverUrl;
    }

    public static SystemObject getCurrentSystemInfo() {
        return currentSystemInfo;
    }

    public static void setCurrentSystemInfo(SystemObject currentSystemInfo) {
        Constants.currentSystemInfo = currentSystemInfo;
    }
}
