package com.rezzcomm.reservation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Timeslot_list implements Parcelable {
	
	public String start_date;
	public String end_date;
	public String timeslot_id;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(start_date);
		dest.writeString(end_date);
		dest.writeString(timeslot_id);
	}
}