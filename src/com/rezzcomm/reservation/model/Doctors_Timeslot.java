package com.rezzcomm.reservation.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Doctors_Timeslot implements Serializable {

	public String timeslot_date;
	public ArrayList<Timeslot_list> timeslot_list;
	
	public Doctors_Timeslot() {
		timeslot_list = new ArrayList<Timeslot_list>();
	}
}
