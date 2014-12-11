package se.hj.androidgroupa2;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import se.hj.androidgroupa2.objects.*;
import se.hj.androidgroupa2.objects.Loanable.STATUS;
public class LoanableAdapter extends ArrayAdapter<Loanable> {
private Context _context;
private Button CheckOutBtn;
private UpdateDataInterface _dataUpdate;

	public LoanableAdapter(Context context, int resource, int textViewResourceId, List<Loanable> objects, UpdateDataInterface ref) {
		super(context, resource, textViewResourceId, objects);
		_context = context;
		_dataUpdate = ref;
	}
	/*
	public LoanableAdapter(Context context, List<Loanable> objects) {
		super(context, 0, objects);
	}*/
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*Log.i("kyesh","Position:" + position);
		Log.i("kyesh", "convertView"+convertView);
		Log.i("kyesh", "parent"+parent);*/
		
		/*if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.loanable_layout, parent, false);
	       }
		*/
		View itemView = super.getView(position, convertView, parent);
		final Loanable loanable = getItem(position);
		
		TextView DoeLibsId = (TextView) itemView.findViewById(R.id.DoeLibsId);
		TextView Location = (TextView)itemView.findViewById(R.id.Location);
		CheckOutBtn = (Button) itemView.findViewById(R.id.CheckOutBtn);

		
			DoeLibsId.setText(loanable.Barcode.toString());
			if (loanable.Owner != null)
				Location.setText(loanable.Owner.Room + " ("+ loanable.Category +")");
			else
				Location.setText("Unknown" + " ("+ loanable.Category +")");
			if(loanable.Status != STATUS.AVAILABLE)
			{
				CheckOutBtn.setActivated(false);
				CheckOutBtn.setText("Unavailable");
			}
			else if (ApiHelper.LoggedInUser == null)
			{
				CheckOutBtn.setActivated(false);
				CheckOutBtn.setText("Not logged in");
			}
			else
			{
				CheckOutBtn.setActivated(true);
				CheckOutBtn.setText(R.string.checkoutbtn);
//				CheckOutBtn.setBackgroundColor(Color.rgb(34, 230, 86));
				CheckOutBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Loanable.checkOutLoanable(loanable.LoanableId, new Loanable.CallbackReference() {
							@Override
							public void callbackFunction(Object result) {
								
								Boolean success = (Boolean) result;
								if (success == null) success = false;
									
								if(success)
								{
									Toast toast = Toast.makeText(_context, "Loanable successfully checked out", Toast.LENGTH_SHORT);
									toast.show();
									_dataUpdate.updateData();
								}
								else
								{
									Toast toast = Toast.makeText(_context, "Something went wrong", Toast.LENGTH_SHORT);
									toast.show();
								}
							}
						});
					}
				});
			}
		
		
		return itemView;
	}
}

