package com.rezzcomm.reservation.view.favourite;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.controller.ImageLoader;
import com.rezzcomm.reservation.controller.NetRequest;
import com.rezzcomm.reservation.controller.NetRequest.OnSuccessListener;
import com.rezzcomm.reservation.controller.parser.DoctorDetailsJSONParser;
import com.rezzcomm.reservation.model.BundleInformation;
import com.rezzcomm.reservation.model.BundleInformation.FavoriteColumns;
import com.rezzcomm.reservation.model.Doctors;
import com.rezzcomm.reservation.model.Favorite;
import com.rezzcomm.reservation.model.Path;
import com.rezzcomm.reservation.model.Path.PathParameter;
import com.rezzcomm.reservation.util.IOUtils;
import com.rezzcomm.reservation.util.MD5TimePass;
import com.rezzcomm.reservation.view.search.SearchDetailFragment;

public class FavouriteFragment extends Fragment {

	private TextView title;
	private ListView list_favourite;

	private Doctors doctor;
	private ArrayList<Favorite> favourite;
	private LayoutInflater inflater;

	private DoctorListAdapter dla;
	private DoctorDetailsJSONParser doctor_detail_parser;
	private ImageLoader imageLoader;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.favourite_fragment, container, false);
		
		title = (TextView) view.findViewById(R.id.title);
		list_favourite = (ListView) view.findViewById(R.id.list_favourite);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		title.setText("My Favourite List");

		inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(getActivity());
		
		NetRequest nr = new NetRequest(getActivity());
		nr.execute(Path.MAIN_URL);
		nr.setOnSuccessListener(new SearchSuccessListener());

		dla = new DoctorListAdapter();
		
		favourite = getFavoriteList();
		list_favourite.setAdapter(dla);
		list_favourite.setOnItemLongClickListener(new DeleteFavouriteLongClickListener());
	}
	
	private final class SearchSuccessListener implements OnSuccessListener {
		@Override
		public void doSuccess(String result) {
			// TODO Auto-generated method stub
			list_favourite.setOnItemClickListener(new DoctorListClickListener());
		}

		@Override
		public void doError() {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
		}
	}
	
	private ArrayList<Favorite> getFavoriteList() {
		Cursor cursor = null;
		try {
			cursor = getActivity().getContentResolver().query(FavoriteColumns.CONTENT_URI, FavoriteColumns.QUERY_SHORT, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				int columnSpecialtyID = cursor.getColumnIndexOrThrow(FavoriteColumns.SPECIALTY_ID);
				int columnSpecialtyName = cursor.getColumnIndexOrThrow(FavoriteColumns.SPECIALTY_NAME);
				int columnReasonForVisitID = cursor.getColumnIndexOrThrow(FavoriteColumns.REASON_FOR_VISIT_ID);
				int columnDate = cursor.getColumnIndexOrThrow(FavoriteColumns.DATE);
				int columnSpecialty = cursor.getColumnIndexOrThrow(FavoriteColumns.SPECIALTY);
				int columnDoctorID = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_ID);
				int columnDoctorName = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_NAME);
				int columnDoctorProfileImage = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_PROFILE_IMAGE);
				int columnDoctorProffesionalStatement = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_PROFFESIONAL_STATEMENT);
				int columnDoctorSpecialty = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_SPECIALTY);
				int columnPlaceID = cursor.getColumnIndexOrThrow(FavoriteColumns.PLACE_ID);
				int columnPlaceAddress = cursor.getColumnIndexOrThrow(FavoriteColumns.PLACE_ADDRESS);
				int columnPlaceName = cursor.getColumnIndexOrThrow(FavoriteColumns.PLACE_NAME);
				int columnTimeslotID = cursor.getColumnIndexOrThrow(FavoriteColumns.TIMESLOT_ID);
				int columnDoctorEducation = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_EDUCATION);
				int columnDoctorAwards = cursor.getColumnIndexOrThrow(FavoriteColumns.DOCTOR_AWARDS);

				ArrayList<Favorite> list = new ArrayList<Favorite>(cursor.getCount());
				while (cursor.moveToNext()) {
					Favorite fav = new Favorite();
					fav.specialty_id = cursor.getString(columnSpecialtyID);
					fav.specialty_name = cursor.getString(columnSpecialtyName);
					fav.reason_for_visit_id = cursor.getString(columnReasonForVisitID);
					fav.date = cursor.getString(columnDate);
					fav.specialty = cursor.getString(columnSpecialty);
					fav.doctor_id = cursor.getString(columnDoctorID);
					fav.doctor_name = cursor.getString(columnDoctorName);
					fav.doctor_profile_image = cursor.getString(columnDoctorProfileImage);
					fav.doctor_professional_statement = cursor.getString(columnDoctorProffesionalStatement);
					fav.doctor_specialty = cursor.getString(columnDoctorSpecialty);
					fav.place_id = cursor.getString(columnPlaceID);
					fav.place_address = cursor.getString(columnPlaceAddress);
					fav.place_name = cursor.getString(columnPlaceName);
					fav.timeslot_id = cursor.getString(columnTimeslotID);
					fav.doctor_education = cursor.getString(columnDoctorEducation);
					fav.doctor_awards = cursor.getString(columnDoctorAwards);

					list.add(fav);
				}
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.close(cursor);
		}
	}
	
	private final class DeleteFavouriteLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
			// TODO Auto-generated method stub
			Animation shake_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
			view.startAnimation(shake_anim);
			
			ImageView delete = (ImageView) view.findViewById(R.id.delete);
			delete.setVisibility(View.VISIBLE);
			delete.setTag(position);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (favourite.size() > 0) {
						getActivity().getContentResolver().delete(FavoriteColumns.CONTENT_URI, 
								FavoriteColumns.DOCTOR_ID + "=?", 
								new String[]{favourite.get((Integer)v.getTag()).doctor_id});
						favourite = getFavoriteList();
						dla.notifyDataSetChanged();
					}
				}
			});
			
			return true;
		}
	}

	private final class DoctorListClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> av, View view, int position, long id) {
			// TODO Auto-generated method stub
			MD5TimePass md5 = new MD5TimePass();
			String[] unixMD5 = md5.getMD5();
	        Calendar calendar = Calendar.getInstance();
	        int[] date_arr = new int[] {calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
			
			try {
				doctor_detail_parser = new DoctorDetailsJSONParser();
				doctor = doctor_detail_parser.getJSONData(Path.DOCTOR_DETAIL_URL + 
						PathParameter.uctime + unixMD5[0] + 
						PathParameter.hcode + unixMD5[1] + 
						PathParameter.doctor_id + favourite.get(position).doctor_id +
						PathParameter.place_id + favourite.get(position).place_id +
						PathParameter.start_date + date_arr[0] + "/" + (date_arr[1]+1) + "/" + date_arr[2] +
						PathParameter.end_date + date_arr[0] + "/" + (date_arr[1]+1) + "/" + date_arr[2]);
				
				if (doctor.doctor_user_id != null) {
					FragmentManager fm = getFragmentManager();

					if (fm != null) {
						FragmentTransaction ft = fm.beginTransaction();
						Fragment fragment = new SearchDetailFragment();
						Bundle bundle = new Bundle();
						
						if (favourite != null) {
							bundle.putString(BundleInformation.specialty_id, favourite.get(position).specialty_id);
							bundle.putString(BundleInformation.specialty_name, favourite.get(position).specialty_name);
							bundle.putIntArray(BundleInformation.date, date_arr);
							bundle.putParcelableArrayList(BundleInformation.specialty, doctor.specialty);
							bundle.putParcelableArrayList(BundleInformation.timeslot, doctor.timeslot.get(0).timeslot_list);
							bundle.putString(BundleInformation.doctor_id, favourite.get(position).doctor_id);
							bundle.putString(BundleInformation.doctor_name, favourite.get(position).doctor_name);
							bundle.putString(BundleInformation.doctor_profile_image, favourite.get(position).doctor_profile_image);
							bundle.putString(BundleInformation.doctor_professional_statement, favourite.get(position).doctor_professional_statement);
							bundle.putString(BundleInformation.doctor_specialty, favourite.get(position).doctor_specialty);
							bundle.putString(BundleInformation.place_id, favourite.get(position).place_id);
							bundle.putString(BundleInformation.place_address_line1,favourite.get(position).place_address);
							bundle.putString(BundleInformation.place_name, favourite.get(position).place_name);
							bundle.putString(BundleInformation.doctor_education, favourite.get(position).doctor_education);
							bundle.putString(BundleInformation.doctor_awards, favourite.get(position).doctor_awards);
						}

						fragment.setArguments(bundle);
						ft.replace(R.id.fragment_content, fragment);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
						ft.addToBackStack(null);
						ft.commit();
					}
				} else {
					Toast.makeText(getActivity(), "No Doctor Details", Toast.LENGTH_SHORT).show();
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

	private final class DoctorListAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder viewHolder;

			if (view == null) {
				view = inflater.inflate(R.layout.favourite_fragment_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.doctor_image = (ImageView) view.findViewById(R.id.doctor_image);
				viewHolder.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
				viewHolder.doctor_specialty = (TextView) view.findViewById(R.id.doctor_specialty);
				viewHolder.place_address = (TextView) view.findViewById(R.id.place_address_line1);
				viewHolder.place_name = (TextView) view.findViewById(R.id.place_name);

				view.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) view.getTag();
			}

			viewHolder.doctor_image.setTag(Path.THUMBNAIL_URL + favourite.get(position).doctor_profile_image);
			imageLoader.displayImage(Path.THUMBNAIL_URL + favourite.get(position).doctor_profile_image, getActivity(), viewHolder.doctor_image);
			viewHolder.doctor_name.setText(favourite.get(position).doctor_name);
			viewHolder.doctor_specialty.setText(favourite.get(position).doctor_specialty);
			viewHolder.place_address.setText(favourite.get(position).place_address);
			viewHolder.place_name.setText(favourite.get(position).place_name);

			return view;
		}

		public class ViewHolder {
			public ImageView doctor_image;
			public TextView doctor_name;
			public TextView doctor_specialty;
			public TextView place_address;
			public TextView place_name;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Favorite getItem(int position) {
			// TODO Auto-generated method stub
			return favourite.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (favourite != null)
				return favourite.size();
			else
				return 0;
		}
	}
}
