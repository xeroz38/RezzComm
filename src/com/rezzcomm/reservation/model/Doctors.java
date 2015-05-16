package com.rezzcomm.reservation.model;

import java.util.ArrayList;

public class Doctors {
	
	public String doctor_user_id;
	public String doctor_first_name;
	public String doctor_last_name;
	public String doctor_telp_no;
	public String doctor_gender;
	public String doctor_professional_statement;
	public String doctor_profile_image;
	public String doctor_message;

	public String place_address_line1;
	public String place_address_line2;
	public String place_address_line3;
	public String place_address_line4;
	public String place_city;
	public String place_country;
	public String place_description;
	public String place_fax_no;
	public String place_latitude;
	public String place_longitude;
	public String place_name;
	public String place_id;
	public String place_type;
	public String place_postal_code;
	public String place_profile_image;
	public String place_state;
	public String place_tel_no;
	
	public String review_comments;
	public String review_date;
	public String review_wait_time;
	public String review_helpfull;
	public String review_id;
	public String review_overall;

	public ArrayList<Doctors_MainSpecialty> main_specialty;
	public ArrayList<Doctors_Education> education;
	public ArrayList<Doctors_Fellowship> fellowship;
	public ArrayList<Doctors_Image> image;
	public ArrayList<Doctors_Language> language;
	public ArrayList<Doctors_Specialty> specialty;
	public ArrayList<Doctors_Timeslot> timeslot;

	public Doctors() {
		main_specialty = new ArrayList<Doctors_MainSpecialty>();
		education = new ArrayList<Doctors_Education>();
		fellowship = new ArrayList<Doctors_Fellowship>();
		image = new ArrayList<Doctors_Image>();
		language = new ArrayList<Doctors_Language>();
		specialty = new ArrayList<Doctors_Specialty>();
		timeslot = new ArrayList<Doctors_Timeslot>();
	}
}
