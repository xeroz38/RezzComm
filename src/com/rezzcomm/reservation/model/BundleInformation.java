package com.rezzcomm.reservation.model;

import android.net.Uri;

public interface BundleInformation {
	
	public final String specialty						= "specialty";
	public final String specialty_id					= "specialty_id";
	public final String specialty_name					= "specialty_name";
	public final String reason_for_visit_id				= "reason_for_visit_id";
	public final String reason_for_visit				= "reason_for_visit";
	public final String latitude						= "latitude";
	public final String longitude						= "longitude";
	public final String date							= "date";
	public final String time							= "time";

	public final String doctor_id						= "doctor_id";
	public final String doctor_name						= "doctor_name";
	public final String doctor_profile_image			= "doctor_profile_image";
	public final String doctor_professional_statement	= "doctor_professional_statement";
	public final String doctor_main_specialty			= "doctor_main_specialty";
	public final String doctor_specialty				= "doctor_specialty";
	public final String doctor_education				= "doctor_education";
	public final String doctor_awards					= "doctor_awards";

	public final String place_id						= "place_id";
	public final String place_address_line1				= "place_address_line1";
	public final String place_name						= "place_name";
	
	public final String timeslot						= "timeslot";
	public final String timeslot_id						= "timeslot_id";

	public final String appointment_for					= "appointment_for";
	public final String first_time_visit				= "first_time_visit";
	public final String patient_name					= "patient_name";
	public final String patient_tel_no					= "patient_tel_no";
	public final String patient_email					= "patient_email";
	public final String patient_id						= "patient_id";
	
	public final String reservation_id					= "reservation_id";

	
	public interface UserInformation {

		public final String[] Country					= {"Singapore", "Malaysia", "Indonesia", "Unknown"};
		public final String[] Gender 					= {"Male", "Female"};
	}
	
	public interface SharedInformation {

		public final String country						= "country";
		public final String email						= "email";
		public final String first_name					= "first_name";
		public final String gender						= "gender";
		public final String last_name					= "last_name";
		public final String patient_id					= "patient_id";
		public final String tel_no						= "tel_no";
		public final String token						= "token";
		public final String timeslot_id					= "timeslot_id";
		
		public final String location_name				= "location_name";
		public final String location_longitude			= "location_longitude";
		public final String location_latitude			= "location_latitude";
		public final String location_place_type			= "location_place_type";
	}
	
	public interface FavoriteColumns
	{
		public final Uri CONTENT_URI = Uri.parse("content://" + "com.rezzcomm.reservation").buildUpon().appendPath("favorite").build();
		
		public final String[] QUERY_SHORT = {
				
			FavoriteColumns.SPECIALTY_ID,
			FavoriteColumns.SPECIALTY_NAME,
			FavoriteColumns.REASON_FOR_VISIT_ID,
			FavoriteColumns.DATE,
			FavoriteColumns.SPECIALTY,
			FavoriteColumns.DOCTOR_ID,
            FavoriteColumns.DOCTOR_NAME,
            FavoriteColumns.DOCTOR_PROFILE_IMAGE,
            FavoriteColumns.DOCTOR_PROFFESIONAL_STATEMENT,
            FavoriteColumns.DOCTOR_SPECIALTY,
            FavoriteColumns.PLACE_ID,
            FavoriteColumns.PLACE_ADDRESS,
            FavoriteColumns.PLACE_NAME,
            FavoriteColumns.TIMESLOT_ID,
            FavoriteColumns.DOCTOR_EDUCATION,
            FavoriteColumns.DOCTOR_AWARDS
		};

		public final String SPECIALTY_ID					= "specialty_id";
		public final String SPECIALTY_NAME					= "specialty_name";
		public final String REASON_FOR_VISIT_ID				= "reason_for_visit_id";
		public final String DATE							= "date";
		public final String SPECIALTY						= "specialty";
		public final String DOCTOR_ID						= "doctor_id";
		public final String DOCTOR_NAME						= "doctor_name";
		public final String DOCTOR_PROFILE_IMAGE			= "doctor_profile_image";
		public final String DOCTOR_PROFFESIONAL_STATEMENT	= "doctor_proffesional_statement";
		public final String DOCTOR_SPECIALTY				= "doctor_specialty";
		public final String PLACE_ID						= "place_id";
		public final String PLACE_ADDRESS					= "place_address";
		public final String PLACE_NAME						= "place_name";
		public final String TIMESLOT_ID						= "timeslot_id";
		public final String DOCTOR_EDUCATION				= "doctor_education";
		public final String DOCTOR_AWARDS					= "doctor_awards";

	}
}
