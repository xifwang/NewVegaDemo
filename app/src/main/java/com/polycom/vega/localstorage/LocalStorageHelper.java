package com.polycom.vega.localstorage;

/**
 * Created by xwcheng on 9/7/2015.
 */
public class LocalStorageHelper {
    private static String storageName = "com.polycom.vega";

    private static LocalStorageHelper instance;

    public static LocalStorageHelper getInstance() {
        return (instance == null ? instance = new LocalStorageHelper() : instance);
    }

    private LocalStorageHelper() {
    }
}
