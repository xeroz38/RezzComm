package com.rezzcomm.reservation.view.appointments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.DownloadImageBitmap;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.Doctors_Specialty;

public class AppointmentsDetailFragment extends Fragment {

	private String specialty_id, timeslot_id, reservation_id, reason_for_visit_id;
	private String doctor_id, doctor_name_str, doctor_profile_image, doctor_specialty_str;
	private String place_id_str, place_address_line1_str, place_name_str;
	private String appt_for, first_time_visit_str;
	private String patient_name, patient_contact, patient_email, reason_visit_str, reservation_date_str;

	private TextView date, time;
	private TextView title, doctor_name, doctor_specialty, place_address_line1, place_name, reason_visit;
	private EditText pname, first_time_visit;
	private ImageView doctor_image;
	private Button edit_btn, cancel_btn, add_to_calendar, share_this_btn;

	private ProgressBar progress;
	private ArrayList<Doctors_Specialty> reason_for_visit;
	private DownloadImageBitmap downloadImage;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.appointments_detail, container, false);

		title = (TextView) view.findViewById(R.id.title);

		progress = (ProgressBar) view.findViewById(R.id.progress);
		doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
		doctor_name = (TextView) view.findViewById(R.id.doctor_name);
		doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
		place_address_line1 = (TextView) view.findViewById(R.id.place_address_line1);
		place_name = (TextView) view.findViewById(R.id.place_name);

		date = (TextView) view.findViewById(R.id.date);
		time = (TextView) view.findViewById(R.id.time);

		reason_visit = (TextView) view.findViewById(R.id.reason_visit);

		pname = (EditText) view.findViewById(R.id.pname);
		first_time_visit = (EditText) view.findViewById(R.id.first_time_visit);

		edit_btn = (Button) view.findViewById(R.id.edit_btn);
		cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
		add_to_calendar = (Button) view.findViewById(R.id.add_calender_btn);
		share_this_btn = (Button) view.findViewById(R.id.share_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
			specialty_id 			= bundle.getString(BundleInformation.specialty_id);
			reason_for_visit_id		= bundle.getString(BundleInformation.reason_for_visit_id);
			reason_for_visit		= bundle.getParcelableArrayList(BundleInformation.specialty);
			reservation_date_str 	= bundle.getString(BundleInformation.date);
			doctor_id 				= bundle.getString(BundleInformation.doctor_id); 
			doctor_name_str 		= bundle.getString(BundleInformation.doctor_name);
			doctor_profile_image 	= bundle.getString(BundleInformation.doctor_profile_image);
			doctor_specialty_str 	= bundle.getString(BundleInformation.doctor_specialty);
			place_id_str 			= bundle.getString(BundleInformation.place_id);
			place_address_line1_str = bundle.getString(BundleInformation.place_address_line1);
			place_name_str 			= bundle.getString(BundleInformation.place_name);
			timeslot_id 			= bundle.getString(BundleInformation.timeslot_id);
			reservation_id 			= bundle.getString(BundleInformation.reservation_id);
			appt_for 				= bundle.getString(BundleInformation.appointment_for);
			first_time_visit_str 	= bundle.getString(BundleInformation.first_time_visit);
			patient_name 			= bundle.getString(BundleInformation.patient_name);
			patient_contact 		= bundle.getString(BundleInformation.patient_tel_no);
			patient_email 			= bundle.getString(BundleInformation.patient_email);
			reason_visit_str 		= bundle.getString(BundleInformation.reason_for_visit);
		}

		title.setText("My Appointment");

		doctor_image.setImageResource(R.drawable.default_thumb);
		doctor_name.setText(doctor_name_str);
		doctor_specialty.setText(doctor_specialty_str);
		place_address_line1.setText(place_address_line1_str);
		place_name.setText(place_name_str);

		final String[] datetime = reservation_date_str.split("T");
		date.setText("Date: " + datetime[0]);
		time.setText("Time: " + datetime[1].substring(0, 5));

		reason_visit.setText(reason_visit_str);

		pname.setText(patient_name);
		if (first_time_visit_str.equals("First_Time")) first_time_visit.setText("Yes");
		if (first_time_visit_str.equals("Visited")) first_time_visit.setText("No");

		edit_btn.setOnClickListener(new EditClickListener());
		cancel_btn.setOnClickListener(new CancelClickListener());
		add_to_calendar.setOnClickListener(new AddToCalendarClickListener());
		share_this_btn.setOnClickListener(new ShareClickListener());

		new DownloadImage().execute("");
	}

	private final class AddToCalendarClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String eventUriString = "content://com.android.calendar/events";
			ContentValues eventValues = new ContentValues();

			String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			long startDate = 0;
			long endDate = 0;

			try {
				startDate = dateFormat.parse(reservation_date_str).getTime();
				endDate = startDate + 1000 * 60 * 60; // For next 1hr
			} catch (ParseException e) {
				e.printStackTrace();
			}

			eventValues.put("calendar_id", 1); 
			eventValues.put("title", "RezzComm");
			eventValues.put("description", "RezzComm is a free online reservation service that helps patients find doctors and book appointments.");
			eventValues.put("eventLocation", "Singapore");
			eventValues.put("dtstart", startDate);
			eventValues.put("dtend", endDate);
			eventValues.put("eventStatus", 0); 
			eventValues.put("visibility", 3); 
			eventValues.put("transparency", 0); 
			eventValues.put("hasAlarm", 1); 

			Uri eventUri = getActivity().getContentResolver().insert(Uri.parse(eventUriString), eventValues);

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(eventUri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
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
	
	private final class ShareClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"RezzComm");
			intent.putExtra(android.content.Intent.EXTRA_TEXT, "RezzComm is a free online reservation service that helps patients find doctors and book appointments.\n" +
					"Our vision is to provide innovative online reservation service to make it universally accessible for users worldwide.");
			startActivity(Intent.createChooser(intent,"Share via"));
		}
	}

	private final class EditClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new AppointmentsEditFragment();
				Bundle bundle = new Bundle();

				bundle.putString(BundleInformation.specialty_id, specialty_id);
				bundle.putParcelableArrayList(BundleInformation.specialty, reason_for_visit);
				bundle.putString(BundleInformation.date, reservation_date_str);
				bundle.putString(BundleInformation.doctor_id, doctor_id);
				bundle.putString(BundleInformation.doctor_name, doctor_name_str);
				bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
				bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
				bundle.putString(BundleInformation.place_id, place_id_str);
				bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
				bundle.putString(BundleInformation.place_name, place_name_str);
				bundle.putString(BundleInformation.timeslot_id, timeslot_id);
				bundle.putString(BundleInformation.reservation_id, reservation_id);
				bundle.putString(BundleInformation.reason_for_visit_id, reason_for_visit_id);
				bundle.putString(BundleInformation.reason_for_visit, reason_visit_str);
				bundle.putString(BundleInformation.appointment_for, appt_for);
				bundle.putString(BundleInformation.first_time_visit, first_time_visit_str);
				bundle.putString(BundleInformation.patient_name, patient_name);
				bundle.putString(BundleInformation.patient_tel_no, patient_contact);
				bundle.putString(BundleInformation.patient_email, patient_email);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class CancelClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new AppointmentsCancelFragment();
				Bundle bundle = new Bundle();

				bundle.putString(BundleInformation.specialty_id, specialty_id);
				bundle.putString(BundleInformation.date, reservation_date_str);
				bundle.putString(BundleInformation.doctor_id, doctor_id);
				bundle.putString(BundleInformation.doctor_name, doctor_name_str);
				bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
				bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
				bundle.putString(BundleInformation.place_id, place_id_str);
				bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
				bundle.putString(BundleInformation.place_name, place_name_str);
				bundle.putString(BundleInformation.timeslot_id, timeslot_id);
				bundle.putString(BundleInformation.reservation_id, reservation_id);
				bundle.putString(BundleInformation.reason_for_visit_id, reason_for_visit_id);
				bundle.putString(BundleInformation.reason_for_visit, reason_visit_str);
				bundle.putString(BundleInformation.appointment_for, appt_for);
				bundle.putString(BundleInformation.first_time_visit, first_time_visit_str);
				bundle.putString(BundleInformation.patient_name, patient_name);
				bundle.putString(BundleInformation.patient_tel_no, patient_contact);
				bundle.putString(BundleInformation.patient_email, patient_email);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}
}
