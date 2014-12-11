package se.hj.androidgroupa2;

import se.hj.androidgroupa2.objects.StoredDataName;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class SettingsFragment extends Fragment {

	Resources _res;
	Context _context;
	Switch _shakeToggle;
	SharedPreferences _pref;
	
	public SettingsFragment()
	{}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		
		_res = getActivity().getResources();
		_context = getActivity();
		_pref = _context.getSharedPreferences(StoredDataName.SHARED_PREF, Activity.MODE_PRIVATE);
		getActivity().setTitle(R.string.title_activity_settings);
		
		if (getActivity().getClass() == MainActivity.class)
		{
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.updateCheckedMenuItem(mainActivity.getMenuItemPosition(MainActivity.NAV_ITEM_STAFF.SETTINGS.getNumVal()));
		}
		_shakeToggle = (Switch) rootView.findViewById(R.id.settings_shakeToggle);
		
		_shakeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				SharedPreferences.Editor pref = _pref.edit();
				if (isChecked)
					pref.putBoolean(StoredDataName.PREF_SETTINGS_SHAKE, true);
				else
					pref.putBoolean(StoredDataName.PREF_SETTINGS_SHAKE, false);
				pref.commit();
			}
		});

		_shakeToggle.setChecked(_pref.getBoolean(StoredDataName.PREF_SETTINGS_SHAKE, true));
		
		return rootView;
	}
}
