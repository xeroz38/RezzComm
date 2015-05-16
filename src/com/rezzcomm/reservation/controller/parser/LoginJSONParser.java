package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.rezzcomm.reservation.controller.JSONParser;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Patient;

public class LoginJSONParser {

	public Patient getJSONData(Context context, String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = jsonParser.getJSONFromUrl(url);

		Patient patient = new Patient();
		if (jsonParser.isCorrect()) {
			patient.patient_country = jsonObj.getString("country");
			patient.patient_lastname = jsonObj.getString("last_name");
			patient.patient_id = jsonObj.getString("patient_id");
			
			SharedPreferences sp = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(SharedInformation.country, jsonObj.getString("country"));
			editor.putString(SharedInformation.last_name, jsonObj.getString("last_name"));
			editor.putString(SharedInformation.patient_id, jsonObj.getString("patient_id"));
			editor.commit();
		}

		return patient;
	}
}