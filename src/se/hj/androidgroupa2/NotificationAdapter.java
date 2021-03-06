package se.hj.androidgroupa2;

import java.util.List;

import se.hj.androidgroupa2.objects.DoelibsNotification;
import se.hj.androidgroupa2.objects.UpdateDataInterface;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationAdapter extends ArrayAdapter<DoelibsNotification> {

	private Context _context;
	private UpdateDataInterface _updateRef;
	private Resources _res;
	
	private OnClickListener _onAcceptClickListener;
	private OnClickListener _onDeclineClickListener;
	private OnClickListener _onRemoveClickListener;
	
	public NotificationAdapter(Context context, int resource,
			int textViewResourceId, List<DoelibsNotification> objects, UpdateDataInterface updateRef) {
		super(context, resource, textViewResourceId, objects);
		_context = context;
		_res = context.getResources();
		_updateRef = updateRef;
		
		_onAcceptClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: CONTINUE
				DoelibsNotification noti = (DoelibsNotification) v.getTag();
				if (noti == null) return;

				ConnectivityManager conManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = conManager.getActiveNetworkInfo();
				
				if (info != null && info.isConnected())
				{
					DoelibsNotification.handleNotification(noti, true, new DoelibsNotification.CallbackReference() {
						@Override
						public void callbackFunction(Object result) {
							Boolean success = (Boolean) result;
							if (success == null) success = false;
							if (success)
							{
								Toast.makeText(_context, _res.getString(R.string.notification_info_handled), Toast.LENGTH_SHORT).show();
								_updateRef.updateData(); //TODO: remove just item instead of refreshing.
							}
							else
								Toast.makeText(_context, _res.getString(R.string.info_technicalIssues), Toast.LENGTH_LONG).show();
						}
					});
				}
				else
					Toast.makeText(_context, _res.getString(R.string.info_noInternet), Toast.LENGTH_LONG).show();
			}
		};
		_onDeclineClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				DoelibsNotification noti = (DoelibsNotification) v.getTag();
				if (noti == null) return;

				ConnectivityManager conManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = conManager.getActiveNetworkInfo();
				
				if (info != null && info.isConnected())
				{
					DoelibsNotification.handleNotification(noti, false, new DoelibsNotification.CallbackReference() {
						@Override
						public void callbackFunction(Object result) {
							Boolean success = (Boolean) result;
							if (success == null) success = false;
							if (success)
							{
								Toast.makeText(_context, _res.getString(R.string.notification_info_handled), Toast.LENGTH_SHORT).show();
								_updateRef.updateData(); //TODO: remove just item instead of refreshing.
							}
							else
								Toast.makeText(_context, _res.getString(R.string.info_technicalIssues), Toast.LENGTH_LONG).show();
						}
					});
				}
				else
					Toast.makeText(_context, _res.getString(R.string.info_noInternet), Toast.LENGTH_LONG).show();
			}
		};
		_onRemoveClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {

				DoelibsNotification noti = (DoelibsNotification) v.getTag();
				if (noti == null) return;

				ConnectivityManager conManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = conManager.getActiveNetworkInfo();
				
				if (info != null && info.isConnected())
				{
					DoelibsNotification.deleteNotification(noti.NotificationId, new DoelibsNotification.CallbackReference() {
						@Override
						public void callbackFunction(Object result) {
							Boolean success = (Boolean) result;
							if (success == null) success = false;
							
							if (success)
							{
								Toast.makeText(_context, _res.getString(R.string.notification_info_removed), Toast.LENGTH_SHORT).show();
								_updateRef.updateData(); //TODO: remove just item instead of refreshing.
							}
							else
								Toast.makeText(_context, _res.getString(R.string.info_technicalIssues), Toast.LENGTH_LONG).show();
						}
					});
				}
				else
					Toast.makeText(_context, _res.getString(R.string.info_noInternet), Toast.LENGTH_LONG).show();
			}
		};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rootView = super.getView(position, convertView, parent);
		
		TextView sender = (TextView) rootView.findViewById(R.id.notification_list_item_sender);
		TextView date = (TextView) rootView.findViewById(R.id.notification_list_item_date);
		TextView message = (TextView) rootView.findViewById(R.id.notification_list_item_text);
		Button leftButton = (Button) rootView.findViewById(R.id.notification_list_item_leftButton);
		Button rightButton = (Button) rootView.findViewById(R.id.notification_list_item_rightButton);
		LinearLayout root = (LinearLayout) rootView.findViewById(R.id.notification_list_item_root);
		
		DoelibsNotification not = getItem(position);
		
//		if (not.Read) root.setBackgroundColor(Color.WHITE);
//		else root.setBackgroundColor(_res.getColor(R.color.borrowing_item_yellow));
		
		if (not.Type == DoelibsNotification.NOTIFICATION_TYPE.REGISTRATION_ACCEPT_REQUEST.getNumVal() ||
				not.Type == DoelibsNotification.NOTIFICATION_TYPE.RENEW_EXPIRE_DATE_REQUEST.getNumVal())
		{
			leftButton.setEnabled(true);
			leftButton.setVisibility(View.VISIBLE);
			leftButton.setText(R.string.notification_accept);
			leftButton.setBackgroundColor(_res.getColor(R.color.notification_button_green));
			leftButton.setTag(not);
			leftButton.setOnClickListener(_onAcceptClickListener);
			
			rightButton.setVisibility(View.VISIBLE);
			rightButton.setText(R.string.notification_decline);
			rightButton.setBackgroundColor(_res.getColor(R.color.notification_button_red));
			rightButton.setTag(not);
			rightButton.setOnClickListener(_onDeclineClickListener);
		}
		else
		{
			leftButton.setEnabled(false);
			leftButton.setVisibility(View.INVISIBLE);

			rightButton.setVisibility(View.VISIBLE);
			rightButton.setText(R.string.notification_remove);
			rightButton.setBackgroundColor(_res.getColor(R.color.notification_button_red));
			rightButton.setTag(not);
			rightButton.setOnClickListener(_onRemoveClickListener);
		}
		
		if (not.Sender != null)
			sender.setText(not.Sender.FirstName + " " + not.Sender.LastName);
		else
			sender.setText(R.string.notification_fromSystem);
		
		date.setText(not.SendDate);
		message.setText(not.Message);
		
		return rootView;
	}
}
