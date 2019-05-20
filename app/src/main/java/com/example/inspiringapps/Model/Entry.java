package com.example.inspiringapps.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry implements Parcelable {

    String ipAddress;
    String webPage;

    public Entry(String ipAddress, String webPage){

        this.ipAddress = ipAddress;
        this.webPage = webPage;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    protected Entry(Parcel in) {
        ipAddress = in.readString();
        webPage = in.readString();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ipAddress);
        dest.writeString(webPage);
    }
}
