package com.polycom.vega.localstorage;

import android.content.Context;
import android.content.SharedPreferences;

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

    public String get(Context context, String key) throws Exception {
        if (context == null) {
            throw new Exception("Context is null.");
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(storageName, Context.MODE_PRIVATE);

        return (sharedPreferences == null ? null : sharedPreferences.getString(key, null));
    }

    public void save(Context context, String key, String value) throws Exception {
        if (context == null) {
            throw new Exception("Context is null.");
        }

        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(storageName, Context.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
