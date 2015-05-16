package com.rezzcomm.reservation.controller.parser;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.rezzcomm.reservation.controller.JSONParser;

public class ReleaseTimeslotJSONParser {

	public String getJSONData(Context context, String url) throws JSONException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = jsonParser.getJSONFromUrl(url);

		String message = "";
		if (jsonParser.isCorrect()) {
			message = jsonObj.getString("message");
		}

		return message;
	}
}