package se.hj.androidgroupa2;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LoanableAdapter extends ArrayAdapter<JSONObject> {

	public LoanableAdapter(Context context, int resource, int textViewResourceId, List<JSONObject> objects) {
		super(context, resource, textViewResourceId, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = super.getView(position, convertView, parent);
		
		JSONObject loanable = getItem(position);
		
		TextView DoeLibsId = (TextView) itemView.findViewById(R.id.DoeLibsId);
		TextView Location = (TextView)itemView.findViewById(R.id.Location);

		
		try {
			DoeLibsId.setText(loanable.getString("Barcode"));
			Location.setText(loanable.getJSONObject("Owner").getString("Room") + "("+ loanable.getJSONObject("Category").getString("Name") +")");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("kyesh", "Exception", e);
		}
		
		
		return itemView;
	}
}

