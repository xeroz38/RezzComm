package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rezzcomm.reservation.controller.JSONParser;
import com.rezzcomm.reservation.model.Doctors_Education;
import com.rezzcomm.reservation.model.Doctors_Fellowship;
import com.rezzcomm.reservation.model.Doctors_Image;
import com.rezzcomm.reservation.model.Doctors_Language;
import com.rezzcomm.reservation.model.Doctors_Specialty;
import com.rezzcomm.reservation.model.PatientReservation;

public class PatientReservationJSONParser {
	
	public ArrayList<PatientReservation> getJSONData(String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArr = jsonParser.getJSONArrayFromUrl(url);
		
		ArrayList<PatientReservation> reservation = null;
		
		if (jsonArr != null) {
			reservation = new ArrayList<PatientReservation>();
			for (int i = 0; i < jsonArr.length(); i++) {
				PatientReservation sub_reservation = new PatientReservation();
				JSONObject reservationObj = jsonArr.getJSONObject(i);
				
	//			sub_reservation.first_time = jsonArr.getJSONObject(i).getString("first_time");
				sub_reservation.reservation_id = reservationObj.getString("reservation_id");
				sub_reservation.for_ = jsonArr.getJSONObject(i).getString("for_");
				sub_reservation.first_time = jsonArr.getJSONObject(i).getString("first_time");
				sub_reservation.peer_name = jsonArr.getJSONObject(i).getString("peer_name");
				sub_reservation.peer_tel_no = jsonArr.getJSONObject(i).getString("peer_tel_no");
				sub_reservation.peer_email = jsonArr.getJSONObject(i).getString("peer_email");
	
				JSONObject doctorObj = reservationObj.getJSONObject("reservation_doctor");
	//			sub_reservation.doctor_email_address = doctorObj.getString("email_address");
				sub_reservation.doctor_first_name = doctorObj.getString("first_name");
				sub_reservation.doctor_user_id = doctorObj.getString("user_id");
				sub_reservation.doctor_last_name = doctorObj.getString("last_name");
				sub_reservation.doctor_telp_no = doctorObj.getString("tel_no");
				
				JSONObject doctorProfileObj = doctorObj.getJSONObject("doctor_profile");
	//			sub_reservation.doctor_id = doctorProfileObj.getString("doctor_id");
				sub_reservation.doctor_gender = doctorProfileObj.getString("gender");
				sub_reservation.doctor_professional_statement = doctorProfileObj.getString("professional_statement");
				sub_reservation.doctor_profile_image = doctorProfileObj.getString("profile_image");
				sub_reservation.doctor_message = doctorProfileObj.getString("doctor_message");
	
				JSONArray doctorEducationArr = doctorProfileObj.getJSONArray("doctor_education_list");
				for (int j = 0; j < doctorEducationArr.length(); j++) {
					Doctors_Education education = new Doctors_Education();
					JSONObject doctorEducationObj = doctorEducationArr.getJSONObject(j);
	//				education.education_id = doctorEducationObj.getString("doctor_education_id");
	//				education.education_institute_name = doctorEducationObj.getString("institute_name");
	//				education.education_line_no = doctorEducationObj.getString("line_no");
					education.education_name = doctorEducationObj.getString("name");
					education.education_short_name = doctorEducationObj.getString("short_name");
					education.education_date = doctorEducationObj.getString("education_date");
	
					sub_reservation.education.add(education);
				}
				
				JSONArray doctorFellowshipArr = doctorProfileObj.getJSONArray("doctor_fellowship_list");
				for (int j = 0; j < doctorFellowshipArr.length(); j++) {
					Doctors_Fellowship fellowship = new Doctors_Fellowship();
					JSONObject doctorFellowshipObj = doctorFellowshipArr.getJSONObject(j);
	//				education.education_id = doctorEducationObj.getString("doctor_education_id");
	//				education.education_institute_name = doctorEducationObj.getString("institute_name");
	//				education.education_line_no = doctorEducationObj.getString("line_no");
					fellowship.fellowship_name = doctorFellowshipObj.getString("name");
	//				fellowship.education_short_name = doctorEducationObj.getString("short_name");
	//				fellowship.education_date = doctorEducationObj.getString("education_date");
	
					sub_reservation.fellowship.add(fellowship);
				}
				
				JSONArray doctorImageArray = doctorProfileObj.getJSONArray("doctor_image_list");
				for (int j = 0; j < doctorImageArray.length(); j++) {
					Doctors_Image image = new Doctors_Image();
					JSONObject doctorImageObj = doctorImageArray.getJSONObject(j);
	//				image.image_id = doctorImageObj.getString("doctor_image_id");
	//				image.image_line_no = doctorImageObj.getString("line_no");
					image.image_name = doctorImageObj.getString("name");
	
					sub_reservation.image.add(image);
				}
				
				JSONArray doctorLanguageArray = doctorProfileObj.getJSONArray("doctor_language_list");
				for (int j = 0; j < doctorLanguageArray.length(); j++) {
					Doctors_Language language = new Doctors_Language();
	//				JSONObject doctorImageObj = doctorLanguageArray.getJSONObject(j);
	//				language.doctor_language_id = doctorImageObj.getString("doctor_language_id");
	//				language.doctor_language = doctorImageObj.getString("language");
	//				language.doctor_line_no = doctorImageObj.getString("line_no");
	
					sub_reservation.language.add(language);
				}
				
				JSONArray doctorSpecialtyArray = doctorProfileObj.getJSONArray("doctor_specialty_list");
				for (int j = 0; j < doctorSpecialtyArray.length(); j++) {
					Doctors_Specialty specialty = new Doctors_Specialty();
					JSONObject doctorSpecialtyObj = doctorSpecialtyArray.getJSONObject(j);
	//				specialty.specialty_parent_id = doctorSpecialtyObj.getString("parent_id");
	//				specialty.specialty_short_name = doctorSpecialtyObj.getString("short_name");
					specialty.specialty_name = doctorSpecialtyObj.getString("name");
					specialty.specialty_id = doctorSpecialtyObj.getString("specialty_id");
					
					sub_reservation.specialty.add(specialty);
				}
				
				JSONObject reservationPlaceObj = reservationObj.getJSONObject("reservation_place");
				sub_reservation.reservation_name = reservationPlaceObj.getString("name");
				sub_reservation.reservation_state = reservationPlaceObj.getString("state");
				sub_reservation.reservation_country = reservationPlaceObj.getString("country");
				sub_reservation.reservation_description = reservationPlaceObj.getString("description");
				sub_reservation.reservation_city = reservationPlaceObj.getString("city");
	//			sub_reservation.reservation_place_facility_list = reservationPlaceObj.getString("place_facility_list");
	//			sub_reservation.reservation_place_image_list = reservationPlaceObj.getString("place_image_list");
				sub_reservation.reservation_place_id = reservationPlaceObj.getString("place_id");
				sub_reservation.reservation_address_line1 = reservationPlaceObj.getString("address_line1");
				sub_reservation.reservation_postal_code = reservationPlaceObj.getString("postal_code");
				sub_reservation.reservation_tel_no = reservationPlaceObj.getString("tel_no");
				sub_reservation.reservation_profile_image = reservationPlaceObj.getString("profile_image");
				sub_reservation.reservation_address_line2 = reservationPlaceObj.getString("address_line2");
				sub_reservation.reservation_address_line3 = reservationPlaceObj.getString("address_line3");
				sub_reservation.reservation_address_line4 = reservationPlaceObj.getString("address_line4");
				sub_reservation.reservation_fax_no = reservationPlaceObj.getString("fax_no");
	
				JSONObject reservationSpecialtyObj = reservationObj.getJSONObject("reservation_specialty");
				sub_reservation.reservation_specialty_name = reservationSpecialtyObj.getString("name");
				sub_reservation.reservation_specialty_id = reservationSpecialtyObj.getString("specialty_id");
				
				JSONObject reservationTimeslotObj = reservationObj.getJSONObject("reservation_timeslot");
	//			sub_reservation.reservation_duration_min = reserveObj.getString("duration_min");
	//			sub_reservation.reservation_status = reserveObj.getString("status");
				sub_reservation.reservation_timeslot_id = reservationTimeslotObj.getString("timeslot_id");
				sub_reservation.reservation_end_date = reservationTimeslotObj.getString("end_date");
				sub_reservation.reservation_start_date = reservationTimeslotObj.getString("start_date");
	
				reservation.add(sub_reservation);
			}
		}
		
		return reservation;
	}
}