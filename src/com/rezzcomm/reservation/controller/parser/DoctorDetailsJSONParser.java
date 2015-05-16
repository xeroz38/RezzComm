package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rezzcomm.reservation.controller.JSONParser;
import com.rezzcomm.reservation.model.Doctors;
import com.rezzcomm.reservation.model.Doctors_Education;
import com.rezzcomm.reservation.model.Doctors_Fellowship;
import com.rezzcomm.reservation.model.Doctors_Image;
import com.rezzcomm.reservation.model.Doctors_Language;
import com.rezzcomm.reservation.model.Doctors_Specialty;
import com.rezzcomm.reservation.model.Doctors_Timeslot;
import com.rezzcomm.reservation.model.Timeslot_list;

public class DoctorDetailsJSONParser {
	
	public Doctors getJSONData(String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = jsonParser.getJSONFromUrl(url);
		
		Doctors doctors = new Doctors();
		
		JSONArray reviewArr = jsonObj.getJSONArray("review_list");
		for (int j = 0; j < reviewArr.length(); j++) {

		}		
		
		JSONArray timeSlotArr = jsonObj.getJSONArray("timeslot_date_list");
		for (int j = 0; j < timeSlotArr.length(); j++) {
			Doctors_Timeslot timeslot = new Doctors_Timeslot();
			JSONObject doctorTimeslotObj = timeSlotArr.getJSONObject(j);
			timeslot.timeslot_date = doctorTimeslotObj.getString("timeslot_date");
			
			JSONArray doctorTimeslotList = doctorTimeslotObj.getJSONArray("timeslot_list");
			for (int k = 0; k < doctorTimeslotList.length(); k++) {
				Timeslot_list sub_timeslot = new Timeslot_list();
				JSONObject doctorTimeslotListObj = doctorTimeslotList.getJSONObject(k);
				sub_timeslot.start_date = doctorTimeslotListObj.getString("start_date");
				sub_timeslot.end_date = doctorTimeslotListObj.getString("end_date");
				sub_timeslot.timeslot_id = doctorTimeslotListObj.getString("timeslot_id");
				
				timeslot.timeslot_list.add(sub_timeslot);
			}
			doctors.timeslot.add(timeslot);
		}
		
		JSONObject averageReviewObj = jsonObj.getJSONObject("average_review");
		doctors.review_comments = averageReviewObj.getString("comments");
		doctors.review_date = averageReviewObj.getString("review_date");
		doctors.review_wait_time = averageReviewObj.getString("waiting_time");
		doctors.review_helpfull = averageReviewObj.getString("helpfulness");
		doctors.review_id = averageReviewObj.getString("review_id");
		doctors.review_overall = averageReviewObj.getString("overall");
		
		JSONObject doctorObj = jsonObj.getJSONObject("doctor");
		doctors.doctor_first_name = doctorObj.getString("first_name");
		doctors.doctor_last_name = doctorObj.getString("last_name");
		doctors.doctor_telp_no = doctorObj.getString("tel_no");
		doctors.doctor_user_id = doctorObj.getString("user_id");
		
		JSONObject doctorProfileObj = doctorObj.getJSONObject("doctor_profile");
		doctors.doctor_message = doctorProfileObj.getString("first_doctor_message");
		doctors.doctor_gender = doctorProfileObj.getString("gender");
		doctors.doctor_professional_statement = doctorProfileObj.getString("professional_statement");
		doctors.doctor_profile_image = doctorProfileObj.getString("profile_image");

		JSONArray doctorEducationArr = doctorProfileObj.getJSONArray("doctor_education_list");
		for (int j = 0; j < doctorEducationArr.length(); j++) {
			Doctors_Education education = new Doctors_Education();
			JSONObject doctorEducationObj = doctorEducationArr.getJSONObject(j);
			education.education_name = doctorEducationObj.getString("name");
			education.education_short_name = doctorEducationObj.getString("short_name");
			education.education_date = doctorEducationObj.getString("education_date");

			doctors.education.add(education);
		}
		
		JSONArray doctorFellowshipArr = doctorProfileObj.getJSONArray("doctor_fellowship_list");
		for (int j = 0; j < doctorFellowshipArr.length(); j++) {
			Doctors_Fellowship fellowship = new Doctors_Fellowship();
			JSONObject doctorFellowshipObj = doctorFellowshipArr.getJSONObject(j);
			fellowship.fellowship_description = doctorFellowshipObj.getString("description");
			fellowship.fellowship_name = doctorFellowshipObj.getString("name");
			
			doctors.fellowship.add(fellowship);
		}
		
		JSONArray doctorImageArr = doctorProfileObj.getJSONArray("doctor_image_list");
		for (int j = 0; j < doctorImageArr.length(); j++) {
			Doctors_Image image = new Doctors_Image();
			JSONObject doctorImageObj = doctorImageArr.getJSONObject(j);
			image.image_description = doctorImageObj.getString("description");
			image.image_name = doctorImageObj.getString("name");
			
			doctors.image.add(image);
		}
		
		JSONArray doctorLanguageArr = doctorProfileObj.getJSONArray("doctor_language_list");
		for (int j = 0; j < doctorLanguageArr.length(); j++) {
			Doctors_Language language = new Doctors_Language();
			JSONObject doctorLanguageObj = doctorLanguageArr.getJSONObject(j);
			language.doctor_language = doctorLanguageObj.getString("language");
			
			doctors.language.add(language);
		}
		
		JSONArray doctorSpecialtyArr = doctorProfileObj.getJSONArray("doctor_specialty_list");
		for (int j = 0; j < doctorSpecialtyArr.length(); j++) {
			Doctors_Specialty specialty = new Doctors_Specialty();
			JSONObject doctorSpecialtyObj = doctorSpecialtyArr.getJSONObject(j);
			specialty.specialty_name = doctorSpecialtyObj.getString("name");
			specialty.specialty_id = doctorSpecialtyObj.getString("specialty_id");
			
			doctors.specialty.add(specialty);
		}
		
		JSONObject placeObj = jsonObj.getJSONObject("place");
		doctors.place_address_line1 = placeObj.getString("address_line1");
		doctors.place_address_line2 = placeObj.getString("address_line2");
		doctors.place_address_line3 = placeObj.getString("address_line3");
		doctors.place_address_line4 = placeObj.getString("address_line4");
		doctors.place_city = placeObj.getString("city");
		doctors.place_country = placeObj.getString("country");
		doctors.place_description = placeObj.getString("description");
		doctors.place_fax_no = placeObj.getString("fax_no");
		doctors.place_name = placeObj.getString("name");
		doctors.place_id = placeObj.getString("place_id");
		doctors.place_postal_code = placeObj.getString("postal_code");
		doctors.place_state = placeObj.getString("state");
		doctors.place_tel_no = placeObj.getString("tel_no");
		
		return doctors;
	}
}