package com.vogella.myapplication.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;
import com.vogella.myapplication.R;

import java.util.Calendar;

public class MyEventDay extends EventDay implements Parcelable {
        private String note, type;
        Calendar date;


    @Override
    public Calendar getCalendar() {
        return date;
    }
    public MyEventDay(Calendar day, int imageResource,String type, String note) {
            super(day, imageResource);
            this.note = note;
            date = day;
            this.type = type;
        }
        public MyEventDay(Calendar day, String note){
            super(day);
            this.note = note;
        }
        public  String getNote() {
            return note;
        }
        public MyEventDay(Parcel in) {
            super((Calendar) in.readSerializable(), in.readInt());
            note = in.readString();
        }
        public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
            @Override
            public MyEventDay createFromParcel(Parcel in) {
                return new MyEventDay(in);
            }
            @Override
            public MyEventDay[] newArray(int size) {
                return new MyEventDay[size];
            }
        };
        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeSerializable(getCalendar());
            parcel.writeInt(R.drawable.googleg_disabled_color_18);
            parcel.writeString(note);
        }
        @Override
        public int describeContents() {
            return 0;
        }
}
