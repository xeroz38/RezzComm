package com.rezzcomm.reservation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctors_MainSpecialty implements Parcelable {

	public String specialty_name;
	public String specialty_parent_id;
	public String specialty_id;
	public String specialty_short_name;
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(specialty_name);
		dest.writeString(specialty_parent_id);
		dest.writeString(specialty_id);
		dest.writeString(specialty_short_name);
	}
}
