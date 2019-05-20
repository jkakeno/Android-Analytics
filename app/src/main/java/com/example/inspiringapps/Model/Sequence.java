package com.example.inspiringapps.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sequence implements Parcelable {

    String sequence;
    String occurance;

    public Sequence(String sequence, String occurance) {
        this.sequence = sequence;
        this.occurance = occurance;
    }

    protected Sequence(Parcel in) {
        sequence = in.readString();
        occurance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sequence);
        dest.writeString(occurance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sequence> CREATOR = new Creator<Sequence>() {
        @Override
        public Sequence createFromParcel(Parcel in) {
            return new Sequence(in);
        }

        @Override
        public Sequence[] newArray(int size) {
            return new Sequence[size];
        }
    };

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getOccurance() {
        return occurance;
    }

    public void setOccurance(String occurance) {
        this.occurance = occurance;
    }
}
