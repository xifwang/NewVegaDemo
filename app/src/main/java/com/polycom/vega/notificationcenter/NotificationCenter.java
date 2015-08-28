package com.polycom.vega.notificationcenter;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by xwcheng on 8/28/2015.
 */
public class NotificationCenter {
    private static NotificationCenter instance;

    public static NotificationCenter getInstance() {
        return (instance == null ? instance = new NotificationCenter() : instance);
    }

    private HashMap<String, String> notifications;

    private NotificationCenter() {
        notifications = new HashMap<String, String>();
    }

    public String registerNotification(String notificationRestPath) {
        if (TextUtils.isEmpty(notificationRestPath)) {
            return "";
        }

        String messageName = notifications.get(notificationRestPath);

        if (!TextUtils.isEmpty(messageName)) {
            return messageName;
        }

        messageName = String.format("NotificationMessage%d", notifications.size());

        notifications.put(notificationRestPath, messageName);

        return messageName;
    }
}
