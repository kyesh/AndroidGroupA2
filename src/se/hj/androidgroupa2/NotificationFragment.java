package se.hj.androidgroupa2;

import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.UpdateDataInterface;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationFragment extends Fragment implements UpdateDataInterface {

	OnFragmentCompleteListener _onFragmentComplete;
	
	public NotificationFragment()
	{}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_onFragmentComplete = (OnFragmentCompleteListener) getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
	}
}
