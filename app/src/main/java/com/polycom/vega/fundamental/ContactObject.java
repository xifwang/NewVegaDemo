package com.polycom.vega.fundamental;

/**
 * Created by xwcheng on 9/11/2015.
 */
public class ContactObject extends Object {
    private String displayName;
    private String destinationIp;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public ContactObject(String displayName, String destinationIp) {
        this.displayName = displayName;
        this.destinationIp = destinationIp;
    }
}
