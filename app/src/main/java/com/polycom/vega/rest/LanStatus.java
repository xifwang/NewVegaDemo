package com.polycom.vega.rest;

/**
 * Created by xwcheng on 8/13/2015.
 */
public class LanStatus {
    private String duplex;
    private String state;
    private String speedMbps;

    public String getDuplex() {
        return duplex;
    }

    public void setDuplex(String duplex) {
        this.duplex = duplex;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSpeedMbps() {
        return speedMbps;
    }

    public void setSpeedMbps(String speedMbps) {
        this.speedMbps = speedMbps;
    }
}
