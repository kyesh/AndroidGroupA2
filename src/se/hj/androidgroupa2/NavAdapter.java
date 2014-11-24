package se.hj.androidgroupa2;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavAdapter extends ArrayAdapter<NavAdapterItem> {

	public NavAdapter(Context context, int resource, int textViewResourceId,
			List<NavAdapterItem> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		NavAdapterItem item = getItem(position);
		
	 	View dividerTop = view.findViewById(R.id.drawer_list_item_dividerTop);
		if (item.DividerTop) dividerTop.setVisibility(View.VISIBLE);
		else dividerTop.setVisibility(View.GONE);

	 	View dividerBottom = view.findViewById(R.id.drawer_list_item_dividerBottom);
		if (item.DividerBottom) dividerBottom.setVisibility(View.VISIBLE);
		else dividerBottom.setVisibility(View.GONE);

		ImageView icon = (ImageView) view.findViewById(R.id.drawer_list_item_icon);
		if (item.Icon != null)
		{
			icon.setImageDrawable(item.Icon);
			icon.setVisibility(View.VISIBLE);
		}
		else
			icon.setVisibility(View.GONE);

		TextView header = (TextView) view.findViewById(R.id.drawer_list_item_header);
		if (!item.HeaderText.trim().isEmpty())
		{
			header.setText(item.HeaderText);
			header.setVisibility(View.VISIBLE);
		}
		else header.setVisibility(View.GONE);
		
		return view;
	}
}
