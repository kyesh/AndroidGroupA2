package se.hj.androidgroupa2;

import se.hj.androidgroupa2.objects.Title;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TitleDetailFragment extends Fragment {
	
	private Title title;
	
	private TextView _title ;
	private TextView _authors;
	private TextView _ISBN10;
	private TextView _ISBN13 ;
	private TextView _publicationYear;
	private TextView _publisher;
	private TextView _topics ;


	public TitleDetailFragment()
	{}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_titledetails, container, false);
		getActivity().setTitle("Title details");
		
		_title = (TextView) rootView.findViewById(R.id.TitleDetails_Title);
		_authors = (TextView) rootView.findViewById(R.id.TitleDetails_Author);
		_ISBN10 = (TextView) rootView.findViewById(R.id.TitleDetails_ISBN10);
		_ISBN13 = (TextView) rootView.findViewById(R.id.TitleDetails_ISBN13);
		_publicationYear = (TextView) rootView.findViewById(R.id.TitleDetails_Puplicationyear);
		_publisher = (TextView) rootView.findViewById(R.id.TitleDetails_Publisher);
		_topics = (TextView) rootView.findViewById(R.id.TitleDetails_Topics);
		
		Bundle args = getArguments();
	    Title title = (Title) args
	        .getSerializable("TAG_TO_TITLE");
		
//		title = new Title();
//		title.BookTitle = "Emil lagar mat";
//		title.ISBN10 = "1234567890";
//		title.ISBN13 = "1234567890123";
//		title.EditionYear = 2014;
		
		if(title != null)
		{
	    _title.setText(title.BookTitle);
	    _authors.setText("Emil lygnebrandt, Joanthan Holm, Alexander Lagerqvist");
	    _ISBN10.setText(title.ISBN10);
	    _ISBN13.setText(title.ISBN13);
	    _publicationYear.setText(title.EditionYear.toString());
	    _publisher.setText("Bonniers");
	    _topics.setText("Food and stuff");
		}
	    
//	    _title.setText(title.BookTitle);
//	    _authors.setText(title.);
//	    _ISBN10.setText(title.ISBN10);
//	    _ISBN13.setText(title.ISBN13);
//	    _publicationYear.setText(title.EditionYear);
//	    _publisher.setText(title.Publisher.Name);
//	    _title.setText(title.);
		
		return rootView;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		
//		Bundle args = getArguments();
//	    Title title = (Title) args.getSerializable(TAG_TITLE);
//	    
	}
	
	
}

