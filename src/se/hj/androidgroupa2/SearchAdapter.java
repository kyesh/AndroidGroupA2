package se.hj.androidgroupa2;

import java.util.List;

import se.hj.androidgroupa2.objects.Title;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends ArrayAdapter<Title> {
	
	private TextView _title;
	private TextView _authors;
	private TextView _editionYear;

	public SearchAdapter(Context context, int resource, int textViewResourceId,
			List<Title> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
				
		_title = (TextView) view.findViewById(R.id.search_item_title);
		_authors = (TextView) view.findViewById(R.id.search_item_author);
		_editionYear = (TextView) view.findViewById(R.id.search_item_year);
		
		Title title = getItem(position);
		
		_title.setText(title.BookTitle);
		_authors.setText("");
		_editionYear.setText(title.EditionYear);
		
		return view;
	}
}