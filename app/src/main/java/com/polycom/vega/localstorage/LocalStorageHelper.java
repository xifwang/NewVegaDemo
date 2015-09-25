package com.polycom.vega.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by xwcheng on 9/7/2015.
 */
public class LocalStorageHelper {
    private static String storageName = "com.polycom.vega";
    private static String languageKey = "language";

    private static LocalStorageHelper instance;

    private LocalStorageHelper() {
    }

    public static LocalStorageHelper getInstance() {
        return (instance == null ? instance = new LocalStorageHelper() : instance);
    }

    public String getLanguage(Context context) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Context is null.");
        }

        return get(context, languageKey);
    }

    public void saveLanguage(Context context, String language) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Context is null.");
        } else if (TextUtils.isEmpty(language)) {
            throw new IllegalArgumentException("Language name is empty.");
        }

        save(context, languageKey, language);
    }

    public String get(Context context, String key) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Context is null.");
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(storageName, Context.MODE_PRIVATE);

        return (sharedPreferences == null ? null : sharedPreferences.getString(key, null));
    }

    public void save(Context context, String key, String value) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Context is null.");
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
