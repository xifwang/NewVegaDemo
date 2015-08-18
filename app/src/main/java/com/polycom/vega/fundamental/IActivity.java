package com.polycom.vega.fundamental;

/**
 * Created by xwcheng on 8/17/2015.
 */
public interface IActivity {
    void initComponent();
    void initComponentState();

    void initAnimation();
    void registerNotification();
}
