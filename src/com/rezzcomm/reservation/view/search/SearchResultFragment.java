package com.rezzcomm.reservation.view.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.ImageLoader;
import com.rezzcomm.reservation.controller.NetRequest;
import com.rezzcomm.reservation.controller.NetRequest.OnSuccessListener;
import com.rezzcomm.reservation.controller.parser.BookingTimeslotJSONParser;
import com.rezzcomm.reservation.controller.parser.CSVListParser;
import com.rezzcomm.reservation.controller.parser.ReleaseTimeslotJSONParser;
import com.rezzcomm.reservation.controller.parser.SearchDoctorsJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Doctors;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.MD5TimePass;
import com.rezzcomm.reservation.view.search.booking.BookingLoginFragment;

public class SearchResultFragment extends Fragment {

	private String specialty_id;
	private String specialty_name;
	private String latitude, longitude;
	private int reason_for_visit_id;
	private int[] date_arr;
	private int maxDate;
	private String booking_message, release_message;

	private TextView title;
	private Button btn1, btn2, btn3, btn4, btn5;
	private ImageView prev, next;
	private ListView list_doctor;
	private Spinner reasonforvisit;
	private Calendar calendar;
	private ProgressDialog pd;

	private LayoutInflater inflater;
	private ArrayAdapter<String> dataAdapter;
	private ArrayList<Doctors> doctors;
	private ImageLoader imageLoader;
	private SharedPreferences sp;
	private CSVListParser csvlist;
	private SearchDoctorsJSONParser search_doctors_parser;
	private BookingTimeslotJSONParser booking_parser;
	private ReleaseTimeslotJSONParser release_parser;


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		NetRequest nr = new NetRequest(getActivity());
		nr.execute(Path.MAIN_URL);
		nr.setOnSuccessListener(new SearchSuccessListener());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.search_result, container, false);

		title = (TextView) view.findViewById(R.id.title);

		btn1 = (Button) view.findViewById(R.id.btn1);
		btn2 = (Button) view.findViewById(R.id.btn2);
		btn3 = (Button) view.findViewById(R.id.btn3);
		btn4 = (Button) view.findViewById(R.id.btn4);
		btn5 = (Button) view.findViewById(R.id.btn5);

		prev = (ImageView) view.findViewById(R.id.prev);
		next = (ImageView) view.findViewById(R.id.next);

		reasonforvisit = (Spinner) view.findViewById(R.id.reasonforvisit);
		list_doctor = (ListView) view.findViewById(R.id.list_doctor);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
			specialty_id = bundle.getString(BundleInformation.specialty_id);
			specialty_name = bundle.getString(BundleInformation.specialty_name);
			reason_for_visit_id = bundle.getInt(BundleInformation.reason_for_visit_id);
			latitude = bundle.getString(BundleInformation.latitude);
			longitude = bundle.getString(BundleInformation.longitude);
			date_arr = bundle.getIntArray(BundleInformation.date);
		}

		title.setText(specialty_name);

		inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pd = new ProgressDialog(getActivity());
		//		pd.setIcon(R.drawable.default_thumb);
		pd.setTitle("Please Wait");
		pd.setMessage("Loading...");
		pd.show();

		imageLoader = new ImageLoader(getActivity());
		csvlist = new CSVListParser(getActivity());

		final ArrayList<String> tempArr = new ArrayList<String>();
		tempArr.add("Choose reason for visit");
		for (int i = 0; i < csvlist.getReason_for_visit_arr().size(); i++) {
			if (specialty_id.equals(csvlist.getReason_for_visit_arr().get(i).parent)){
				tempArr.add(csvlist.getReason_for_visit_arr().get(i).name);
			}
		}
		dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tempArr) {

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
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		reasonforvisit.setAdapter(dataAdapter);
		reasonforvisit.setSelection(reason_for_visit_id);

		if (reason_for_visit_id > 0) specialty_id = csvlist.getReason_for_visit_arr().get(reason_for_visit_id).id;

		calendar = Calendar.getInstance();
		calendar.set(date_arr[2], date_arr[1]+1, date_arr[0]);
		maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calculateCalendar(date_arr[0], date_arr[1], date_arr[2]);

		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
	}

	private void getDoctorsData() {
		// TODO Auto-generated method stub
		MD5TimePass md5 = new MD5TimePass();
		String[] unixMD5 = md5.getMD5();

		try {
			search_doctors_parser = new SearchDoctorsJSONParser();
			doctors = search_doctors_parser.getJSONData(Path.SEARCH_RESULT_URL + 
					PathParameter.uctime + unixMD5[0] + 
					PathParameter.hcode + unixMD5[1] + 
					PathParameter.specialty_id + specialty_id + 
					PathParameter.latitude + latitude + 
					PathParameter.longitude + longitude +
					PathParameter.start_date + date_arr[2] + "/" + (date_arr[1]+1) + "/" + date_arr[0] + 
					PathParameter.end_date + date_arr[2] + "/" + (date_arr[1]+1) + "/" + date_arr[0] + 
					PathParameter.option_value + "" +
					PathParameter.start_item + "0");
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

	private void releaseTimeslot() {
		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

		if (sp.getString(SharedInformation.timeslot_id, null) != null) {
			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();

			try {
				release_parser = new ReleaseTimeslotJSONParser();
				release_message = release_parser.getJSONData(getActivity(), Path.RELEASE_TIMESLOT_URL + 
						PathParameter.uctime + unixMD5[0] + 
						PathParameter.hcode + unixMD5[1] + 
						PathParameter.timeslot_id + sp.getString(SharedInformation.timeslot_id, null));

				if (release_message.equals("Timeslot Released")) sp.edit().remove(SharedInformation.timeslot_id).commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class DoctorsDataParsing extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			releaseTimeslot();
			getDoctorsData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (doctors.size() == 0) Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
			list_doctor.setAdapter(new DoctorListAdapter());
			list_doctor.setOnItemClickListener(new DoctorListClickListener());
			pd.dismiss();
		}
	}

	private final class DoctorListClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> av, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String doctor_main_specialty = "";
			for (int i = 0; i < doctors.get(position).main_specialty.size(); i++) {
				doctor_main_specialty = doctor_main_specialty + ", " + doctors.get(position).main_specialty.get(i).specialty_name;
			}
			String doctor_education = "";
			for (int i = 0; i < doctors.get(position).education.size(); i++) {
				doctor_education = doctor_education + "* " + doctors.get(position).education.get(i).education_name + "\n    " + 
						doctors.get(position).education.get(i).education_short_name + " " + 
						doctors.get(position).education.get(i).education_date + "\n";
			}
			String doctor_fellowship = "";
			for (int i = 0; i < doctors.get(position).fellowship.size(); i++) {
				doctor_fellowship = doctor_fellowship + "* " + doctors.get(position).fellowship.get(i).fellowship_name + "\n    " + 
						doctors.get(position).fellowship.get(i).fellowship_description + "\n";
			}
			String doctor_specialty = "";
			for (int i = 0; i < doctors.get(position).specialty.size(); i++) {
				doctor_specialty = doctor_specialty + ", " + doctors.get(position).specialty.get(i).specialty_name;
			}
			String doctor_language = "";
			for (int i = 0; i < doctors.get(position).language.size(); i++) {
				doctor_language = doctor_language + "* " + doctors.get(position).language.get(i).doctor_language + "\n";
			}

			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new SearchDetailFragment();
				Bundle bundle = new Bundle();

				if (doctors != null) {
					bundle.putString(BundleInformation.specialty_id, specialty_id);
					bundle.putString(BundleInformation.specialty_name, specialty_name);
					bundle.putIntArray(BundleInformation.date, date_arr);
					bundle.putParcelableArrayList(BundleInformation.specialty, doctors.get(position).specialty);
					bundle.putParcelableArrayList(BundleInformation.timeslot, doctors.get(position).timeslot.get(0).timeslot_list);
					bundle.putString(BundleInformation.doctor_id, doctors.get(position).doctor_user_id);
					bundle.putString(BundleInformation.doctor_name, doctors.get(position).doctor_first_name + " " + doctors.get(position).doctor_last_name);
					bundle.putString(BundleInformation.doctor_profile_image, doctors.get(position).doctor_profile_image);
					bundle.putString(BundleInformation.doctor_professional_statement, doctors.get(position).doctor_professional_statement);
					bundle.putString(BundleInformation.doctor_main_specialty, doctor_main_specialty.substring(2, doctor_main_specialty.length()));
					bundle.putString(BundleInformation.doctor_specialty, doctor_specialty.substring(2, doctor_specialty.length()));
					bundle.putString(BundleInformation.place_id, doctors.get(position).place_id);
					bundle.putString(BundleInformation.place_address_line1, doctors.get(position).place_name);
					bundle.putString(BundleInformation.place_name, doctors.get(position).place_address_line1 + "\n" + doctors.get(position).place_country + " " + doctors.get(position).place_postal_code);
					bundle.putString(BundleInformation.doctor_education, "Education:\n" + doctor_education + "\n\n" + "Fellowship:\n" + doctor_fellowship);
					bundle.putString(BundleInformation.doctor_awards, "Language:\n" + doctor_language);
				}

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class ReasonForVisitOnItemChangeListener implements OnItemSelectedListener {
		private boolean isLoad;

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			if (position > 0) specialty_id = csvlist.getReason_for_visit_arr().get(position).id;
			if (isLoad) new DoctorsDataParsing().execute("");
			isLoad = true;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	private void calculateCalendar(int day, int month, int year) {

		month++;
		if (day-2 >= 1) {
			btn1.setText((day-2) + "/" + month);
		} else {
			btn1.setText((day-2+maxDate) + "/" + (month-1));
		}

		if (day-1 >= 1) {
			btn2.setText((day-1) + "/" + month);
		} else {
			btn2.setText((day-1+maxDate) + "/" + (month-1));
		}

		btn3.setText(day + "/" + month);

		if (day+1 <= maxDate) {
			btn4.setText((day+1) + "/" + month);
		} else {
			btn4.setText((day+1-maxDate) + "/" + (month+1));
		}

		if (day+2 <= maxDate) {
			btn5.setText((day+2) + "/" + month);
		} else {
			btn5.setText((day+2-maxDate) + "/" + (month+1));
		}
	}

	private final class SearchSuccessListener implements OnSuccessListener {
		@Override
		public void doSuccess(String result) {
			// TODO Auto-generated method stub
			reasonforvisit.setOnItemSelectedListener(new ReasonForVisitOnItemChangeListener());
			prev.setOnClickListener(new PreviousClickListener());
			next.setOnClickListener(new NextClickListener());
			new DoctorsDataParsing().execute("");
		}

		@Override
		public void doError() {
			// TODO Auto-generated method stub
			pd.dismiss();
			Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
		}
	}

	private final class PreviousClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (date_arr[0] > 1) {
				date_arr[0]--;
			} else {
				date_arr[0]--;
				date_arr[0] = date_arr[0] + maxDate; 
				date_arr[1]--;
			}
			calculateCalendar(date_arr[0], date_arr[1], date_arr[2]);
			new DoctorsDataParsing().execute("");
		}
	}

	private final class NextClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (date_arr[0] < maxDate) {
				date_arr[0]++;
			} else {
				date_arr[0]++;
				date_arr[0] = date_arr[0] - maxDate; 
				date_arr[1]++;
			}
			calculateCalendar(date_arr[0], date_arr[1], date_arr[2]);
			new DoctorsDataParsing().execute("");
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private int detectDisplayWidth() {
		int width = 0;
		if (  Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13 ) {
			Display display = getActivity().getWindowManager().getDefaultDisplay(); 
			width = display.getWidth();
		} else {
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
		}

		return width;
	}

	private final class DoctorListAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder viewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.search_result_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
				viewHolder.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
				viewHolder.doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
				viewHolder.place_address_line1 = (TextView) view.findViewById(R.id.place_address_line1);
				viewHolder.place_name = (TextView) view.findViewById(R.id.place_name);
				viewHolder.doctor_schedule = (LinearLayout) view.findViewById(R.id.doctor_schedule);
				viewHolder.doctor_schedule_line2 = (LinearLayout) view.findViewById(R.id.doctor_schedule_line2);
				viewHolder.doctor_schedule_line3 = (LinearLayout) view.findViewById(R.id.doctor_schedule_line3);

				view.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) view.getTag();
			}

			Doctors sub_doctor = getItem(position);

			viewHolder.doctor_image.setTag(Path.THUMBNAIL_URL + sub_doctor.doctor_profile_image);
			imageLoader.displayImage(Path.THUMBNAIL_URL + sub_doctor.doctor_profile_image, getActivity(), viewHolder.doctor_image);
			viewHolder.doctor_name.setText(sub_doctor.doctor_first_name + " " + sub_doctor.doctor_last_name);

			String doctor_specialty = "";
			for (int i = 0; i < sub_doctor.main_specialty.size(); i++) {
				doctor_specialty = doctor_specialty + ", " + sub_doctor.main_specialty.get(i).specialty_name;
			}
			doctor_specialty = doctor_specialty.substring(2, doctor_specialty.length());
			viewHolder.doctor_specialty.setText(doctor_specialty);

