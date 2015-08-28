package com.polycom.vega.notificationcenter;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xwcheng on 8/28/2015.
 */
public class NotificationCenter {
    private static NotificationCenter instance;

    public static NotificationCenter getInstance() {
        return (instance == null ? instance = new NotificationCenter() : instance);
    }

    private HashMap<String, String> cyclicNotifications;
    private HashMap<String, String> oneTimeNotifications;

    private NotificationCenter() {
        cyclicNotifications = new HashMap<String, String>();
        timer = new Timer("notificationTimer", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Iterator iterator = cyclicNotifications.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    String restPath = pair.getKey().toString();
                    String messageName = pair.getValue().toString();

                    // TODO: Request from rest.

                    iterator.remove();
                }
            }
        }, 0, 10000);

        oneTimeNotifications = new HashMap<String, String>();
    }

    private Timer timer;

    public String registerNotification(String notificationRestPath, boolean isCyclic) {
        if (TextUtils.isEmpty(notificationRestPath)) {
            return "";
        }

        String messageName = "";

        if (isCyclic) {
            messageName = cyclicNotifications.get(notificationRestPath);
        } else {
            messageName = oneTimeNotifications.get(notificationRestPath);
        }

        if (!TextUtils.isEmpty(messageName)) {
            return messageName;
        }

        messageName = String.format("NotificationMessage%d", cyclicNotifications.size());

        if (isCyclic) {
            cyclicNotifications.put(notificationRestPath, messageName);
        } else {
            oneTimeNotifications.put(notificationRestPath, messageName);
        }

        return messageName;
    }
}
