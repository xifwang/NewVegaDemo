package com.polycom.vega.fundamental;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xwcheng on 9/11/2015.
 */
public class ContactObject extends Object implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.destinationIp);
    }

    protected ContactObject(Parcel in) {
        this.displayName = in.readString();
        this.destinationIp = in.readString();
    }

    public static final Creator<ContactObject> CREATOR = new Creator<ContactObject>() {
        public ContactObject createFromParcel(Parcel source) {
            return new ContactObject(source);
        }

        public ContactObject[] newArray(int size) {
            return new ContactObject[size];
        }
    };
}
