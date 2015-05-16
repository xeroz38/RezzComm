package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rezzcomm.reservation.controller.JSONParser;
import com.rezzcomm.reservation.model.Location;

public class SearchLocationJSONParser {
	
	public ArrayList<Location> getJSONData(String url) throws JSONException, IOException, URISyntaxException {
		// TODO Auto-generated method stub
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArr = jsonParser.getJSONArrayFromUrl(url);
		
		ArrayList<Location> location = new ArrayList<Location>();
		
		for (int i = 0; i < jsonArr.length(); i++) {
			Location sub_location = new Location();
			
			JSONObject locationListObj = jsonArr.getJSONObject(i);
			sub_location.location_name = locationListObj.getString("name");
			sub_location.location_latitude = locationListObj.getString("latitude");
			sub_location.location_longitude = locationListObj.getString("longitude");
			sub_location.location_place_type = locationListObj.getString("place_type");
			
			location.add(sub_location);
		}
		
		return location;
	}
}