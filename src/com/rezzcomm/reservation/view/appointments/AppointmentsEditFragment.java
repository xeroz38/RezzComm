package com.rezzcomm.reservation.view.appointments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.DownloadImageBitmap;
import com.rezzcomm.reservation.controller.parser.BookingTimeslotJSONParser;
import com.rezzcomm.reservation.controller.parser.DoctorDetailsJSONParser;
import com.rezzcomm.reservation.controller.parser.EditReservationJSONParser;
import com.rezzcomm.reservation.controller.parser.ReleaseTimeslotJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Doctors;
import com.rezzcomm.reservation.model.Doctors_Specialty;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.model.Timeslot_list;
import com.rezzcomm.reservation.util.MD5TimePass;

public class AppointmentsEditFragment extends Fragment {

	private String timeslot_id, reservation_id, reason_for_visit_id;
	private String doctor_id, doctor_name_str, doctor_profile_image, doctor_specialty_str;
	private String place_id_str, place_address_line1_str, place_name_str;
	private String appt_for, first_time_visit_str;
	private String patient_name, patient_contact, patient_email, reservation_date_str;
	private String message, booking_message, release_message;

	private int selectID = 0;
	private String selectrfvID = "";

	private RadioButton myself_radio, others_radio;
	private RadioButton first_time_yes, first_time_no;
	private EditText pname, contact_no, email;
	private TextView title, doctor_name, doctor_specialty, place_address_line1, place_name, date, time;
	private ImageView doctor_image;
	private LinearLayout doctor_schedule, doctor_schedule_line2;
	private Button edit_btn;

	private DatePickerDialog dateDialog;
	private ProgressBar progress;
	private Spinner reasonforvisit;

