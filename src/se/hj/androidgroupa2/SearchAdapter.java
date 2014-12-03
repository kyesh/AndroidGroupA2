package se.hj.androidgroupa2;

import java.util.List;

import se.hj.androidgroupa2.objects.ExtendedTitle;
import se.hj.androidgroupa2.objects.Title;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends ArrayAdapter<ExtendedTitle> {
	
	private TextView _title;
	private TextView _authors;
	private TextView _editionYear;

	public SearchAdapter(Context context, int resource, int textViewResourceId,
			List<ExtendedTitle> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
				
		_title = (TextView) view.findViewById(R.id.search_item_title);
		_authors = (TextView) view.findViewById(R.id.search_item_author);
		_editionYear = (TextView) view.findViewById(R.id.search_item_year);
		
		ExtendedTitle title = getItem(position);
		
		_title.setText(title.TitleInformation.BookTitle);
		_authors.setText(title.Authors.toString());
		_editionYear.setText(title.TitleInformation.EditionYear.toString());
		
		return view;
	}
}