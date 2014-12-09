package se.hj.androidgroupa2;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import se.hj.androidgroupa2.objects.*;
import se.hj.androidgroupa2.objects.Loanable.STATUS;
public class LoanableAdapter extends ArrayAdapter<Loanable> {

	public LoanableAdapter(Context context, int resource, int textViewResourceId, List<Loanable> objects) {
		super(context, resource, textViewResourceId, objects);
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
		Loanable loanable = getItem(position);
		
		TextView DoeLibsId = (TextView) itemView.findViewById(R.id.DoeLibsId);
		TextView Location = (TextView)itemView.findViewById(R.id.Location);
		Button CheckOutBtn = (Button) itemView.findViewById(R.id.CheckOutBtn);

		
			DoeLibsId.setText(loanable.Barcode.toString());
			if (loanable.Owner != null)
				Location.setText(loanable.Owner.Room + " ("+ loanable.Category +")");
			else
				Location.setText("Unknown" + " ("+ loanable.Category +")");
			if(loanable.Status != STATUS.AVAILABLE)
			{
				CheckOutBtn.setText("Unavailable");
				CheckOutBtn.setActivated(false);
			}
			else
			{
				CheckOutBtn.setActivated(true);
				CheckOutBtn.setText(R.string.checkoutbtn);
				CheckOutBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		
		
		return itemView;
	}
}

