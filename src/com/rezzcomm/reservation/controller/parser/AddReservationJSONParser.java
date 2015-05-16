package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.rezzcomm.reservation.controller.JSONParser;

public class AddReservationJSONParser {

	public String getJSONData(Context context, String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = jsonParser.getJSONFromUrl(url);

		String message = "Timeslot is already booked or reserved";
		if (jsonParser.isCorrect()) {
			message = jsonObj.getString("confirmation_id");
		}

		return message;
	}
}