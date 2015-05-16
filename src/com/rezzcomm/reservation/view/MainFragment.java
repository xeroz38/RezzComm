package com.rezzcomm.reservation.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rezzcomm.reservation.R;
import com.rezzcomm.reservation.view.appointments.AppointmentsFragment;
import com.rezzcomm.reservation.view.favourite.FavouriteFragment;
import com.rezzcomm.reservation.view.search.SearchFragment;
import com.rezzcomm.reservation.view.setting.SettingFragment;

public class MainFragment extends Fragment {

	private boolean isVisible = false;
	
	private LinearLayout menu;
	private Button search, favourite, appointments, settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_fragment, container, false);

		menu = (LinearLayout) view.findViewById(R.id.main_menu);
		
		search = (Button) view.findViewById(R.id.search);
		search.setOnClickListener(new MenuClickListener());
		favourite = (Button) view.findViewById(R.id.favourite);
		favourite.setOnClickListener(new MenuClickListener());
		appointments = (Button) view.findViewById(R.id.appointments);
		appointments.setOnClickListener(new MenuClickListener());
		settings = (Button) view.findViewById(R.id.settings);
		settings.setOnClickListener(new MenuClickListener());

		return view;
	}

	private final class MenuClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			Drawable searchNormal = getActivity().getResources().getDrawable(R.drawable.search);
			Drawable favouriteNormal = getActivity().getResources().getDrawable(R.drawable.favourite);
			Drawable appointmentsNormal = getActivity().getResources().getDrawable(R.drawable.appointments);
			Drawable settingNormal = getActivity().getResources().getDrawable(R.drawable.settings);
			
			search.setCompoundDrawablesWithIntrinsicBounds(null, searchNormal, null, null);
			favourite.setCompoundDrawablesWithIntrinsicBounds(null, favouriteNormal, null, null);
			appointments.setCompoundDrawablesWithIntrinsicBounds(null, appointmentsNormal, null, null);
			settings.setCompoundDrawablesWithIntrinsicBounds(null, settingNormal, null, null);
			
			search.setBackgroundResource(0);
			favourite.setBackgroundResource(0);
			appointments.setBackgroundResource(0);
			settings.setBackgroundResource(0);

			switch (v.getId())
			{
				case R.id.search: {
					loadMenu(1);
					break;
				}
				case R.id.favourite: {
					loadMenu(2);
					break;
				}
				case R.id.appointments: {
					loadMenu(3);
					break;
				}
				case R.id.settings: {
					loadMenu(4);
					break;
				}
				default:break;
			}
		}
	}

	public void loadMenu(int menu_id) {
		Drawable searchPress = getActivity().getResources().getDrawable(R.drawable.search_pressed);
		Drawable favouritePress = getActivity().getResources().getDrawable(R.drawable.favourite_pressed);
		Drawable appointmentsPress = getActivity().getResources().getDrawable(R.drawable.appointments_pressed);
		Drawable settingPress = getActivity().getResources().getDrawable(R.drawable.settings_pressed);
		
		FragmentManager fm = getFragmentManager();

		if (fm != null) {
			FragmentTransaction ft = fm.beginTransaction();
			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			switch (menu_id)
			{
				case 1: {
					ft.replace(R.id.fragment_content, new SearchFragment());
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					search.setBackgroundResource(R.drawable.menu_bg_pressed);
					search.setCompoundDrawablesWithIntrinsicBounds(null, searchPress, null, null);
					break;
				}
				case 2: {
					ft.replace(R.id.fragment_content, new FavouriteFragment());
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					favourite.setBackgroundResource(R.drawable.menu_bg_pressed);
					favourite.setCompoundDrawablesWithIntrinsicBounds(null, favouritePress, null, null);
					break;
				}
				case 3: {
					ft.replace(R.id.fragment_content, new AppointmentsFragment());
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					appointments.setBackgroundResource(R.drawable.menu_bg_pressed);
					appointments.setCompoundDrawablesWithIntrinsicBounds(null, appointmentsPress, null, null);
					break;
				}
				case 4: {
					ft.replace(R.id.fragment_content, new SettingFragment());
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					settings.setBackgroundResource(R.drawable.menu_bg_pressed);
					settings.setCompoundDrawablesWithIntrinsicBounds(null, settingPress, null, null);
					break;
				}
				default:break;
			}

			ft.commit();
		}
	}
	
	public void setVisibleMenu() {
		if (isVisible) {
//			final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_hide);
			menu.setVisibility(View.GONE);
//			menu.startAnimation(animation);
			isVisible = false;
		} else {
//			final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.menu_show);
			menu.setVisibility(View.VISIBLE);
//			menu.startAnimation(animation);
			isVisible = true;
		}
	}
}
