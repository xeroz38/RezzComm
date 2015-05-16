package com.rezzcomm.reservation.view.search.booking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.DownloadImageBitmap;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Doctors_Specialty;

public class BookingDetailFragment extends Fragment {

	private String specialty_id, timeslot_id;
	private int reason_for_visit_id;
	private int[] date_arr;
	private String time_arr;
	private String doctor_id, doctor_name_str, doctor_profile_image, doctor_specialty_str;
	private String place_id_str, place_address_line1_str, place_name_str;

	private RadioButton myself_radio, others_radio;
	private RadioButton first_time_yes, first_time_no;
	private EditText pname, contact_no, email;
	private TextView title, doctor_name, doctor_specialty, place_address_line1, place_name, date, time;
	private ImageView doctor_image;
	private Button next_btn;

	private ProgressBar progress;
	private Spinner reasonforvisit;
	private Calendar calendar;

	private SharedPreferences sp;
	private DownloadImageBitmap downloadImage;
	private ArrayList<Doctors_Specialty> reason_for_visit;
	private ArrayAdapter<String> dataAdapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.book_detail, container, false);

		title = (TextView) view.findViewById(R.id.title);

		reasonforvisit = (Spinner) view.findViewById(R.id.reasonforvisit);

		progress = (ProgressBar) view.findViewById(R.id.progress);
		doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
		doctor_name = (TextView) view.findViewById(R.id.doctor_name);
		doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
		place_address_line1 = (TextView) view.findViewById(R.id.place_address_line1);
		place_name = (TextView) view.findViewById(R.id.place_name);

		date = (TextView) view.findViewById(R.id.date);
		time = (TextView) view.findViewById(R.id.time);

		myself_radio = (RadioButton) view.findViewById(R.id.radio_myself);
		others_radio = (RadioButton) view.findViewById(R.id.radio_others);
		pname = (EditText) view.findViewById(R.id.pname);
		contact_no = (EditText) view.findViewById(R.id.contactno);
		email = (EditText) view.findViewById(R.id.email);
		first_time_yes = (RadioButton) view.findViewById(R.id.radio_visityes);
		first_time_no = (RadioButton) view.findViewById(R.id.radio_visitno);

		next_btn = (Button) view.findViewById(R.id.next_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
			specialty_id 			= bundle.getString(BundleInformation.specialty_id);
			reason_for_visit_id		= bundle.getInt(BundleInformation.reason_for_visit_id);
			date_arr 				= bundle.getIntArray(BundleInformation.date);
			time_arr 				= bundle.getString(BundleInformation.time);
			reason_for_visit		= bundle.getParcelableArrayList(BundleInformation.specialty);
			doctor_id 				= bundle.getString(BundleInformation.doctor_id);
			doctor_name_str 		= bundle.getString(BundleInformation.doctor_name);
			doctor_profile_image	= bundle.getString(BundleInformation.doctor_profile_image);
			doctor_specialty_str 	= bundle.getString(BundleInformation.doctor_specialty);
			place_id_str		 	= bundle.getString(BundleInformation.place_id);
			place_address_line1_str = bundle.getString(BundleInformation.place_address_line1);
			place_name_str 			= bundle.getString(BundleInformation.place_name);
			timeslot_id				= bundle.getString(BundleInformation.timeslot_id);
		}

		title.setText("Book Your Appointment");

		final ArrayList<String> tempArr = new ArrayList<String>();
		tempArr.add("Choose reason for visit");
		for (int i = 0; i < reason_for_visit.size(); i++) {
			tempArr.add(reason_for_visit.get(i).specialty_name);
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

		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

		calendar = Calendar.getInstance();
		if (date_arr == null) {
			// from favourite fragment
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			date_arr = new int[3];
			date_arr[0] = today.monthDay;
			date_arr[1] = today.month;
			date_arr[2] = today.year;
		} else {
			// from search result fragment
			calendar.set(date_arr[2], date_arr[1], date_arr[0]);
		}

		doctor_image.setImageResource(R.drawable.default_thumb);
		doctor_name.setText(doctor_name_str);
		doctor_specialty.setText(doctor_specialty_str);
		place_address_line1.setText(place_address_line1_str);
		place_name.setText(place_name_str);

		date.setText("Date: " + date_arr[0] + " / " + (date_arr[1]+1) + " / " + date_arr[2]);
		time.setText("Time: " + time_arr);

		others_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					pname.setFocusable(true);
					pname.setFocusableInTouchMode(true);
					contact_no.setFocusable(true);
					contact_no.setFocusableInTouchMode(true);
					email.setFocusable(true);
					email.setFocusableInTouchMode(true);
				} else {
					pname.setFocusable(false);
					pname.setFocusableInTouchMode(false);
					contact_no.setFocusable(false);
					contact_no.setFocusableInTouchMode(false);
					email.setFocusable(false);
					email.setFocusableInTouchMode(false);
				}
			}
		});

		next_btn.setOnClickListener(new NextClickListener());

		new DownloadImage().execute("");
	}

	private class DownloadImage extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			downloadImage = new DownloadImageBitmap(doctor_profile_image);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (downloadImage.getBitmap() != null) doctor_image.setImageBitmap(downloadImage.getBitmap());
			else doctor_image.setImageResource(R.drawable.default_thumb);
			progress.setVisibility(View.GONE);
		}
	}

	private final class NextClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String appts_for = "For+Patient";
			String first_time_visit = "First+Time";
			String patient_name = sp.getString(SharedInformation.last_name, "Guest");

			if (first_time_yes.isChecked()) {
				first_time_visit = "First+Time";
			} else if (first_time_no.isChecked()) { 
				first_time_visit = "Visited";
			}

			if (others_radio.isChecked()) {
				Pattern pattern_email = Patterns.EMAIL_ADDRESS;

				if (pname.getText().toString().trim().equals("")) {
					Toast.makeText(getActivity(), "Enter patient name", Toast.LENGTH_SHORT).show();
				} else if (contact_no.getText().toString().trim().equals("")) {
					Toast.makeText(getActivity(), "Enter contact number", Toast.LENGTH_SHORT).show();
				} else if (email.getText().toString().trim().equals("")) {
					Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
				} else if (!pattern_email.matcher(email.getText().toString()).matches()) {
					Toast.makeText(getActivity(), "Enter correct email", Toast.LENGTH_SHORT).show();
				} else {
					appts_for = "For+Patient+Peer" + 
							"&peer_name=" + pname.getText().toString() + 
							"&peer_tel_no=" + contact_no.getText().toString() + 
							"&peer_email=" + email.getText().toString();
					patient_name = pname.getText().toString();
					goToNext(appts_for, first_time_visit, patient_name);
				}
			} else if (myself_radio.isChecked()) { 
				appts_for = "For+Patient";
				patient_name = sp.getString(SharedInformation.last_name, "Guest");
				goToNext(appts_for, first_time_visit, patient_name);
			}
		}

		private void goToNext(String appts_for, String first_time_visit, String patient_name) {
			// TODO Auto-generated method stub
			if (reason_for_visit_id > 0) specialty_id = reason_for_visit.get(reasonforvisit.getSelectedItemPosition()-1).specialty_id;

			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new BookingConfirmationFragment();
				Bundle bundle = new Bundle();

				bundle.putString(BundleInformation.specialty_id, specialty_id);
				bundle.putIntArray(BundleInformation.date, date_arr);
				bundle.putString(BundleInformation.time, time_arr);
				bundle.putString(BundleInformation.doctor_id, doctor_id);
				bundle.putString(BundleInformation.doctor_name, doctor_name_str);
				bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
				bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
				bundle.putString(BundleInformation.place_id, place_id_str);
				bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
				bundle.putString(BundleInformation.place_name, place_name_str);
				bundle.putString(BundleInformation.timeslot_id, timeslot_id);

				bundle.putString(BundleInformation.appointment_for, appts_for);
				bundle.putString(BundleInformation.first_time_visit, first_time_visit);
				bundle.putString(BundleInformation.patient_name, patient_name);
				bundle.putString(BundleInformation.patient_id, sp.getString(SharedInformation.patient_id, "0"));

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}
}
