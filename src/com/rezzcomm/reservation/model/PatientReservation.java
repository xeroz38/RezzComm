package com.rezzcomm.reservation.model;

import java.util.ArrayList;

public class PatientReservation {
	
	public String first_time;
	public String reservation_date;
	public String for_;
	public String peer_name;
	public String peer_tel_no;
	public String peer_email;

	public String reservation_name;
	public String reservation_state;
	public String reservation_country;
	public String reservation_description;
	public String reservation_city;
	public String reservation_place_facility_list;
	public String reservation_place_image_list;
	public String reservation_place_id;
	public String reservation_address_line1;
	public String reservation_postal_code;
	public String reservation_tel_no;
	public String reservation_profile_image;
	public String reservation_address_line2;
	public String reservation_address_line3;
	public String reservation_address_line4;
	public String reservation_fax_no;

	public String reservation_specialty_name;
	public String reservation_specialty_id;
	
	public String reservation_id;
	public String reservation_duration_min;
	public String reservation_end_date;
	public String reservation_start_date;
	public String reservation_status;
	public String reservation_timeslot_id;

	public String doctor_id;
	public String doctor_first_name;
	public String doctor_user_id;
	public String doctor_last_name;
	public String doctor_email_address;
	public String doctor_telp_no;
	public String doctor_gender;
	public String doctor_professional_statement;
	public String doctor_profile_image;
	public String doctor_message;

	public ArrayList<Doctors_Education> education;
	public ArrayList<Doctors_Fellowship> fellowship;
	public ArrayList<Doctors_Image> image;
	public ArrayList<Doctors_Language> language;
	public ArrayList<Doctors_Specialty> specialty;

	public PatientReservation() {
		education = new ArrayList<Doctors_Education>();
		fellowship = new ArrayList<Doctors_Fellowship>();
		image = new ArrayList<Doctors_Image>();
		language = new ArrayList<Doctors_Language>();
		specialty = new ArrayList<Doctors_Specialty>();
	}
}
