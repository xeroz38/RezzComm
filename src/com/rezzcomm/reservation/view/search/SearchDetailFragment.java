package com.rezzcomm.reservation.view.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.DownloadImageBitmap;
import com.rezzcomm.reservation.controller.parser.DoctorDetailsJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.FavoriteColumns;
import com.rezzcomm.reservation.model.Doctors;
import com.rezzcomm.reservation.model.Doctors_Specialty;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.model.Timeslot_list;
import com.rezzcomm.reservation.util.MD5TimePass;
import com.rezzcomm.reservation.view.search.booking.BookingLoginFragment;

public class SearchDetailFragment extends Fragment {

	private boolean isProfState = false;
	private boolean isSpecialty = false;
	private boolean isEducation = false;
	private boolean isAwards = false;
	private boolean isLanguage = false;
	private boolean isFavourite = false;

	private String specialty_id;
	private String specialty_name;
	private int[] date_arr;
	private String doctor_id, doctor_name_str, doctor_profile_image;
	private String doctor_professional_statement, doctor_main_specialty_str, doctor_specialty_str, doctor_education_str, doctor_awards;
	private String place_id_str, place_address_line1_str, place_name_str;
	private int maxDate;

	private TextView title, doctor_name, doctor_specialty, place_address_line1, place_name;
	private TextView prof_state_text, specialty_sub_text, education_text, awards_text, language_spoken_text;
	
	private ImageView prev, next;
	private Button btn1, btn2, btn3, btn4, btn5;
	private Button book_btn, add_to_fav_btn, share_this_btn;
	private Button prof_state, specialty_sub, education, awards, language_spoken;

	private ProgressBar progress;
	private ImageView doctor_image;
	private LinearLayout doctor_schedule, doctor_schedule_line2, doctor_schedule_line3;
	private Spinner reasonforvisit;
	private Calendar calendar;

