package com.rezzcomm.reservation.view.appointments;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.parser.RegistrationJSONParser;
import com.rezzcomm.reservation.controller.parser.SendActivationJSONParser;
import com.rezzcomm.reservation.model.BundleInformation.UserInformation;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.MD5TimePass;

public class AppointmentsRegistrationFragment extends Fragment {

	private String country_selected, gender_selected;
	private String message;

	private TextView title, agree_tou_text, agree_tou_link;
	private EditText email, fname, lname, password, repass, mobile;
	private Button register;
	private Spinner country, gender;
	private CheckBox agree_tou;

	private  EditText activation_code;
	
	private ArrayAdapter<String> dataAdapter;
	private RegistrationJSONParser registration_parser;
	private SendActivationJSONParser send_activation_parser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.appointments_registration, container, false);

		title = (TextView) view.findViewById(R.id.title);

		email = (EditText) view.findViewById(R.id.email);
		fname = (EditText) view.findViewById(R.id.fname);
		lname = (EditText) view.findViewById(R.id.lname);
		password = (EditText) view.findViewById(R.id.password);
		repass = (EditText) view.findViewById(R.id.re_password);
		mobile = (EditText) view.findViewById(R.id.mobile);

		register = (Button) view.findViewById(R.id.register_btn);
		country = (Spinner) view.findViewById(R.id.country);
		gender = (Spinner) view.findViewById(R.id.gender);

		agree_tou = (CheckBox) view.findViewById(R.id.checkbox_agree_tou);
		agree_tou_text = (TextView) view.findViewById(R.id.checkbox_agree_tou_text);
		agree_tou_link = (TextView) view.findViewById(R.id.checkbox_agree_tou_link);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		title.setText("My Appointment");

		dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserInformation.Country) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(getActivity());
					convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
				}
				TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
				tv.setText(UserInformation.Country[position]);
				tv.setTextSize(12);

				return convertView;
			}
		};
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		country.setAdapter(dataAdapter);
		country.setOnItemSelectedListener(new CountryItemSelectedListener());

		dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserInformation.Gender) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(getActivity());
					convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
				}
				TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
				tv.setText(UserInformation.Gender[position]);
				tv.setTextSize(12);

				return convertView;
			}
		};
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(dataAdapter);
		gender.setOnItemSelectedListener(new GenderItemSelectedListener());

		agree_tou_text.setText("I have read and accept RezzComm's ");
		agree_tou_link.setText("Terms of Use");
		agree_tou_link.setOnClickListener(new TermsOfUseClickListener());

		register.setOnClickListener(new RegisterClickListener());
	}

	private final class TermsOfUseClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse(Path.TERM_OF_USE_URL);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	private final class RegisterClickListener implements OnClickListener {
		
		private final class AccountActivationClickListener implements DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MD5TimePass md5 = new MD5TimePass();
				String[] unixMD5 = md5.getMD5();
				
				try {
					send_activation_parser = new SendActivationJSONParser();
					message = send_activation_parser.getJSONData(Path.SMS_ACTIVATION_URL + 
							PathParameter.uctime + unixMD5[0] + 
							PathParameter.hcode + unixMD5[1] + 
							PathParameter.email + email.getText() +
							PathParameter.activation_code + activation_code.getText());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private final class ResendActivationCodeClickListener implements DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MD5TimePass md5 = new MD5TimePass();
				String[] unixMD5 = md5.getMD5();
				
				try {
					send_activation_parser = new SendActivationJSONParser();
					message = send_activation_parser.getJSONData(Path.SMS_ACTIVATION_URL + 
							PathParameter.uctime + unixMD5[0] + 
							PathParameter.hcode + unixMD5[1] + 
							PathParameter.email + email.getText());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Pattern pattern_email = Patterns.EMAIL_ADDRESS;
			Pattern pattern_mobile = Patterns.PHONE;

			if (email.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
			} 
			else if (!pattern_email.matcher(email.getText().toString()).matches()) {
				Toast.makeText(getActivity(), "Enter correct email", Toast.LENGTH_SHORT).show();
			} 
			else if (fname.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter first name", Toast.LENGTH_SHORT).show();
			} 
			else if (lname.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter last name", Toast.LENGTH_SHORT).show();
			} 
			else if (password.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
			} 
			else if (repass.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Re-Enter password", Toast.LENGTH_SHORT).show();
			} 
			else if (!password.getText().toString().equals(repass.getText().toString())) {
				Toast.makeText(getActivity(), "Password must be same", Toast.LENGTH_SHORT).show();
			} 
			else if (mobile.getText().toString().trim().equals("")) {
				Toast.makeText(getActivity(), "Enter mobile phone", Toast.LENGTH_SHORT).show();
			} 
			else if (!pattern_mobile.matcher(mobile.getText().toString()).matches()) {
				Toast.makeText(getActivity(), "Enter correct mobile phone", Toast.LENGTH_SHORT).show();
			} 
			else if (agree_tou.isChecked()) {
				try {
					MD5TimePass md5 = new MD5TimePass();
					String[] unixMD5 = md5.getMD5();

					registration_parser = new RegistrationJSONParser();
					message = registration_parser.getJSONData(Path.REGISTRATION_URL + 
							PathParameter.uctime + unixMD5[0] + 
							PathParameter.hcode + unixMD5[1] + 
							PathParameter.first_name + fname.getText() + 
							PathParameter.last_name + lname.getText() + 
							PathParameter.email + email.getText() + 
							PathParameter.password + password.getText() + 
							PathParameter.tel_no + mobile.getText() + 
							PathParameter.country + country_selected + 
							PathParameter.gender + gender_selected);

					if (message.equals("Success")) {
						send_activation_parser = new SendActivationJSONParser();
						message = send_activation_parser.getJSONData(Path.SMS_ACTIVATION_URL + 
								PathParameter.uctime + unixMD5[0] + 
								PathParameter.hcode + unixMD5[1] + 
								PathParameter.email + email.getText());

						if (message.equals("Success")) {

							final LinearLayout view = new LinearLayout(getActivity());
							final LinearLayout message_view = new LinearLayout(getActivity());
							final TextView message_note = new TextView(getActivity());
							final TextView activation_code_text = new TextView(getActivity());
							activation_code = new EditText(getActivity());
							
							view.setOrientation(LinearLayout.VERTICAL);
							message_note.setText("You have already submitted your registration previously. Please key in the activation code to " +
									"activate your account now. Click the “RESEND” button if you did not receive any SMS activation " +
									"code. Thank you.");
							message_note.setPadding(15, 5, 5, 5);
							message_view.setPadding(15, 5, 5, 5);
							activation_code_text.setText("Activation Code: ");
							activation_code.setWidth(250);
							
							view.addView(message_note);
							message_view.addView(activation_code_text);
							message_view.addView(activation_code);
							view.addView(message_view);
							
							Builder ad = new AlertDialog.Builder(getActivity());
							ad.setTitle("Activate Account");
							ad.setView(view);
							ad.setPositiveButton("Submit", new AccountActivationClickListener());
							ad.setNegativeButton("Resend Activation Code Button", new ResendActivationCodeClickListener());
							ad.show();

							FragmentManager fm = getFragmentManager();

							if (fm != null) {
								FragmentTransaction ft = fm.beginTransaction();
								ft.replace(R.id.fragment_content, new AppointmentsLoginFragment());
								ft.commit();
							}

						} else {
							Toast.makeText(getActivity(), "Send Activation Failed", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(getActivity(), "Please read and accept the user agreement.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private final class CountryItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			country_selected = parent.getItemAtPosition(position).toString();
			switch (position) {
			case 0 : {
				country_selected = "SG";
				break;
			}
			case 1 : {
				country_selected = "MY";
				break;
			}
			case 2 : {
				country_selected = "ID";
				break;
			}
			case 3 : {
				country_selected = "NA";
				break;
			}
			default : break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	private final class GenderItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			gender_selected = parent.getItemAtPosition(position).toString();
			switch (position) {
			case 0 : {
				gender_selected = "1";
				break;
			}
			case 1 : {
				gender_selected = "2";
				break;
			}
			default : break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}
}
