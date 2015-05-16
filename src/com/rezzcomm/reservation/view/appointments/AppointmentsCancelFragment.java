package com.rezzcomm.reservation.view.appointments;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.rezzcomm.reservation.controller.parser.CancelReservationJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.MD5TimePass;

public class AppointmentsCancelFragment extends Fragment {

	private String reservation_id;
	private String doctor_name_str, doctor_profile_image, doctor_specialty_str;
	private String reason_visit_str, reservation_date_str;
	private String message;
	
	private TextView date, time;
	private TextView title, doctor_name, specialty, reason_visit, cancel_doctor;
	private EditText cancel_reason;
	private ImageView doctor_image;
	private Button cancel_btn;
	
	private ProgressBar progress;
	private CancelReservationJSONParser cancel_parser;
	private DownloadImageBitmap downloadImage;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.appointments_cancel, container, false);

		title = (TextView) view.findViewById(R.id.title);

		progress = (ProgressBar) view.findViewById(R.id.progress);
		doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
		doctor_name = (TextView) view.findViewById(R.id.doctor_name);
		specialty = (TextView) view.findViewById(R.id.specialty);
		
		date = (TextView) view.findViewById(R.id.date);
		time = (TextView) view.findViewById(R.id.time);
		
		reason_visit = (TextView) view.findViewById(R.id.reason_visit);
		cancel_doctor = (TextView) view.findViewById(R.id.cancel_doctor);
		cancel_reason = (EditText) view.findViewById(R.id.cancel_reason);

		cancel_btn = (Button) view.findViewById(R.id.cancel_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
			doctor_name_str 		= bundle.getString(BundleInformation.doctor_name);
			doctor_profile_image 	= bundle.getString(BundleInformation.doctor_profile_image);
			doctor_specialty_str 	= bundle.getString(BundleInformation.doctor_specialty);
			reservation_date_str 	= bundle.getString(BundleInformation.date);
			reservation_id 			= bundle.getString(BundleInformation.reservation_id);
			reason_visit_str 		= bundle.getString(BundleInformation.reason_for_visit);
		}
		
		title.setText("Cancel My Appointments");

//		Drawable doctor_image = getActivity().getResources().getDrawable(R.drawable.default_thumb);
//		doctor_name.setCompoundDrawablePadding(15);
//		doctor_name.setCompoundDrawablesWithIntrinsicBounds(doctor_image, null, null, null);
		doctor_image.setImageResource(R.drawable.default_thumb);
		doctor_name.setText(doctor_name_str);
		specialty.setText("Specialty:\n" + doctor_specialty_str);
		
		final String[] datetime = reservation_date_str.split("T");
		date.setText("Date: " + datetime[0]);
		time.setText("Time: " + datetime[1].substring(0, 5));
		
		reason_visit.setText("Reason of Visit:\n" + reason_visit_str);
		cancel_doctor.setText("I would like to cancel this appt with Dr." + doctor_name_str + ".");
		cancel_btn.setOnClickListener(new ConfirmCancelClickListener());
		
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

	private final class CancelClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();
			
			try {
				cancel_parser = new CancelReservationJSONParser();
				message = cancel_parser.getJSONData(getActivity(), Path.CANCEL_RESERVATION_URL + 
								PathParameter.uctime + unixMD5[0] + 
								PathParameter.hcode + unixMD5[1] + 
								PathParameter.reservation_id + reservation_id + 
								PathParameter.reservation_reason + cancel_reason.getText().toString());
				
				if (message.equals("Reservation is cancelled!")) {
					
					FragmentManager fm = getFragmentManager();

					if (fm != null) {
						FragmentTransaction ft = fm.beginTransaction();
						ft.replace(R.id.fragment_content, new AppointmentsFragment());
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						ft.commit();
					}
					Toast.makeText(getActivity(), "Reservation is cancelled!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "Reservation does not exist.", Toast.LENGTH_SHORT).show();
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

	private final class ConfirmCancelClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (cancel_reason.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter reason for cancellation", Toast.LENGTH_SHORT).show();
			} else {
				
				final TextView message = new TextView(getActivity());
				message.setText("Your appointment with " + doctor_name_str + " is cancelled.");
				message.setGravity(Gravity.CENTER);
				message.setTextSize(16);
				message.setTextColor(Color.WHITE);
				message.setPadding(25, 10, 25, 10);

				Builder ad = new AlertDialog.Builder(getActivity());
				ad.setTitle("Successful");
				ad.setView(message);
				ad.setNegativeButton("Cancel", null);
				ad.setPositiveButton("OK", new CancelClickListener());
				ad.show();
			}
		}
	}
}
