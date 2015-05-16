package com.rezzcomm.reservation.view.setting;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Patient;
import com.rezzcomm.reservation.model.BundleInformation.SharedInformation;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.MD5TimePass;

public class SettingFragment extends Fragment {

	private RelativeLayout email_box, password_box;
	private TextView welcome;
	private Button login, logout, about, term_of_use;
	private EditText email, password, version, last_updated;
	
	private SharedPreferences sp;
	private Patient patient;
	private LoginJSONParser login_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.setting_fragment, container, false);

		email_box = (RelativeLayout) view.findViewById(R.id.email_box);
		password_box = (RelativeLayout) view.findViewById(R.id.password_box);
		email = (EditText) view.findViewById(R.id.email);
		password = (EditText) view.findViewById(R.id.password);
		
		welcome = (TextView) view.findViewById(R.id.welcome);
		login = (Button) view.findViewById(R.id.login_btn);
		logout = (Button) view.findViewById(R.id.logout_btn);

		version = (EditText) view.findViewById(R.id.version);
		last_updated = (EditText) view.findViewById(R.id.last_update);
		
		about = (Button) view.findViewById(R.id.about_btn);
		term_of_use = (Button) view.findViewById(R.id.tou_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		version.setText("1.0");
		last_updated.setText("12 June 2012");
		
		sp = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
		if (sp.getString(SharedInformation.patient_id, null) != null) {
			welcome.setText("Welcome, " + sp.getString(SharedInformation.last_name, "Guest") + "!");
			welcome.setVisibility(View.VISIBLE);
			email_box.setVisibility(View.GONE);
			password_box.setVisibility(View.GONE);
			login.setVisibility(View.GONE);
			logout.setVisibility(View.VISIBLE);
		}
		
		login.setOnClickListener(new LoginClickListener());
		logout.setOnClickListener(new LogoutClickListener());
		about.setOnClickListener(new AboutClickListener());
		term_of_use.setOnClickListener(new TermOfUseClickListener());
	}

	private final class TermOfUseClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse(Path.TERM_OF_USE_URL);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	private final class AboutClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Builder ad = new AlertDialog.Builder(getActivity());
			ad.setTitle("About RezzComm");
			ad.setMessage("RezzComm is a free online reservation service that helps patients find doctors and book appointments.\n" +
					"Our vision is to provide innovative online reservation service to make it universally accessible for users worldwide.");
			ad.setPositiveButton("OK", null);
			ad.show();
		}
	}

	private final class LogoutClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			welcome.setVisibility(View.GONE);
			email_box.setVisibility(View.VISIBLE);
			password_box.setVisibility(View.VISIBLE);
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.GONE);
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
						welcome.setText("Welcome, " + patient.patient_lastname + "!");
						welcome.setVisibility(View.VISIBLE);
						email_box.setVisibility(View.GONE);
						password_box.setVisibility(View.GONE);
						login.setVisibility(View.GONE);
						logout.setVisibility(View.VISIBLE);
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
}
