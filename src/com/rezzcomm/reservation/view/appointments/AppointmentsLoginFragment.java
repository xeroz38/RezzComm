package com.rezzcomm.reservation.view.appointments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.json.JSONException;

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
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.parser.LoginJSONParser;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.model.Patient;
import com.rezzcomm.reservation.util.MD5TimePass;

public class AppointmentsLoginFragment extends Fragment {

	private TextView title;
	private EditText email, password;
	private Button login, register;
	
	private Patient patient;
	private LoginJSONParser login_parser;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.appointments_login, container, false);

		title = (TextView) view.findViewById(R.id.title);
		email = (EditText) view.findViewById(R.id.email);
		password = (EditText) view.findViewById(R.id.password);
		login = (Button) view.findViewById(R.id.login_btn);
		register = (Button) view.findViewById(R.id.register_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		title.setText("My Appointment");

		login.setOnClickListener(new LoginClickListener());
		register.setOnClickListener(new RegisterClickListener());
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
							ft.replace(R.id.fragment_content, new AppointmentsFragment());
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
				ft.replace(R.id.fragment_content, new AppointmentsRegistrationFragment());
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}
}