	private Doctors doctor;
	private ArrayList<Doctors_Specialty> reason_for_visit;
	private ArrayList<Timeslot_list> timeslot;
	private ArrayAdapter<String> dataAdapter;
	private ContentValues values;
	private DownloadImageBitmap downloadImage;
	private DoctorDetailsJSONParser doctor_detail_parser;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.search_detail, container, false);

		title = (TextView) view.findViewById(R.id.title);

		reasonforvisit = (Spinner) view.findViewById(R.id.reasonforvisit);
		
		btn1 = (Button) view.findViewById(R.id.btn1);
		btn2 = (Button) view.findViewById(R.id.btn2);
		btn3 = (Button) view.findViewById(R.id.btn3);
		btn4 = (Button) view.findViewById(R.id.btn4);
		btn5 = (Button) view.findViewById(R.id.btn5);
		
		prev = (ImageView) view.findViewById(R.id.prev);
		next = (ImageView) view.findViewById(R.id.next);

		progress = (ProgressBar) view.findViewById(R.id.progress);
		doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
		doctor_name = (TextView) view.findViewById(R.id.doctor_name);
		doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
		place_address_line1 = (TextView) view.findViewById(R.id.place_address_line1);
		place_name = (TextView) view.findViewById(R.id.place_name);

		doctor_schedule = (LinearLayout) view.findViewById(R.id.doctor_schedule);
		doctor_schedule_line2 = (LinearLayout) view.findViewById(R.id.doctor_schedule_line2);
		doctor_schedule_line3 = (LinearLayout) view.findViewById(R.id.doctor_schedule_line3);

		prof_state = (Button) view.findViewById(R.id.prof_state);
		prof_state_text = (TextView) view.findViewById(R.id.prof_state_text);
		specialty_sub = (Button) view.findViewById(R.id.specialty_sub_specialty);
		specialty_sub_text = (TextView) view.findViewById(R.id.specialty_sub_specialty_text);
		education = (Button) view.findViewById(R.id.education);
		education_text = (TextView) view.findViewById(R.id.education_text);
		awards = (Button) view.findViewById(R.id.awards);
		awards_text = (TextView) view.findViewById(R.id.awards_text);
		language_spoken = (Button) view.findViewById(R.id.language);
		language_spoken_text = (TextView) view.findViewById(R.id.language_text);

		book_btn = (Button) view.findViewById(R.id.book_btn);
		add_to_fav_btn = (Button) view.findViewById(R.id.add_to_favourite_btn);
		share_this_btn = (Button) view.findViewById(R.id.share_this_btn);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null){
			specialty_id 					= bundle.getString(BundleInformation.specialty_id);
			specialty_name					= bundle.getString(BundleInformation.specialty_name);
			date_arr 						= bundle.getIntArray(BundleInformation.date);
			reason_for_visit				= bundle.getParcelableArrayList(BundleInformation.specialty);
			timeslot 						= bundle.getParcelableArrayList(BundleInformation.timeslot);
			doctor_id 						= bundle.getString(BundleInformation.doctor_id);
			doctor_profile_image			= bundle.getString(BundleInformation.doctor_profile_image);
			doctor_professional_statement 	= bundle.getString(BundleInformation.doctor_professional_statement);
			doctor_name_str 				= bundle.getString(BundleInformation.doctor_name);
			doctor_main_specialty_str 		= bundle.getString(BundleInformation.doctor_main_specialty);
			doctor_specialty_str 			= bundle.getString(BundleInformation.doctor_specialty);
			place_id_str					= bundle.getString(BundleInformation.place_id);
			place_address_line1_str			= bundle.getString(BundleInformation.place_address_line1);
			place_name_str 					= bundle.getString(BundleInformation.place_name);
			doctor_education_str 			= bundle.getString(BundleInformation.doctor_education);
			doctor_awards					= bundle.getString(BundleInformation.doctor_awards);
		}

		title.setText(specialty_name);

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

		doctor_image.setImageResource(R.drawable.default_thumb);
		doctor_name.setText(doctor_name_str);
		doctor_specialty.setText(doctor_main_specialty_str);
		place_address_line1.setText(place_address_line1_str);
		place_name.setText(place_name_str);

		if (doctor_professional_statement.equals("null")) prof_state_text.setText("Information not available");
		else prof_state_text.setText(doctor_professional_statement);
		specialty_sub_text.setText("Specialty:\n" + specialty_name + "\n\n" + "Sub-Specialty:\n" + doctor_specialty_str);
		education_text.setText(doctor_education_str);
		awards_text.setText("Information not available");
		language_spoken_text.setText(doctor_awards);
		
		prev.setOnClickListener(new PreviousClickListener());
		next.setOnClickListener(new NextClickListener());

		prof_state.setOnClickListener(new ExpandButtonClickListener());
		specialty_sub.setOnClickListener(new ExpandButtonClickListener());
		education.setOnClickListener(new ExpandButtonClickListener());
		awards.setOnClickListener(new ExpandButtonClickListener());
		language_spoken.setOnClickListener(new ExpandButtonClickListener());

		book_btn.setOnClickListener(new BookClickListener());
		add_to_fav_btn.setOnClickListener(new FavouriteClickListener());
		share_this_btn.setOnClickListener(new ShareClickListener());
		
		Cursor cursor = getActivity().getContentResolver().query(FavoriteColumns.CONTENT_URI, FavoriteColumns.QUERY_SHORT,  FavoriteColumns.DOCTOR_ID + "=?", new String[]{doctor_id}, null);

		if (cursor.getCount() > 0) {
			add_to_fav_btn.setText("Remove Favourite");
			isFavourite = true;
		} else {
			add_to_fav_btn.setText("Add to Favourite");
			isFavourite = false;
		}
		
		calendar = Calendar.getInstance();
		calendar.set(date_arr[2], date_arr[1]+1, date_arr[0]);
		maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		calculateCalendar(date_arr[0], date_arr[1], date_arr[2]);
		
		new DoctorsDataParsing().execute("");
		new DownloadImage().execute("");
	}
	
	private void calculateTimeslot() {
		// TODO Auto-generated method stub
		int widthSoFar = 0;
		doctor_schedule.removeAllViews();
		doctor_schedule_line2.removeAllViews();
		doctor_schedule_line3.removeAllViews();

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
			doctor_schedule_line3.measure(0, 0);
			widthSoFar += doctor_time.getMeasuredWidth() + 14 + 24; // 14 for extra margin, 24 for extra padding (both left right)
			int maxWidth = detectDisplayWidth();

			if (widthSoFar >= maxWidth) {
				doctor_schedule_line2.setVisibility(View.VISIBLE);

				if (widthSoFar >= (maxWidth*2)) {
					doctor_schedule_line3.setVisibility(View.VISIBLE);
					doctor_schedule_line3.addView(doctor_time);
				} else {
					doctor_schedule_line2.addView(doctor_time);
					widthSoFar = doctor_schedule_line2.getMeasuredWidth() + doctor_schedule.getMeasuredWidth();
				}
			} else {
				doctor_schedule.addView(doctor_time);
				widthSoFar = doctor_schedule.getMeasuredWidth();
			}
		}
	}

	private void getDoctorData() {
		MD5TimePass md5 = new MD5TimePass();
		String[] unixMD5 = md5.getMD5();
		
		try {
			doctor_detail_parser = new DoctorDetailsJSONParser();
			doctor = doctor_detail_parser.getJSONData(Path.DOCTOR_DETAIL_URL + 
					PathParameter.uctime + unixMD5[0] + 
					PathParameter.hcode + unixMD5[1] + 
					PathParameter.doctor_id + doctor_id +
					PathParameter.place_id + place_id_str +
					PathParameter.start_date + date_arr[2] + "/" + (date_arr[1]+1) + "/" + date_arr[0] +
					PathParameter.end_date + date_arr[2] + "/" + (date_arr[1]+1) + "/" + date_arr[0]);
			
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
	
	private void calculateCalendar(int day, int month, int year) {

		month++;
		if (day-2 >= 1) {
			btn1.setText((day-2) + "/" + month);
		} else {
			btn1.setText((day-2+maxDate) + "/" + (month-1));
		}

		if (day-1 >= 1) {
			btn2.setText((day-1) + "/" + month);
		} else {
			btn2.setText((day-1+maxDate) + "/" + (month-1));
		}

		btn3.setText(day + "/" + month);

		if (day+1 <= maxDate) {
			btn4.setText((day+1) + "/" + month);
		} else {
			btn4.setText((day+1-maxDate) + "/" + (month+1));
		}

		if (day+2 <= maxDate) {
			btn5.setText((day+2) + "/" + month);
		} else {
			btn5.setText((day+2-maxDate) + "/" + (month+1));
		}
	}
	
	private final class PreviousClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (date_arr[0] > 1) {
				date_arr[0]--;
			} else {
				date_arr[0]--;
				date_arr[0] = date_arr[0] + maxDate; 
				date_arr[1]--;
			}
			calculateCalendar(date_arr[0], date_arr[1], date_arr[2]);
			new DoctorsDataParsing().execute("");
		}
	}
	
	private final class NextClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (date_arr[0] < maxDate) {
				date_arr[0]++;
			} else {
				date_arr[0]++;
				date_arr[0] = date_arr[0] - maxDate; 
				date_arr[1]++;
			}
			calculateCalendar(date_arr[0], date_arr[1], date_arr[2]);
			new DoctorsDataParsing().execute("");
		}
	}
	
	private class DoctorsDataParsing extends AsyncTask<String, String, String> {

		private ProgressDialog pd = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pd.setIcon(R.drawable.default_thumb);
			pd.setTitle("Please Wait");
			pd.setMessage("Loading...");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			getDoctorData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			timeslot = doctor.timeslot.get(0).timeslot_list;
			calculateTimeslot();
			pd.dismiss();
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

	private final class FavouriteClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isFavourite) {
				add_to_fav_btn.setText("Add to Favourite");
				isFavourite = false;
				
				getActivity().getContentResolver().delete(FavoriteColumns.CONTENT_URI, FavoriteColumns.DOCTOR_ID + "=?", new String[]{doctor_id});
				
				Builder ad = new AlertDialog.Builder(getActivity());
				ad.setTitle("Favourite");
				ad.setMessage("Removed from favourite doctor");
				ad.setNegativeButton("OK", null);
				ad.show();
			} else {
				add_to_fav_btn.setText("Remove Favourite");
				isFavourite = true;

				values = new ContentValues();
				values.put(FavoriteColumns.SPECIALTY_ID, specialty_id);
				values.put(FavoriteColumns.SPECIALTY_NAME, specialty_name);
				values.put(FavoriteColumns.REASON_FOR_VISIT_ID, reasonforvisit.getSelectedItemPosition());
				values.put(FavoriteColumns.DATE, date_arr[0] + date_arr[1] + date_arr[2]);
				values.put(FavoriteColumns.SPECIALTY, 0);
				values.put(FavoriteColumns.DOCTOR_ID, doctor_id);
				values.put(FavoriteColumns.DOCTOR_NAME, doctor_name_str);
				values.put(FavoriteColumns.DOCTOR_PROFILE_IMAGE, doctor_profile_image);
				values.put(FavoriteColumns.DOCTOR_PROFFESIONAL_STATEMENT, doctor_professional_statement);
				values.put(FavoriteColumns.DOCTOR_SPECIALTY, doctor_specialty_str);
				values.put(FavoriteColumns.PLACE_ID, place_id_str);
				values.put(FavoriteColumns.PLACE_ADDRESS, place_address_line1_str);
				values.put(FavoriteColumns.PLACE_NAME, place_name_str);
				values.put(FavoriteColumns.TIMESLOT_ID, 0);
				values.put(FavoriteColumns.DOCTOR_EDUCATION, doctor_education_str);
				values.put(FavoriteColumns.DOCTOR_AWARDS, doctor_awards);

				getActivity().getContentResolver().insert(FavoriteColumns.CONTENT_URI, values);

				Builder ad = new AlertDialog.Builder(getActivity());
				ad.setTitle("Favourite");
				ad.setMessage("Added to favourite doctor");
				ad.setNegativeButton("OK", null);
				ad.show();
			}
		}
	}

	private final class ExpandButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Drawable group_collapse = getActivity().getResources().getDrawable(R.drawable.group_indicator_collapse);
			Drawable group_expand = getActivity().getResources().getDrawable(R.drawable.group_indicator_expand);

			switch (v.getId()) 
			{
			case R.id.prof_state: 
			{
				if (isProfState) {
					prof_state_text.setVisibility(View.VISIBLE);
					prof_state.setCompoundDrawablesWithIntrinsicBounds(null, null, group_collapse, null);
					isProfState = false;
				} else {
					prof_state_text.setVisibility(View.GONE);
					prof_state.setCompoundDrawablesWithIntrinsicBounds(null, null, group_expand, null);
					isProfState = true;
				}
				break;
			}
			case R.id.specialty_sub_specialty: 
			{
				if (isSpecialty) {
					specialty_sub_text.setVisibility(View.VISIBLE);
					specialty_sub.setCompoundDrawablesWithIntrinsicBounds(null, null, group_collapse, null);
					isSpecialty = false;
				} else {
					specialty_sub_text.setVisibility(View.GONE);
					specialty_sub.setCompoundDrawablesWithIntrinsicBounds(null, null, group_expand, null);
					isSpecialty = true;
				}
				break;
			}
			case R.id.education: 
			{
				if (isEducation) {
					education_text.setVisibility(View.VISIBLE);
					education.setCompoundDrawablesWithIntrinsicBounds(null, null, group_collapse, null);
					isEducation = false;
				} else {
					education_text.setVisibility(View.GONE);
					education.setCompoundDrawablesWithIntrinsicBounds(null, null, group_expand, null);
					isEducation = true;
				}
				break;
			}
			case R.id.awards: 
			{
				if (isAwards) {
					awards_text.setVisibility(View.VISIBLE);
					awards.setCompoundDrawablesWithIntrinsicBounds(null, null, group_collapse, null);
					isAwards = false;
				} else {
					awards_text.setVisibility(View.GONE);
					awards.setCompoundDrawablesWithIntrinsicBounds(null, null, group_expand, null);
					isAwards = true;
				}
				break;
			}
			case R.id.language: 
			{
				if (isLanguage) {
					language_spoken_text.setVisibility(View.VISIBLE);
					language_spoken.setCompoundDrawablesWithIntrinsicBounds(null, null, group_collapse, null);
					isLanguage = false;
				} else {
					language_spoken_text.setVisibility(View.GONE);
					language_spoken.setCompoundDrawablesWithIntrinsicBounds(null, null, group_expand, null);
					isLanguage = true;
				}
				break;
			}
			default:break;
			}
		}
	}

	private final class TimeslotClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getFragmentManager();

			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = new BookingLoginFragment();
				Bundle bundle = new Bundle();

				bundle.putString(BundleInformation.specialty_id, specialty_id);
				bundle.putInt(BundleInformation.reason_for_visit_id, reasonforvisit.getSelectedItemPosition());
				bundle.putIntArray(BundleInformation.date, date_arr);
				bundle.putString(BundleInformation.time, timeslot.get(v.getId()).start_date.substring(11, 16));
				bundle.putParcelableArrayList(BundleInformation.specialty, reason_for_visit);
				bundle.putString(BundleInformation.doctor_id, doctor_id);
				bundle.putString(BundleInformation.doctor_name, doctor_name_str);
				bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
				bundle.putString(BundleInformation.doctor_specialty, doctor_main_specialty_str);
				bundle.putString(BundleInformation.place_id, place_id_str);
				bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
				bundle.putString(BundleInformation.place_name, place_name_str);
				bundle.putString(BundleInformation.timeslot_id, timeslot.get(v.getId()).timeslot_id);

				fragment.setArguments(bundle);
				ft.replace(R.id.fragment_content, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
	}

	private final class BookClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (timeslot.size() > 0) {
				FragmentManager fm = getFragmentManager();

				if (fm != null) {
					FragmentTransaction ft = fm.beginTransaction();
					Fragment fragment = new BookingLoginFragment();
					Bundle bundle = new Bundle();

					bundle.putString(BundleInformation.specialty_id, specialty_id);
					bundle.putInt(BundleInformation.reason_for_visit_id, reasonforvisit.getSelectedItemPosition());
					bundle.putIntArray(BundleInformation.date, date_arr);
					bundle.putString(BundleInformation.time, timeslot.get(0).start_date.substring(11, 16));
					bundle.putParcelableArrayList(BundleInformation.specialty, reason_for_visit);
					bundle.putString(BundleInformation.doctor_id, doctor_id);
					bundle.putString(BundleInformation.doctor_name, doctor_name_str);
					bundle.putString(BundleInformation.doctor_profile_image, doctor_profile_image);
					bundle.putString(BundleInformation.doctor_specialty, doctor_specialty_str);
					bundle.putString(BundleInformation.place_id, place_id_str);
					bundle.putString(BundleInformation.place_address_line1, place_address_line1_str);
					bundle.putString(BundleInformation.place_name, place_name_str);
					bundle.putString(BundleInformation.timeslot_id, timeslot.get(0).timeslot_id);

					fragment.setArguments(bundle);
					ft.replace(R.id.fragment_content, fragment);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.addToBackStack(null);
					ft.commit();
				}
			} else {
				Toast.makeText(getActivity(), "There is no available timeslot", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
