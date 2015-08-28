package com.polycom.vega.restobject;

/**
 * Created by xwcheng on 8/13/2015.
 */
public class SystemObject {
    private String build;
    private String buildType;
    private String hardwareVersion;
    private LanStatusObject lanStatus;
    private String model;
    private String rcBatteryCondition;
    private String timeServerState;
    private String serialNumber;
    private String softwareVersion;
    private String state;
    private String systemName;
    private String systemTime;
    private String uptime;
    private String rebootNeeded;
    private String timeOffset;

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public LanStatusObject getLanStatus() {
        return lanStatus;
    }

    public void setLanStatus(LanStatusObject lanStatus) {
        this.lanStatus = lanStatus;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRcBatteryCondition() {
        return rcBatteryCondition;
    }

    public void setRcBatteryCondition(String rcBatteryCondition) {
        this.rcBatteryCondition = rcBatteryCondition;
    }

    public String getTimeServerState() {
        return timeServerState;
    }

    public void setTimeServerState(String timeServerState) {
        this.timeServerState = timeServerState;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getRebootNeeded() {
        return rebootNeeded;
    }

    public void setRebootNeeded(String rebootNeeded) {
        this.rebootNeeded = rebootNeeded;
    }

    public String getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }
}
