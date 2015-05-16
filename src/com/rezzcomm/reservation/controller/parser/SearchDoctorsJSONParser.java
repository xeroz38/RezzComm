package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rezzcomm.reservation.controller.JSONParser;
import com.rezzcomm.reservation.model.Doctors;
import com.rezzcomm.reservation.model.Doctors_Education;
import com.rezzcomm.reservation.model.Doctors_Fellowship;
import com.rezzcomm.reservation.model.Doctors_Image;
import com.rezzcomm.reservation.model.Doctors_Language;
import com.rezzcomm.reservation.model.Doctors_MainSpecialty;
import com.rezzcomm.reservation.model.Doctors_Specialty;
import com.rezzcomm.reservation.model.Doctors_Timeslot;
import com.rezzcomm.reservation.model.Timeslot_list;

public class SearchDoctorsJSONParser {
	
	public ArrayList<Doctors> getJSONData(String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArr = jsonParser.getJSONArrayFromUrl(url);
		
		ArrayList<Doctors> doctors = new ArrayList<Doctors>();
		
		for (int i = 0; i < jsonArr.length(); i++) {
			Doctors sub_doctors = new Doctors();
			
			JSONObject doctorListObj = jsonArr.getJSONObject(i);
			JSONObject doctorObj = doctorListObj.getJSONObject("doctor");
			sub_doctors.doctor_first_name = doctorObj.getString("first_name");
			sub_doctors.doctor_last_name = doctorObj.getString("last_name");
			sub_doctors.doctor_telp_no = doctorObj.getString("tel_no");
			sub_doctors.doctor_user_id = doctorObj.getString("user_id");
			
			JSONObject doctorProfileObj = doctorObj.getJSONObject("doctor_profile");
			sub_doctors.doctor_message = doctorProfileObj.getString("first_doctor_message");
			sub_doctors.doctor_gender = doctorProfileObj.getString("gender");
			sub_doctors.doctor_professional_statement = doctorProfileObj.getString("professional_statement");
			sub_doctors.doctor_profile_image = doctorProfileObj.getString("profile_image");
			
			JSONArray doctorMainSpecialtyArr = doctorProfileObj.getJSONArray("doctor_main_specialty_list");
			for (int j = 0; j < doctorMainSpecialtyArr.length(); j++) {
				Doctors_MainSpecialty main_specialty = new Doctors_MainSpecialty();
				JSONObject doctorMainSpecialtyObj = doctorMainSpecialtyArr.getJSONObject(j);
				main_specialty.specialty_name = doctorMainSpecialtyObj.getString("name");
				main_specialty.specialty_id = doctorMainSpecialtyObj.getString("specialty_id");
				
				sub_doctors.main_specialty.add(main_specialty);
			}

			JSONArray doctorEducationArr = doctorProfileObj.getJSONArray("doctor_education_list");
			for (int j = 0; j < doctorEducationArr.length(); j++) {
				Doctors_Education education = new Doctors_Education();
				JSONObject doctorEducationObj = doctorEducationArr.getJSONObject(j);
				education.education_name = doctorEducationObj.getString("name");
				education.education_short_name = doctorEducationObj.getString("short_name");
				education.education_date = doctorEducationObj.getString("education_date");
				
				sub_doctors.education.add(education);
			}
			
			JSONArray doctorFellowshipArr = doctorProfileObj.getJSONArray("doctor_fellowship_list");
			for (int j = 0; j < doctorFellowshipArr.length(); j++) {
				Doctors_Fellowship fellowship = new Doctors_Fellowship();
				JSONObject doctorFellowshipObj = doctorFellowshipArr.getJSONObject(j);
				fellowship.fellowship_name = doctorFellowshipObj.getString("name");
				fellowship.fellowship_description = doctorFellowshipObj.getString("description");
				
				sub_doctors.fellowship.add(fellowship);
			}
			
			JSONArray doctorImageArr = doctorProfileObj.getJSONArray("doctor_image_list");
			for (int j = 0; j < doctorImageArr.length(); j++) {
				Doctors_Image image = new Doctors_Image();
				JSONObject doctorImageObj = doctorImageArr.getJSONObject(j);
				image.image_name = doctorImageObj.getString("name");
				image.image_description = doctorImageObj.getString("description");
				
				sub_doctors.image.add(image);
			}
			
			JSONArray doctorLanguageArr = doctorProfileObj.getJSONArray("doctor_language_list");
			for (int j = 0; j < doctorLanguageArr.length(); j++) {
				Doctors_Language language = new Doctors_Language();
				JSONObject doctorLanguageObj = doctorLanguageArr.getJSONObject(j);
				language.doctor_language = doctorLanguageObj.getString("language");
				
				sub_doctors.language.add(language);
			}
			
			JSONArray doctorSpecialtyArr = doctorProfileObj.getJSONArray("doctor_specialty_list");
			for (int j = 0; j < doctorSpecialtyArr.length(); j++) {
				Doctors_Specialty specialty = new Doctors_Specialty();
				JSONObject doctorSpecialtyObj = doctorSpecialtyArr.getJSONObject(j);
				specialty.specialty_name = doctorSpecialtyObj.getString("name");
				specialty.specialty_id = doctorSpecialtyObj.getString("specialty_id");
				
				sub_doctors.specialty.add(specialty);
			}

			JSONObject placeObj = doctorListObj.getJSONObject("place");
			sub_doctors.place_address_line1 = placeObj.getString("address_line1");
			sub_doctors.place_address_line2 = placeObj.getString("address_line2");
			sub_doctors.place_address_line3 = placeObj.getString("address_line3");
			sub_doctors.place_address_line4 = placeObj.getString("address_line4");
			sub_doctors.place_city = placeObj.getString("city");
			sub_doctors.place_country = placeObj.getString("country");
			sub_doctors.place_description = placeObj.getString("description");
			sub_doctors.place_fax_no = placeObj.getString("fax_no");
			sub_doctors.place_name = placeObj.getString("name");
			sub_doctors.place_id = placeObj.getString("place_id");
			sub_doctors.place_postal_code = placeObj.getString("postal_code");
			sub_doctors.place_state = placeObj.getString("state");
			sub_doctors.place_tel_no = placeObj.getString("tel_no");
			
			JSONArray reviewArr = doctorListObj.getJSONArray("review_list");
			for (int j = 0; j < reviewArr.length(); j++) {

			}
			
			JSONArray timeSlotArr = doctorListObj.getJSONArray("timeslot_date_list");
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
				sub_doctors.timeslot.add(timeslot);
			}
			
			JSONObject averageReviewObj = doctorListObj.getJSONObject("average_review");
			sub_doctors.review_comments = averageReviewObj.getString("comments");
			sub_doctors.review_date = averageReviewObj.getString("review_date");
			sub_doctors.review_wait_time = averageReviewObj.getString("waiting_time");
			sub_doctors.review_helpfull = averageReviewObj.getString("helpfulness");
			sub_doctors.review_id = averageReviewObj.getString("review_id");
			sub_doctors.review_overall = averageReviewObj.getString("overall");

			doctors.add(sub_doctors);
		}
		
		return doctors;
	}
}