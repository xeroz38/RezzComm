package com.rezzcomm.reservation.view.search;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.parser.CSVListParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;

public class SearchFragment extends Fragment {

	private boolean isVisible = false;
	private int[] date_arr = new int[3];
//	private double latitude, longitude;

	private EditText date, location;
	private DatePicker datepicker;
	private Button search_btn;
	private Spinner specialty, reasonforvisit;
	private ScrollView search_sv;
	
	private SharedPreferences sp;
	private CSVListParser csvlist;
	private ArrayList<String> tempArr;
	private ArrayAdapter<String> dataAdapterSP, dataAdapterROV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.search_fragment, container, false);

		search_sv = (ScrollView) view.findViewById(R.id.search_scroll_view);

		date = (EditText) view.findViewById(R.id.date);
		datepicker = (DatePicker) view.findViewById(R.id.datepicker);
		specialty = (Spinner) view.findViewById(R.id.specialty);
		reasonforvisit = (Spinner) view.findViewById(R.id.reasonforvisit);
		location = (EditText) view.findViewById(R.id.location);
		search_btn = (Button) view.findViewById(R.id.search_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		csvlist = new CSVListParser(getActivity());

		dataAdapterSP = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, csvlist.getSpecialty_name()) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(getActivity());
					convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
				}
				TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
				tv.setText(csvlist.getSpecialty_name().get(position));
				tv.setTextSize(12);
				
				return convertView;
			}
		};
		dataAdapterSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		specialty.setAdapter(dataAdapterSP);
		specialty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				tempArr = new ArrayList<String>();
				tempArr.add("Choose reason for visit");
				
				for(int i = 0; i < csvlist.getReason_for_visit_arr().size(); i++) {
					if ((csvlist.getSpecialty_arr().get(position).id).equals(csvlist.getReason_for_visit_arr().get(i).parent)){
						tempArr.add(csvlist.getReason_for_visit_arr().get(i).name);
					}
				}
				dataAdapterROV = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tempArr) {

					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						// TODO Auto-generated method stub
						if (convertView == null) {
							LayoutInflater inflater = LayoutInflater.from(getActivity());
							convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
						}
						TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
						tv.setText(tempArr.get(position));
						tv.setTextSize(12);
						
						return convertView;
					}
				};
				dataAdapterROV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				reasonforvisit.setAdapter(dataAdapterROV);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		location.setText("Get Location");
		location.setOnClickListener(new LocationClickListener());

		date_arr[0] = datepicker.getDayOfMonth();
		date_arr[1] = datepicker.getMonth();
		date_arr[2] = datepicker.getYear();

		date.setText(date_arr[0] + " / " + (date_arr[1]+1) + " / " + date_arr[2]);
		date.setOnClickListener(new DateClickListener());
		datepicker.init(date_arr[2], date_arr[1], date_arr[0], new DateOnChangeListener());

		search_btn.setOnClickListener(new SearchClickListener());
		
		sp = getActivity().getSharedPreferences("location_pref", Context.MODE_PRIVATE);
		location.setText(sp.getString(SharedInformation.location_name, "Get Location"));
	}
	
	private final class LocationClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content, new SearchLocationFragment());
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class SearchClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			date_arr[0] = datepicker.getDayOfMonth();
			date_arr[1] = datepicker.getMonth();
			date_arr[2] = datepicker.getYear();
			
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new SearchResultFragment();
				Bundle bundle = new Bundle();
				bundle.putString(BundleInformation.specialty_id, csvlist.getSpecialty_arr().get(specialty.getSelectedItemPosition()).id);
				bundle.putString(BundleInformation.specialty_name, csvlist.getSpecialty_arr().get(specialty.getSelectedItemPosition()).name);
				bundle.putInt(BundleInformation.reason_for_visit_id, reasonforvisit.getSelectedItemPosition());
				bundle.putString(BundleInformation.latitude, sp.getString(SharedInformation.location_latitude, ""));
				bundle.putString(BundleInformation.longitude, sp.getString(SharedInformation.location_longitude, ""));
				bundle.putIntArray(BundleInformation.date, date_arr);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class DateClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isVisible) {
				datepicker.setVisibility(View.GONE);
				isVisible = false;
			} else {
				datepicker.setVisibility(View.VISIBLE);
				search_sv.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						search_sv.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
				isVisible = true;
			}
		}
	}

	private final class DateOnChangeListener implements OnDateChangedListener {
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			date_arr[0] = dayOfMonth;
			date_arr[1] = monthOfYear;
			date_arr[2] = year;
			date.setText(date_arr[0] + " / " + (date_arr[1]+1) + " / " + date_arr[2]);
		}
	}
}
