package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.rezzcomm.reservation.controller.JSONParser;

public class CancelReservationJSONParser {

	public String getJSONData(Context context, String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = jsonParser.getJSONFromUrl(url);

		String message = "Reservation does not exist.";
		if (jsonParser.isCorrect()) {
			message = jsonObj.getString("message");
		}

		return message;
	}
}