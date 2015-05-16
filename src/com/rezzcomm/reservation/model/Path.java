package com.rezzcomm.reservation.model;

public interface Path {

	public final String MAIN_URL				= "http://119.75.0.149/Rezzcomm";
	public final String SEARCH_RESULT_URL		= MAIN_URL + "/rest/Search" + "?";
	public final String SEARCH_LOCATION_URL		= MAIN_URL + "/rest/LookupPlace" + "?";
	public final String DOCTOR_DETAIL_URL		= MAIN_URL + "/rest/Search/GetDoctorDetails" + "?";
	public final String REGISTRATION_URL		= MAIN_URL + "/rest/Patient/SignUp" + "?";
	public final String LOGIN_URL				= MAIN_URL + "/rest/Patient/SignIn" + "?";
	public final String ADD_RESERVATION_URL		= MAIN_URL + "/rest/Reservation/AddReservation" + "?";
	public final String EDIT_RESERVATION_URL	= MAIN_URL + "/rest/Reservation/UpdateReservation" + "?";
	public final String CANCEL_RESERVATION_URL	= MAIN_URL + "/rest/Reservation/CancelReservation" + "?";
	public final String RESERVATION_URL			= MAIN_URL + "/rest/Patient/PatientReservationList" + "?";
	public final String BOOKING_TIMESLOT_URL	= MAIN_URL + "/rest/Search/BookTimeslot" + "?";
	public final String RELEASE_TIMESLOT_URL	= MAIN_URL + "/rest/Search/MakeTimeslotAvailable" + "?";
	public final String SMS_ACTIVATION_URL		= MAIN_URL + "/rest/Patient/ResendActivationCode" + "?";
	public final String ACCOUNT_ACTIVATION_URL	= MAIN_URL + "/rest/Patient/ActivateAccount" + "?";
	
	public final String THUMBNAIL_URL			= MAIN_URL + "/images/doctors/";
	public final String TERM_OF_USE_URL			= MAIN_URL + "/terms.jsp";

	public interface PathParameter {

		public final String uctime			= "uctime"					+ "=";
		public final String hcode			= "&" + "hcode" 			+ "=";
		
		public final String doctor_id		= "&" + "doctor_user_id"	+ "=";
		public final String specialty_id	= "&" + "specialty_id"		+ "=";
		public final String patient_id		= "&" + "patient_id"		+ "=";
		public final String place_id		= "&" + "place_id"			+ "=";
		public final String timeslot_id		= "&" + "timeslot_id"		+ "=";
		public final String reservation_id	= "&" + "reservation_id"	+ "=";
		public final String first_time		= "&" + "first_time"		+ "=";
		public final String appt_for		= "&" + "for"				+ "=";

		public final String name			= "&" + "name"				+ "=";
		public final String latitude		= "&" + "latitude"			+ "=";
		public final String longitude		= "&" + "longitude"			+ "=";
		public final String start_date		= "&" + "start_date"		+ "=";
		public final String end_date		= "&" + "end_date"			+ "=";
		public final String option_value	= "&" + "option_value"		+ "=";
		public final String start_item		= "&" + "start_item"		+ "=";

		public final String first_name					= "&" + "first_name"			+ "=";
		public final String last_name					= "&" + "last_name"				+ "=";
		public final String email						= "&" + "email"					+ "=";
		public final String password					= "&" + "password"				+ "=";
		public final String tel_no						= "&" + "tel_no"				+ "=";
		public final String country						= "&" + "country"				+ "=";
		public final String gender						= "&" + "gender"				+ "=";
		
		public final String reservation_reason			= "&" + "reason"				+ "=";
		public final String activation_code				= "&" + "activation_code" 		+ "=";
	}
}