	private SharedPreferences sp;
	private Doctors doctor;
	private ArrayList<Timeslot_list> timeslot;
	private ArrayList<Doctors_Specialty> reason_for_visit;
	private ArrayAdapter<String> dataAdapter;
	private DownloadImageBitmap downloadImage;
	private EditReservationJSONParser edit_parser;
	private DoctorDetailsJSONParser doctor_detail_parser;
	private BookingTimeslotJSONParser booking_parser;
	private ReleaseTimeslotJSONParser release_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.appointments_edit, container, false);

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

		doctor_schedule = (LinearLayout) view.findViewById(R.id.doctor_schedule);
		doctor_schedule_line2 = (LinearLayout) view.findViewById(R.id.doctor_schedule_line2);

		myself_radio = (RadioButton) view.findViewById(R.id.radio_myself);
		others_radio = (RadioButton) view.findViewById(R.id.radio_others);
		pname = (EditText) view.findViewById(R.id.pname);
		contact_no = (EditText) view.findViewById(R.id.contactno);
		email = (EditText) view.findViewById(R.id.email);
		first_time_yes = (RadioButton) view.findViewById(R.id.radio_visityes);
		first_time_no = (RadioButton) view.findViewById(R.id.radio_visitno);

		edit_btn = (Button) view.findViewById(R.id.update_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
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
		}

		title.setText("Edit My Appointments");

		final ArrayList<String> tempArr = new ArrayList<String>();
		for (int i = 0; i < reason_for_visit.size(); i++) {
			tempArr.add(reason_for_visit.get(i).specialty_name);
			if (reason_for_visit.get(i).specialty_id.equals(reason_for_visit_id)) {
				selectID = i;
				selectrfvID = reason_for_visit.get(i).specialty_id;
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
		reasonforvisit.setSelection(selectID);

		doctor_image.setImageResource(R.drawable.default_thumb);
		doctor_name.setText(doctor_name_str);
		doctor_specialty.setText(doctor_specialty_str);
		place_address_line1.setText(place_address_line1_str);
		place_name.setText(place_name_str);

		final String[] datetime = reservation_date_str.split("T");
		date.setText("Date: " + datetime[0]);
		time.setText("Time: " + datetime[1].substring(0, 5));
		date.setOnClickListener(new DateClickListener());
		time.setOnClickListener(new DateClickListener());

		if (appt_for.equals("For_Patient")) {
			myself_radio.setChecked(true);
		} else if (appt_for.equals("For_Patient_Peer")) {
			pname.setText(patient_name);
			contact_no.setText(patient_contact);
			email.setText(patient_email);
			others_radio.setChecked(true);
			pname.setFocusable(true);
			pname.setFocusableInTouchMode(true);
			contact_no.setFocusable(true);
			contact_no.setFocusableInTouchMode(true);
			email.setFocusable(true);
			email.setFocusableInTouchMode(true);
		}

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

		if (first_time_visit_str.equals("First_Time")) first_time_yes.setChecked(true);
		else if (first_time_visit_str.equals("Visited")) first_time_no.setChecked(true);

		final String[] datetimesetdialog = reservation_date_str.split("-");
		releaseTimeslot();
		dateDialog = new DatePickerDialog(getActivity(), new DateSetListener(), 
				Integer.parseInt(datetimesetdialog[0]),						// year
				Integer.parseInt(datetimesetdialog[1])-1,					// month
				Integer.parseInt(datetimesetdialog[2].substring(0, 2))		// date
				);	

		edit_btn.setOnClickListener(new UpdateClickListener());

		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
		new DownloadImage().execute("");
	}

	private final class DateSetListener implements OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();
			monthOfYear++;

			try {
				doctor_detail_parser = new DoctorDetailsJSONParser();
				doctor = doctor_detail_parser.getJSONData(Path.DOCTOR_DETAIL_URL + 
						PathParameter.uctime + unixMD5[0] + 
						PathParameter.hcode + unixMD5[1] + 
						PathParameter.doctor_id + doctor_id +
						PathParameter.place_id + place_id_str +
						PathParameter.start_date + year + "/" + monthOfYear + "/" + dayOfMonth +
						PathParameter.end_date + year + "/" + monthOfYear + "/" + dayOfMonth);

				timeslot = doctor.timeslot.get(0).timeslot_list;

				if (doctor.timeslot.get(0).timeslot_list.size() > 0) {
					date.setText("Date: " + year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
					time.setText("Time: " + "--:--");
					calculateTimeslot();
				} else {
					doctor_schedule.removeAllViews();
					doctor_schedule_line2.removeAllViews();
					Toast.makeText(getActivity(), "There is no available timeslot", Toast.LENGTH_SHORT).show();
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
	}

	private final class DateClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dateDialog.setTitle("Edit Appointment");
			dateDialog.setMessage("Change appointment date: ");
			dateDialog.show();
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

	private void calculateTimeslot() {
		// TODO Auto-generated method stub
		int widthSoFar = 0;
		doctor_schedule.removeAllViews();
		doctor_schedule_line2.removeAllViews();

		for (int i = 0; i < timeslot.size(); i++) {
			TextView doctor_time = new TextView(getActivity());
			doctor_time.setId(i);
			doctor_time.setText(timeslot.get(i).start_date.substring(11, 16));
			doctor_time.setTextSize(16);
			doctor_time.setTextColor(Color.WHITE);
			LayoutParams para = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			para.setMargins(7, 5, 7, 5);
			doctor_time.setLayoutParams(para);
			doctor_time.setPadding(12, 5, 12, 5);
			doctor_time.setBackgroundResource(R.drawable.main_schedule_bg);
			doctor_time.setOnClickListener(new TimeslotClickListener());

			doctor_time.measure(0, 0);
			doctor_schedule.measure(0, 0);
			doctor_schedule_line2.measure(0, 0);
			widthSoFar += doctor_time.getMeasuredWidth() + 14 + 24; // 14 for extra margin, 24 for extra padding (both left right)
			int maxWidth = detectDisplayWidth();

			if (widthSoFar >= maxWidth) {
				doctor_schedule_line2.setVisibility(View.VISIBLE);
				doctor_schedule_line2.addView(doctor_time);
			} else {
				doctor_schedule.addView(doctor_time);
				widthSoFar = doctor_schedule.getMeasuredWidth();
			}
		}
	}

	private final class TimeslotClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			releaseTimeslot();

			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();

			try {
				booking_parser = new BookingTimeslotJSONParser();
				booking_message = booking_parser.getJSONData(getActivity(), Path.BOOKING_TIMESLOT_URL + 
						PathParameter.uctime + unixMD5[0] + 
						PathParameter.hcode + unixMD5[1] + 
						PathParameter.timeslot_id + doctor.timeslot.get(0).timeslot_list.get((Integer)v.getId()).timeslot_id);

				if (booking_message.equals("Timeslot Booked")) {
					SharedPreferences.Editor editor = sp.edit();
					editor.putString(SharedInformation.timeslot_id, doctor.timeslot.get(0).timeslot_list.get((Integer)v.getId()).timeslot_id);
					editor.commit();

					timeslot_id = doctor.timeslot.get(0).timeslot_list.get((Integer)v.getId()).timeslot_id;
					time.setText("Time: " + doctor.timeslot.get(0).timeslot_list.get((Integer)v.getId()).start_date.substring(11, 16));

					doctor_schedule.removeAllViews();
					doctor_schedule_line2.removeAllViews();
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

	private final class UpdateClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!time.getText().equals("Time: " + "--:--")) {

				String appts_for = "For+Patient";
				String first_time_visit = "First+Time";

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
						updateAppointments(appts_for, first_time_visit);
					}
				} else if (myself_radio.isChecked()) { 
					appts_for = "For+Patient";
					updateAppointments(appts_for, first_time_visit);
				}
			} else {
				Toast.makeText(getActivity(), "Choose timeslot", Toast.LENGTH_SHORT).show();
			}
		}

		private void updateAppointments(String appts_for, String first_time_visit) {
			// TODO Auto-generated method stub
			releaseTimeslot();

			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();

			try {
				edit_parser = new EditReservationJSONParser();
				message = edit_parser.getJSONData(getActivity(), Path.EDIT_RESERVATION_URL + 
						PathParameter.uctime + unixMD5[0] + 
						PathParameter.hcode + unixMD5[1] + 
						PathParameter.patient_id + sp.getString(SharedInformation.patient_id, "Guest") + 
						PathParameter.specialty_id + selectrfvID + 
						PathParameter.doctor_id + doctor_id + 
						PathParameter.place_id + place_id_str + 
						PathParameter.timeslot_id + timeslot_id + 
						PathParameter.reservation_id + reservation_id + 
						PathParameter.first_time + first_time_visit +
						PathParameter.appt_for + appts_for);

				if (message.equals("Reservation is updated")) {
					FragmentManager fm = getFragmentManager();

					if (fm != null) {
						FragmentTransaction ft = fm.beginTransaction();
						ft.replace(R.id.fragment_content, new AppointmentsFragment());
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
						ft.commit();
					}
					Toast.makeText(getActivity(), "Edit Appointments Success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "Edit Appointments Failed", Toast.LENGTH_SHORT).show();
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
	}
}
