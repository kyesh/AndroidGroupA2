package se.hj.androidgroupa2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.hj.androidgroupa2.objects.DoelibsNotification;
import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.UpdateDataInterface;
import se.hj.androidgroupa2.objects.User;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NotificationFragment extends Fragment implements UpdateDataInterface {

	OnFragmentCompleteListener _onFragmentComplete;
	Resources _res;
	ProgressBar _progressBar;
	ListView _list;
	NotificationAdapter _listAdapter;
	ArrayList<DoelibsNotification> _listItems;
	
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
		View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
		
		_res = getActivity().getResources();
		
		_progressBar = (ProgressBar) rootView.findViewById(R.id.notification_progressBar);
		_list = (ListView) rootView.findViewById(R.id.notification_list);
		
		_listItems = new ArrayList<DoelibsNotification>();
		_listAdapter = new NotificationAdapter(getActivity(), R.layout.notification_list_item, R.id.notification_list_item_sender, _listItems, this);
		_list.setAdapter(_listAdapter);
		
		updateData();
		
		return rootView;
	}
	
	@Override
	public void updateData() {

		_progressBar.setVisibility(View.VISIBLE);
		_list.setVisibility(View.GONE);
		_listItems.clear();
		
		ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		
		if (info != null && info.isConnected())
		{
			DoelibsNotification.getNotificationsForCurrentUser(new DoelibsNotification.CallbackReference() {
				@Override
				public void callbackFunction(Object result) {
					
					ArrayList<DoelibsNotification> notifications = (ArrayList<DoelibsNotification>) result;
					_listItems.addAll(notifications);
					
					Collections.sort(_listItems, new Comparator<DoelibsNotification>() {
						
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						// Sorts so that latest (newest) gets first.
						// TODO: maybe store more of date to get better sorting.
						@Override
						public int compare(DoelibsNotification lhs, DoelibsNotification rhs) {
							int result;
							try {
								result = format.parse(rhs.SendDate).compareTo(format.parse(lhs.SendDate));
							} catch (Exception e) {
								Log.e("SORT DATE", "in notifications");
								result = rhs.SendDate.compareTo(lhs.SendDate);
							}
							if (result == 0)
							{
								if (!rhs.Read && lhs.Read) return 1;
								else if (rhs.Read && !lhs.Read) return -1;
							}
							return result;
						}
					});
					
					_listAdapter.notifyDataSetChanged();
					
					_progressBar.setVisibility(View.GONE);
					_list.setVisibility(View.VISIBLE);
				}
			});
		}
		else
		{
			Toast.makeText(getActivity(), _res.getString(R.string.info_noInternet), Toast.LENGTH_LONG).show();
			_progressBar.setVisibility(View.GONE);
			_list.setVisibility(View.VISIBLE);
		}
	}
}
