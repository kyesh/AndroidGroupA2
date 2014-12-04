package se.hj.androidgroupa2;

import java.util.List;

import se.hj.androidgroupa2.objects.ExtendedTitle;
import se.hj.androidgroupa2.objects.Title;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		
		LinearLayout rootView = (LinearLayout) view.findViewById(R.id.search_item_root);
		if ((position % 2) == 0)
		{
			rootView.setBackgroundResource(R.drawable.list_search_background_even);
			_title.setTextColor(view.getResources().getColor(R.color.search_item_textColor_even));
			_authors.setTextColor(view.getResources().getColor(R.color.search_item_textColor_even));
			_editionYear.setTextColor(view.getResources().getColor(R.color.search_item_textColor_even));
		}
		else
		{
			rootView.setBackgroundResource(R.drawable.list_search_background_odd);
			_title.setTextColor(view.getResources().getColor(R.color.search_item_textColor_odd));
			_authors.setTextColor(view.getResources().getColor(R.color.search_item_textColor_odd));
			_editionYear.setTextColor(view.getResources().getColor(R.color.search_item_textColor_odd));
		}
		
		ExtendedTitle title = getItem(position);
		
		_title.setText(title.TitleInformation.BookTitle);
		
		String authors = "";
		for (int i = 0; i < title.Authors.size(); i++)
		{
			if (i == 0)
				authors += title.Authors.get(i).Name;
			else
				authors = authors + ", " + title.Authors.get(i).Name;
		}
		_authors.setText(authors);
		
		_editionYear.setText(title.TitleInformation.EditionYear.toString());
		
		return view;
	}
}