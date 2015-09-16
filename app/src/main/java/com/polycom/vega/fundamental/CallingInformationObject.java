package com.polycom.vega.fundamental;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by xwcheng on 9/16/2015.
 */
public class CallingInformationObject extends Object implements Parcelable {
    private ContactObject contact;
    private int conferenceIndex;
    private String destinationFullUrl;
    private Date startTime;

    public ContactObject getContact() {
        return contact;
    }

    public void setContact(ContactObject contact) {
        this.contact = contact;
    }

    public int getConferenceIndex() {
        return conferenceIndex;
    }

    public void setConferenceIndex(int conferenceIndex) {
        this.conferenceIndex = conferenceIndex;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public CallingInformationObject() {
    }

    public String getDestinationFullUrl() {
        return destinationFullUrl;
    }

    public void setDestinationFullUrl(String destinationFullUrl) {
        this.destinationFullUrl = destinationFullUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.contact, flags);
        dest.writeInt(this.conferenceIndex);
        dest.writeString(this.destinationFullUrl);
        dest.writeLong(startTime != null ? startTime.getTime() : -1);
    }

    protected CallingInformationObject(Parcel in) {
        this.contact = in.readParcelable(ContactObject.class.getClassLoader());
        this.conferenceIndex = in.readInt();
        this.destinationFullUrl = in.readString();
        long tmpStartTime = in.readLong();
        this.startTime = tmpStartTime == -1 ? null : new Date(tmpStartTime);
    }

    public static final Creator<CallingInformationObject> CREATOR = new Creator<CallingInformationObject>() {
        public CallingInformationObject createFromParcel(Parcel source) {
            return new CallingInformationObject(source);
        }

        public CallingInformationObject[] newArray(int size) {
            return new CallingInformationObject[size];
        }
    };
}
