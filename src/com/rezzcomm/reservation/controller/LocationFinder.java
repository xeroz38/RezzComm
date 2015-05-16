package com.rezzcomm.reservation.controller;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class LocationFinder {

	public Geocoder geocoder;
	public String bestProvider, address;

	public List<Address> user = null;
	public double latitude;
	public double longitude;

	public LocationFinder(Context context) {

		if(context != null) {
			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			bestProvider = lm.getBestProvider(criteria, false);
			Location location = lm.getLastKnownLocation(bestProvider);

			if (location != null) {
				geocoder = new Geocoder(context);
				try {
					user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					latitude=(double)user.get(0).getLatitude();
					longitude=(double)user.get(0).getLongitude();

					address = "";
					if (user.size() > 0) {
						for (int index = 0; index < user.get(0).getMaxAddressLineIndex(); index++)
							address += user.get(0).getAddressLine(index) + " ";
					}
					System.out.println("Coordinate = " + String.valueOf(latitude + "/" + longitude + "/" +address));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
