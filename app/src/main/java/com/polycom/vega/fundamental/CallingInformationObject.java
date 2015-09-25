package com.polycom.vega.fundamental;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by xwcheng on 9/16/2015.
 */
public class CallingInformationObject extends Object implements Parcelable {
    public static final Creator<CallingInformationObject> CREATOR = new Creator<CallingInformationObject>() {
        public CallingInformationObject createFromParcel(Parcel source) {
            return new CallingInformationObject(source);
        }

        public CallingInformationObject[] newArray(int size) {
            return new CallingInformationObject[size];
        }
    };
    private ContactObject contact;
    private int conferenceIndex;
    private Date startTime;

    public CallingInformationObject() {
    }

    protected CallingInformationObject(Parcel in) {
        this.contact = in.readParcelable(ContactObject.class.getClassLoader());
        this.conferenceIndex = in.readInt();
        long tmpStartTime = in.readLong();
        this.startTime = tmpStartTime == -1 ? null : new Date(tmpStartTime);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.contact, flags);
        dest.writeInt(this.conferenceIndex);
        dest.writeLong(startTime != null ? startTime.getTime() : -1);
    }
}
