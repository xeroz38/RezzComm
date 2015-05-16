package com.rezzcomm.reservation.view.appointments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.ImageLoader;
import com.rezzcomm.reservation.controller.NetRequest;
import com.rezzcomm.reservation.controller.NetRequest.OnSuccessListener;
import com.rezzcomm.reservation.controller.parser.PatientReservationJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.model.PatientReservation;
import com.rezzcomm.reservation.util.MD5TimePass;

public class AppointmentsFragment extends Fragment {

	private TextView title, noappts, upcoming_appts_title, past_appts_title;
	private ListView upcoming_appts, past_appts;

	private ProgressDialog pd;
	private LayoutInflater inflater;
	
	private ImageLoader imageLoader;
	private SharedPreferences sp;
	private ArrayList<PatientReservation> reservation;
	private ArrayList<PatientReservation> upcomings_reservation;
	private ArrayList<PatientReservation> pasts_reservation;
	private PatientReservationJSONParser reservation_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.appointments_fragment, container, false);

		title = (TextView) view.findViewById(R.id.title);
		upcoming_appts_title = (TextView) view.findViewById(R.id.upcoming_appts_title);
		past_appts_title = (TextView) view.findViewById(R.id.past_appts_title);
		noappts = (TextView) view.findViewById(R.id.noappts);

		upcoming_appts = (ListView) view.findViewById(R.id.upcoming_appts);
		past_appts = (ListView) view.findViewById(R.id.past_appts);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		title.setText("My Appointment");
		
		pd = new ProgressDialog(getActivity());
