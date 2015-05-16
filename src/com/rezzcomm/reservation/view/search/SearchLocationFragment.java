package com.rezzcomm.reservation.view.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.LocationFinder;
import com.rezzcomm.reservation.controller.NetRequest;
import com.rezzcomm.reservation.controller.NetRequest.OnSuccessListener;
import com.rezzcomm.reservation.controller.parser.SearchLocationJSONParser;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Location;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.MD5TimePass;

public class SearchLocationFragment extends Fragment {

	private TextView title;
	private EditText search_field;
	private ListView search_list;

	private InputMethodManager imm;
	private SharedPreferences sp;
	private ArrayList<Location> location;
	private SearchLocationJSONParser location_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.search_location, container, false);

		title = (TextView) view.findViewById(R.id.title);
		search_field = (EditText) view.findViewById(R.id.search_field);
		search_list = (ListView) view.findViewById(R.id.search_list);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		title.setText("Get Location");
		location = new ArrayList<Location>();
		
		final Button tv = new Button(getActivity());
		tv.setBackgroundResource(R.drawable.btn_bg);
		tv.setText("Get Current Location");
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(15);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setOnClickListener(new CurrentLocationClickListener());
		search_list.addHeaderView(tv);
		
		search_field.requestFocus();
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(search_field, InputMethodManager.SHOW_IMPLICIT);
		
		NetRequest nr = new NetRequest(getActivity());
		nr.execute(Path.MAIN_URL);
		nr.setOnSuccessListener(new SearchSuccessListener());
		
		sp = getActivity().getSharedPreferences("location_pref", Context.MODE_PRIVATE);
	}
	
	private void getLocationData() {
		MD5TimePass md5 = new MD5TimePass();
		String[] unixMD5 = md5.getMD5();
		
		try {
			location_parser = new SearchLocationJSONParser();
			location = location_parser.getJSONData(Path.SEARCH_LOCATION_URL +
					PathParameter.uctime + unixMD5[0] + 
					PathParameter.hcode + unixMD5[1] + 
					PathParameter.name + search_field.getText().toString());
			
			if (location != null) {
				search_list.setAdapter(new LocationArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, location));
				search_list.setOnItemClickListener(new LocationClickListener());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final class CurrentLocationClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new CurrentLocation().execute("");
		}
	}
	
	private final class SearchSuccessListener implements OnSuccessListener {
		@Override
		public void doSuccess(String result) {
			// TODO Auto-generated method stub
			TextWatcher tw = new TextChangedListener();
			search_field.addTextChangedListener(tw);
		}

		@Override
		public void doError() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
		}
	}

	private final class TextChangedListener implements TextWatcher {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			getLocationData();
			search_list.invalidateViews();
		}
	}

	private final class LocationArrayAdapter extends ArrayAdapter<Location> {
		
		private LocationArrayAdapter(Context context, int textViewResourceId,
				List<Location> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = (TextView) super.getView(position, convertView, parent);
			textView.setText(location.get(position).location_name);
		    textView.setTextColor(Color.BLACK);
		    
		    return textView;
		}
	}

	private final class LocationClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> av, View view, int position, long id) {
			// TODO Auto-generated method stub
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(SharedInformation.location_name, location.get(position-1).location_name);
			editor.putString(SharedInformation.location_latitude, location.get(position-1).location_latitude);
			editor.putString(SharedInformation.location_longitude, location.get(position-1).location_longitude);
			editor.putString(SharedInformation.location_place_type, location.get(position-1).location_place_type);
			editor.commit();
			
			imm.hideSoftInputFromWindow(search_field.getWindowToken(), 0);
			
			FragmentManager fm = getFragmentManager();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content, new SearchFragment());
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		}
	}
	
	private class CurrentLocation extends AsyncTask<String, String, String> {
		
		private LocationFinder lf = new LocationFinder(getActivity());
		private String curLocation;
		private double latitude, longitude;
		private ProgressDialog pd = new ProgressDialog(getActivity());
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			curLocation = lf.getAddress();
			latitude = lf.getLatitude();
			longitude = lf.getLongitude();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			SharedPreferences.Editor editor = sp.edit();
			if (lf.getAddress() != null) {
				editor.putString(SharedInformation.location_name, curLocation);
				editor.putString(SharedInformation.location_latitude, String.valueOf(latitude));
				editor.putString(SharedInformation.location_longitude, String.valueOf(longitude));
			} else {
				editor.putString(SharedInformation.location_name, "Location unavailable");
				editor.putString(SharedInformation.location_latitude, String.valueOf(1.283333));
				editor.putString(SharedInformation.location_longitude, String.valueOf(103.833333));
			}
			editor.commit();
			
			FragmentManager fm = getFragmentManager();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content, new SearchFragment());
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
			pd.dismiss();
		}
	}
}
