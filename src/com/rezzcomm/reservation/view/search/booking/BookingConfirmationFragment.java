package com.rezzcomm.reservation.view.search.booking;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.DownloadImageBitmap;
import com.rezzcomm.reservation.controller.parser.AddReservationJSONParser;
import com.rezzcomm.reservation.controller.parser.ReleaseTimeslotJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.MD5TimePass;
import com.rezzcomm.reservation.view.appointments.AppointmentsFragment;

public class BookingConfirmationFragment extends Fragment {

	private String specialty_id, timeslot_id;
	private String reservation_message;

	private int[] date_arr;
	private String time_arr;
	private String doctor_id, doctor_name_str, doctor_profile_image, doctor_specialty_str;
	private String place_id_str, place_address_line1_str, place_name_str;
	private String appointment_for_str, first_time_visit_str, patient_name_str, patient_id_str;
	private String release_message;

	private TextView title, doctor_name, doctor_specialty, place_address_line1, place_name, date, time, reason_visit;
	private EditText pname, first_time_visit;
	private ImageView doctor_image;
	private ProgressBar progress;
	private Button confirm_btn;

	private SharedPreferences sp;
	private DownloadImageBitmap downloadImage;
	private AddReservationJSONParser add_reservation;
	private ReleaseTimeslotJSONParser release_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.book_confirmation, container, false);

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

		confirm_btn = (Button) view.findViewById(R.id.confirm_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
			specialty_id 			= bundle.getString(BundleInformation.specialty_id);
			date_arr 				= bundle.getIntArray(BundleInformation.date);
			time_arr				= bundle.getString(BundleInformation.time);
			doctor_id 				= bundle.getString(BundleInformation.doctor_id);
			doctor_name_str 		= bundle.getString(BundleInformation.doctor_name);
			doctor_profile_image	= bundle.getString(BundleInformation.doctor_profile_image);
			doctor_specialty_str 	= bundle.getString(BundleInformation.doctor_specialty);
			place_id_str			= bundle.getString(BundleInformation.place_id);
			place_address_line1_str = bundle.getString(BundleInformation.place_address_line1);
			place_name_str 			= bundle.getString(BundleInformation.place_name);
			timeslot_id 			= bundle.getString(BundleInformation.timeslot_id);
			appointment_for_str		= bundle.getString(BundleInformation.appointment_for);
			first_time_visit_str	= bundle.getString(BundleInformation.first_time_visit);
			patient_name_str		= bundle.getString(BundleInformation.patient_name);
			patient_id_str			= bundle.getString(BundleInformation.patient_id);
		}

		title.setText("Book Your Appointment");

		doctor_image.setImageResource(R.drawable.default_thumb);
		doctor_name.setText(doctor_name_str);
		doctor_specialty.setText(doctor_specialty_str);
		place_address_line1.setText(place_address_line1_str);
		place_name.setText(place_name_str);

		date.setText("Date: " + date_arr[0] + " / " + (date_arr[1]+1) + " / " + date_arr[2]);
		time.setText("Time: " + time_arr);

		reason_visit.setText("Clinical Anaesthesia");

		pname.setText(patient_name_str);
		if (first_time_visit_str.equals("First+Time")) first_time_visit.setText("Yes");
		else if (first_time_visit_str.equals("Visited")) first_time_visit.setText("No");

		confirm_btn.setOnClickListener(new ConfirmButtonClickListener());

		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

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

	private void releaseTimeslot() {
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

	private final class ConfirmButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			releaseTimeslot();

			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();

			try {
				add_reservation = new AddReservationJSONParser();
				reservation_message = add_reservation.getJSONData(getActivity(), Path.ADD_RESERVATION_URL + 
						PathParameter.uctime + unixMD5[0] + 
						PathParameter.hcode + unixMD5[1] + 
						PathParameter.patient_id + patient_id_str + 
						PathParameter.specialty_id + specialty_id + 
						PathParameter.doctor_id + doctor_id +
						PathParameter.place_id + place_id_str +
						PathParameter.timeslot_id + timeslot_id +
						PathParameter.first_time + first_time_visit_str +
						PathParameter.appt_for + appointment_for_str);

				if (!reservation_message.equals("Timeslot is already booked or reserved")) {

					final TextView message = new TextView(getActivity());
					message.setText(Html.fromHtml("Your appointment is confirmed.\nYour confirmation ID: <b>" + reservation_message + 
							"</b>\nYou can retrieve your bookings from “<b>My Appts</b>” tab."));
					message.setGravity(Gravity.CENTER);
					message.setTextSize(16);
					message.setTextColor(Color.WHITE);
					message.setPadding(25, 10, 25, 10);

					Builder ad = new AlertDialog.Builder(getActivity());
					ad.setTitle("Congratulations!");
					ad.setView(message);
					ad.setPositiveButton("OK", new ConfirmClickListener());
					ad.show();
				} else {
					Toast.makeText(getActivity(), "Timeslot is already booked or reserved.", Toast.LENGTH_SHORT).show();
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

	private final class ConfirmClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content, new AppointmentsFragment());
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		}
	}
}