//		pd.setIcon(R.drawable.default_thumb);
		pd.setTitle("Please Wait");
		pd.setMessage("Loading...");
		pd.show();
		
		upcomings_reservation = new ArrayList<PatientReservation>();
		pasts_reservation = new ArrayList<PatientReservation>();
		
		upcoming_appts.setAdapter(new UpcomingListAdapter());
		past_appts.setAdapter(new PastListAdapter());
		upcoming_appts.setOnItemClickListener(new UpcomingClickListener());
		
		imageLoader = new ImageLoader(getActivity());
		
		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
		if (sp.getString(SharedInformation.patient_id, null) == null) {
			FragmentManager fm = getFragmentManager();
			
			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content, new AppointmentsLoginFragment());
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
			Toast.makeText(getActivity(), "Please Login", Toast.LENGTH_SHORT).show();
		}
		
		NetRequest nr = new NetRequest(getActivity());
		nr.execute(Path.MAIN_URL);
		nr.setOnSuccessListener(new AppointmentsSuccessListener());
	}
	
	private void getAppointmentsData() {
		MD5TimePass md5 = new MD5TimePass();
		String[] unixMD5 = md5.getMD5();
		
		String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
		
		try {
			reservation_parser = new PatientReservationJSONParser();
			reservation = reservation_parser.getJSONData(Path.RESERVATION_URL +
					PathParameter.uctime + unixMD5[0] + 
					PathParameter.hcode + unixMD5[1] + 
					PathParameter.patient_id + sp.getString(SharedInformation.patient_id, "0"));
			
			if (reservation != null) {
				for (int i = 0; i < reservation.size(); i++) {
	                Date reservationDate = dateFormat.parse(reservation.get(i).reservation_start_date);
	                if (calendar.getTime().compareTo(reservationDate) == -1 || calendar.getTime().compareTo(reservationDate) == 0) {
	                	// upcoming appts
	                	upcomings_reservation.add(reservation.get(i));
	                } else if (calendar.getTime().compareTo(reservationDate) == 1) {
	                	// past appts
	                	pasts_reservation.add(reservation.get(i));
	                }
				}
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class AppointmentsDataParsing extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			getAppointmentsData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			upcoming_appts.invalidateViews();
			past_appts.invalidateViews();
//			if (reservation == null) Toast.makeText(getActivity(), "No Appointments available", Toast.LENGTH_SHORT).show();
			if (upcomings_reservation.size() == 0) upcoming_appts.setVisibility(View.GONE);
			if (pasts_reservation.size() == 0) past_appts.setVisibility(View.GONE);
			if (upcomings_reservation.size() == 0 && pasts_reservation.size() == 0) {
				noappts.setVisibility(View.VISIBLE);
				upcoming_appts_title.setVisibility(View.GONE);
				past_appts_title.setVisibility(View.GONE);
			}
			pd.dismiss();
		}
	}

	private final class AppointmentsSuccessListener implements OnSuccessListener {
		
		@Override
		public void doSuccess(String result) {
			// TODO Auto-generated method stub
			new AppointmentsDataParsing().execute("");
		}

		@Override
		public void doError() {
			// TODO Auto-generated method stub
			pd.dismiss();
			Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
		}
	}

	private final class UpcomingClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> av, View view, int position, long id) {
			// TODO Auto-generated method stub
			String doctor_specialty = "";
			for (int i = 0; i < upcomings_reservation.get(position).specialty.size(); i++) {
				doctor_specialty = doctor_specialty + ", " + upcomings_reservation.get(position).specialty.get(i).specialty_name;
			}
			
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new AppointmentsDetailFragment();
				Bundle bundle = new Bundle();
				
				bundle.putString(BundleInformation.specialty_id, upcomings_reservation.get(position).reservation_specialty_id);
				bundle.putParcelableArrayList(BundleInformation.specialty, upcomings_reservation.get(position).specialty);
				bundle.putString(BundleInformation.date, upcomings_reservation.get(position).reservation_start_date);
				bundle.putString(BundleInformation.doctor_id, upcomings_reservation.get(position).doctor_user_id);
				bundle.putString(BundleInformation.doctor_profile_image, upcomings_reservation.get(position).doctor_profile_image);
				bundle.putString(BundleInformation.doctor_name, upcomings_reservation.get(position).doctor_first_name + " " + upcomings_reservation.get(position).doctor_last_name);
				bundle.putString(BundleInformation.doctor_specialty, doctor_specialty.substring(2, doctor_specialty.length()));
				bundle.putString(BundleInformation.place_id, upcomings_reservation.get(position).reservation_place_id);
				bundle.putString(BundleInformation.place_address_line1, upcomings_reservation.get(position).reservation_address_line1);
				bundle.putString(BundleInformation.place_name, upcomings_reservation.get(position).reservation_name + " " + upcomings_reservation.get(position).reservation_country + " " + upcomings_reservation.get(position).reservation_postal_code);
				bundle.putString(BundleInformation.timeslot_id, upcomings_reservation.get(position).reservation_timeslot_id);
				bundle.putString(BundleInformation.reservation_id, upcomings_reservation.get(position).reservation_id);
				bundle.putString(BundleInformation.reason_for_visit_id, upcomings_reservation.get(position).reservation_specialty_id);
				bundle.putString(BundleInformation.reason_for_visit, upcomings_reservation.get(position).reservation_specialty_name);
				bundle.putString(BundleInformation.appointment_for, upcomings_reservation.get(position).for_);
				bundle.putString(BundleInformation.first_time_visit, upcomings_reservation.get(position).first_time);
				bundle.putString(BundleInformation.patient_name, upcomings_reservation.get(position).peer_name);
				bundle.putString(BundleInformation.patient_tel_no, upcomings_reservation.get(position).peer_tel_no);
				bundle.putString(BundleInformation.patient_email, upcomings_reservation.get(position).peer_email);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class UpcomingListAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder viewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.appointments_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
				viewHolder.doctor_datetime = (TextView) view.findViewById(R.id.doctor_datetime);
				viewHolder.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
				viewHolder.doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
				viewHolder.doctor_reason_visit = (TextView) view.findViewById(R.id.doctor_reason_visit);

				view.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			PatientReservation sub_reservation = getItem(position);
			
			viewHolder.doctor_image.setTag(Path.THUMBNAIL_URL + sub_reservation.doctor_profile_image);
			imageLoader.displayImage(Path.THUMBNAIL_URL + sub_reservation.doctor_profile_image, getActivity(), viewHolder.doctor_image);
			String[] datetime;
			datetime = sub_reservation.reservation_start_date.split("T");
			viewHolder.doctor_datetime.setText(datetime[0] + ", " + datetime[1].substring(0, 5));
			viewHolder.doctor_name.setText("Appt: " + sub_reservation.doctor_first_name + " " + sub_reservation.doctor_last_name);
			
			String doctor_specialty = "";
			for (int i = 0; i < sub_reservation.specialty.size(); i++) {
				doctor_specialty = doctor_specialty + ", " + sub_reservation.specialty.get(i).specialty_name;
			}
			doctor_specialty = doctor_specialty.substring(2, doctor_specialty.length());
			viewHolder.doctor_specialty.setText("Specialty: " + doctor_specialty);
			viewHolder.doctor_reason_visit.setText("Reason of Visit: " + sub_reservation.reservation_specialty_name);

			return view;
		}

		public class ViewHolder {
			public ImageView doctor_image;
			public TextView doctor_datetime;
			public TextView doctor_name;
			public TextView doctor_specialty;
			public TextView doctor_reason_visit;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public PatientReservation getItem(int position) {
			// TODO Auto-generated method stub
			return upcomings_reservation.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (upcomings_reservation != null)
				return upcomings_reservation.size();
			else
				return 0;
		}
	}

	private final class PastListAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder viewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.appointments_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
				viewHolder.doctor_datetime = (TextView) view.findViewById(R.id.doctor_datetime);
				viewHolder.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
				viewHolder.doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
				viewHolder.doctor_reason_visit = (TextView) view.findViewById(R.id.doctor_reason_visit);

				view.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) view.getTag();
			}
			
			PatientReservation sub_reservation = getItem(position);
			
			viewHolder.doctor_image.setTag(Path.THUMBNAIL_URL + sub_reservation.doctor_profile_image);
			imageLoader.displayImage(Path.THUMBNAIL_URL + sub_reservation.doctor_profile_image, getActivity(), viewHolder.doctor_image);
			String[] datetime;
			datetime = sub_reservation.reservation_start_date.split("T");
			viewHolder.doctor_datetime.setText(datetime[0] + ", " + datetime[1].substring(0, 5));
			viewHolder.doctor_name.setText("Appt: " + sub_reservation.doctor_first_name + " " + sub_reservation.doctor_last_name);
			
			String doctor_specialty = "";
			for (int i = 0; i < sub_reservation.specialty.size(); i++) {
				doctor_specialty = doctor_specialty + ", " + sub_reservation.specialty.get(i).specialty_name;
			}
			doctor_specialty = doctor_specialty.substring(2, doctor_specialty.length());
			viewHolder.doctor_specialty.setText("Specialty: " + doctor_specialty);
			viewHolder.doctor_reason_visit.setText("Reason of Visit: " + sub_reservation.reservation_specialty_name);

			return view;
		}

		public class ViewHolder {
			public ImageView doctor_image;
			public TextView doctor_datetime;
			public TextView doctor_name;
			public TextView doctor_specialty;
			public TextView doctor_reason_visit;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public PatientReservation getItem(int position) {
			// TODO Auto-generated method stub
			return pasts_reservation.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (pasts_reservation != null)
				return pasts_reservation.size();
			else
				return 0;
		}
	}

}