//			viewHolder.place_address_line1.setText(sub_doctor.place_address_line1);
//			viewHolder.place_name.setText(sub_doctor.place_name + " " + sub_doctor.place_country + " " + sub_doctor.place_postal_code);
			viewHolder.place_address_line1.setText(sub_doctor.place_name);
			viewHolder.place_name.setText(sub_doctor.place_address_line1 + "\n" + sub_doctor.place_country + " " + sub_doctor.place_postal_code);

			viewHolder.doctor_schedule.removeAllViews();
			viewHolder.doctor_schedule_line2.removeAllViews();
			viewHolder.doctor_schedule_line3.removeAllViews();

			int widthSoFar = 0;
			for (int i = 0; i < sub_doctor.timeslot.get(0).timeslot_list.size(); i++) {
				TextView doctor_time = new TextView(getActivity());
				doctor_time.setId(i);
				doctor_time.setText(sub_doctor.timeslot.get(0).timeslot_list.get(i).start_date.substring(11, 16));
				doctor_time.setTextSize(16);
				doctor_time.setTextColor(Color.WHITE);
				LayoutParams para = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				para.setMargins(7, 5, 7, 5);
				doctor_time.setLayoutParams(para);
				doctor_time.setPadding(12, 5, 12, 5);
				doctor_time.setBackgroundResource(R.drawable.main_schedule_bg);
				doctor_time.setOnClickListener(new ScheduleClickListener());
				doctor_time.setTag(position);

				doctor_time.measure(0, 0);
				viewHolder.doctor_schedule.measure(0, 0);
				viewHolder.doctor_schedule_line2.measure(0, 0);
				viewHolder.doctor_schedule_line3.measure(0, 0);
				widthSoFar += doctor_time.getMeasuredWidth() + 14 + 24; // 14 for extra margin, 24 for extra padding (both left right)
				int maxWidth = detectDisplayWidth();
				
				if (widthSoFar >= maxWidth) {
					viewHolder.doctor_schedule_line2.setVisibility(View.VISIBLE);

					if (widthSoFar >= (maxWidth*2)) {
						viewHolder.doctor_schedule_line3.setVisibility(View.VISIBLE);
						viewHolder.doctor_schedule_line3.addView(doctor_time);
					} else {
						viewHolder.doctor_schedule_line2.addView(doctor_time);
						widthSoFar = viewHolder.doctor_schedule_line2.getMeasuredWidth() + viewHolder.doctor_schedule.getMeasuredWidth();
					}
				} else {
					viewHolder.doctor_schedule.addView(doctor_time);
					widthSoFar = viewHolder.doctor_schedule.getMeasuredWidth();
				}
			}

			return view;
		}

		private final class ScheduleClickListener implements OnClickListener {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MD5TimePass md5 = new MD5TimePass();
				String[] unixMD5 = md5.getMD5();

				try {
					booking_parser = new BookingTimeslotJSONParser();
					booking_message = booking_parser.getJSONData(getActivity(), Path.BOOKING_TIMESLOT_URL + 
							PathParameter.uctime + unixMD5[0] + 
							PathParameter.hcode + unixMD5[1] + 
							PathParameter.timeslot_id + doctors.get((Integer)v.getTag()).timeslot.get(0).timeslot_list.get((Integer)v.getId()).timeslot_id);

					if (booking_message.equals("Timeslot Booked")) {
						SharedPreferences.Editor editor = sp.edit();
						editor.putString(SharedInformation.timeslot_id, doctors.get((Integer)v.getTag()).timeslot.get(0).timeslot_list.get((Integer)v.getId()).timeslot_id);
						editor.commit();

						String doctor_specialty = "";
						for (int i = 0; i < doctors.get((Integer)v.getTag()).main_specialty.size(); i++) {
							doctor_specialty = doctor_specialty + ", " + doctors.get((Integer)v.getTag()).main_specialty.get(i).specialty_name;
						}

						FragmentManager fm = getFragmentManager();

						if (fm != null) {
							FragmentTransaction ft = fm.beginTransaction();
							Fragment fragment = new BookingLoginFragment();
							Bundle bundle = new Bundle();

							bundle.putString(BundleInformation.specialty_id, specialty_id);
							bundle.putInt(BundleInformation.reason_for_visit_id, 0);
							bundle.putIntArray(BundleInformation.date, date_arr);
							bundle.putString(BundleInformation.time, doctors.get((Integer)v.getTag()).timeslot.get(0).timeslot_list.get((Integer)v.getId()).start_date.substring(11, 16));
							bundle.putParcelableArrayList(BundleInformation.specialty, doctors.get((Integer)v.getTag()).specialty);
							bundle.putString(BundleInformation.doctor_id, doctors.get((Integer)v.getTag()).doctor_user_id);
							bundle.putString(BundleInformation.doctor_name, doctors.get((Integer)v.getTag()).doctor_first_name + " " + doctors.get((Integer)v.getTag()).doctor_last_name);
							bundle.putString(BundleInformation.doctor_profile_image, doctors.get((Integer)v.getTag()).doctor_profile_image);
							bundle.putString(BundleInformation.doctor_specialty, doctor_specialty.substring(2, doctor_specialty.length()));
							bundle.putString(BundleInformation.place_id, doctors.get((Integer)v.getTag()).place_id);
							bundle.putString(BundleInformation.place_address_line1, doctors.get((Integer)v.getTag()).place_name);
							bundle.putString(BundleInformation.place_name, doctors.get((Integer)v.getTag()).place_address_line1 + "\n" + doctors.get((Integer)v.getTag()).place_country + " " + doctors.get((Integer)v.getTag()).place_postal_code);
							bundle.putString(BundleInformation.timeslot_id, doctors.get((Integer)v.getTag()).timeslot.get(0).timeslot_list.get((Integer)v.getId()).timeslot_id);

							fragment.setArguments(bundle);
							ft.replace(R.id.fragment_content, fragment);
							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
							ft.addToBackStack(null);
							ft.commit();
						}
					} else {
						Toast.makeText(getActivity(), "Timeslot is already booked or reserved", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public class ViewHolder {
			public ImageView doctor_image;
			public TextView doctor_name;
			public TextView doctor_specialty;
			public TextView place_address_line1;
			public TextView place_name;
			public LinearLayout doctor_schedule;
			public LinearLayout doctor_schedule_line2;
			public LinearLayout doctor_schedule_line3;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Doctors getItem(int position) {
			// TODO Auto-generated method stub
			return doctors.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return doctors.size();
		}
	}
}
