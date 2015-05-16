package com.rezzcomm.reservation.view.search.booking;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.parser.LoginJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Doctors_Specialty;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.model.Patient;
import com.rezzcomm.reservation.util.MD5TimePass;

public class BookingLoginFragment extends Fragment {

	private String specialty_id, timeslot_id;
	private int reason_for_visit_id;
	private int[] date_arr;
	private String time_arr;
	private String doctor_id, doctor_name_str, doctor_profile_image, doctor_specialty_str;
	private String place_id_str, place_address_line1_str, place_name_str;

	private RelativeLayout email_box, password_box;
	private TextView title, signin_lbl, welcome_lbl, notregister_lbl;
	private EditText email, password;
	private Button login, register, logout;
	
	private ArrayList<Doctors_Specialty> reason_for_visit;
	private SharedPreferences sp;
	private Patient patient;
	private LoginJSONParser login_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.book_login, container, false);

		title = (TextView) view.findViewById(R.id.title);
		signin_lbl = (TextView) view.findViewById(R.id.signin_lbl);
		welcome_lbl = (TextView) view.findViewById(R.id.welcome);

		email_box = (RelativeLayout) view.findViewById(R.id.email_box);
		password_box = (RelativeLayout) view.findViewById(R.id.password_box);
		email = (EditText) view.findViewById(R.id.email);
		password = (EditText) view.findViewById(R.id.password);

		login = (Button) view.findViewById(R.id.login_btn);
		notregister_lbl = (TextView) view.findViewById(R.id.notregister);
		register = (Button) view.findViewById(R.id.register_btn);
		logout = (Button) view.findViewById(R.id.logout_btn);

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
			time_arr				= bundle.getString(BundleInformation.time);
			reason_for_visit		= bundle.getParcelableArrayList(BundleInformation.specialty);
			doctor_id 				= bundle.getString(BundleInformation.doctor_id);
			doctor_name_str 		= bundle.getString(BundleInformation.doctor_name);
			doctor_profile_image	= bundle.getString(BundleInformation.doctor_profile_image);
			doctor_specialty_str 	= bundle.getString(BundleInformation.doctor_specialty);
			place_id_str			= bundle.getString(BundleInformation.place_id);
			place_address_line1_str = bundle.getString(BundleInformation.place_address_line1);
			place_name_str 			= bundle.getString(BundleInformation.place_name);
			timeslot_id				= bundle.getString(BundleInformation.timeslot_id);
		}

		title.setText("Book Your Appointment");

		login.setOnClickListener(new LoginClickListener());
		register.setOnClickListener(new RegisterClickListener());
		logout.setOnClickListener(new LogoutClickListener());

		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
		if (sp.getString(SharedInformation.patient_id, null) != null) {
			welcome_lbl.setText("Welcome, " + sp.getString(SharedInformation.last_name, "Guest") + "!");
			welcome_lbl.setVisibility(View.VISIBLE);
			signin_lbl.setText("Account Information");
			email_box.setVisibility(View.GONE);
			password_box.setVisibility(View.GONE);
			notregister_lbl.setText("Not this user?");
			register.setVisibility(View.GONE);
			logout.setVisibility(View.VISIBLE);
			login.setText("Next");
			login.setOnClickListener(new NextClickListener());
		}
	}

	private final class NextClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();
			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new BookingDetailFragment();
				Bundle bundle = new Bundle();

				bundle.putString(BundleInformation.specialty_id, specialty_id);
				bundle.putInt(BundleInformation.reason_for_visit_id, reason_for_visit_id);
				bundle.putIntArray(BundleInformation.date, date_arr);
				bundle.putString(BundleInformation.time, time_arr);
				bundle.putParcelableArrayList(BundleInformation.specialty, reason_for_visit);
				bundle.putString(BundleInformation.doctor_id, doctor_id);
				bundle.putString(BundleInformation.doctor_name, doctor_name_str);
				bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
				bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
				bundle.putString(BundleInformation.place_id, place_id_str);
				bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
				bundle.putString(BundleInformation.place_name, place_name_str);
				bundle.putString(BundleInformation.timeslot_id, timeslot_id);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class LogoutClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			signin_lbl.setText("Sign In");
			welcome_lbl.setVisibility(View.GONE);
			email_box.setVisibility(View.VISIBLE);
			password_box.setVisibility(View.VISIBLE);
			notregister_lbl.setText("Not a registered user?");
			register.setVisibility(View.VISIBLE);
			logout.setVisibility(View.GONE);
			login.setText("Login");
			sp.edit().clear().commit();
		}
	}

	private final class LoginClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Pattern pattern_email = Patterns.EMAIL_ADDRESS;

			if (email.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
			} 
			else if (!pattern_email.matcher(email.getText().toString()).matches()) {
				Toast.makeText(getActivity(), "Enter correct email", Toast.LENGTH_SHORT).show();
			} 
			else if (password.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
			} 
			else {

				try {
					MD5TimePass md5 = new MD5TimePass();
					String[] unixMD5 = md5.getMD5();

					login_parser = new LoginJSONParser();
					patient = login_parser.getJSONData(getActivity(), Path.LOGIN_URL + 
							PathParameter.uctime + unixMD5[0] + 
							PathParameter.hcode + unixMD5[1] + 
							PathParameter.email + email.getText() + 
							PathParameter.password + password.getText());

					if (patient.patient_id != null) {
						FragmentManager fm = getFragmentManager();
						if (fm != null) {
							FragmentTransaction ft = fm.beginTransaction();
							Fragment fragment = new BookingDetailFragment();
							Bundle bundle = new Bundle();

							bundle.putString(BundleInformation.specialty_id, specialty_id);
							bundle.putInt(BundleInformation.reason_for_visit_id, reason_for_visit_id);
							bundle.putIntArray(BundleInformation.date, date_arr);
							bundle.putString(BundleInformation.time, time_arr);
							bundle.putParcelableArrayList(BundleInformation.specialty, reason_for_visit);
							bundle.putString(BundleInformation.doctor_id, doctor_id);
							bundle.putString(BundleInformation.doctor_name, doctor_name_str);
							bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
							bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
							bundle.putString(BundleInformation.place_id, place_id_str);
							bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
							bundle.putString(BundleInformation.place_name, place_name_str);
							bundle.putString(BundleInformation.timeslot_id, timeslot_id);

							fragment.setArguments(bundle);
							ft.replace(R.id.fragment_content, fragment);
							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
							ft.addToBackStack(null);
							ft.commit();
						}
					} else {
						Toast.makeText(getActivity(), "Wrong username or password", Toast.LENGTH_SHORT).show();
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

	private final class RegisterClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new BookingRegistrationFragment();
				Bundle bundle = new Bundle();
				
				bundle.putString(BundleInformation.specialty_id, specialty_id);
				bundle.putInt(BundleInformation.reason_for_visit_id, reason_for_visit_id);
				bundle.putIntArray(BundleInformation.date, date_arr);
				bundle.putString(BundleInformation.time, time_arr);
				bundle.putParcelableArrayList(BundleInformation.specialty, reason_for_visit);
				bundle.putString(BundleInformation.doctor_id, doctor_id);
				bundle.putString(BundleInformation.doctor_name, doctor_name_str);
				bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
				bundle.putString(BundleInformation.place_id, place_id_str);
				bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
				bundle.putString(BundleInformation.place_name, place_name_str);
				bundle.putString(BundleInformation.timeslot_id, timeslot_id);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}
}
