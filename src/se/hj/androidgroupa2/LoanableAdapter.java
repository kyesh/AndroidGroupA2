package se.hj.androidgroupa2;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import se.hj.androidgroupa2.objects.*;
public class LoanableAdapter extends ArrayAdapter<Loanable> {

	public LoanableAdapter(Context context, int resource, int textViewResourceId, List<Loanable> objects) {
		super(context, resource, textViewResourceId, objects);
	}
	
	public LoanableAdapter(Context context, List<Loanable> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("kyesh","Position:" + position);
		Log.i("kyesh", "convertView"+convertView);
		Log.i("kyesh", "parent"+parent);
		
		if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
	       }
		
		View itemView = super.getView(position, convertView, parent);
		
		Loanable loanable = getItem(position);
		
		TextView DoeLibsId = (TextView) itemView.findViewById(R.id.DoeLibsId);
		TextView Location = (TextView)itemView.findViewById(R.id.Location);

		
			DoeLibsId.setText(loanable.LoanableId);
			Location.setText(loanable.Owner.Room + "("+ loanable.Category +")");

		
		
		return itemView;
	}
}

