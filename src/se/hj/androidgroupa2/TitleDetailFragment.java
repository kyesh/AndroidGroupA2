package se.hj.androidgroupa2;

import se.hj.androidgroupa2.objects.ExtendedTitle;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.Title;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TitleDetailFragment extends Fragment {
	
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
		
		_title = (TextView) rootView.findViewById(R.id.TitleDetails_Title);
		_authors = (TextView) rootView.findViewById(R.id.TitleDetails_Author);
		_ISBN10 = (TextView) rootView.findViewById(R.id.TitleDetails_ISBN10);
		_ISBN13 = (TextView) rootView.findViewById(R.id.TitleDetails_ISBN13);
		_publicationYear = (TextView) rootView.findViewById(R.id.TitleDetails_Puplicationyear);
		_publisher = (TextView) rootView.findViewById(R.id.TitleDetails_Publisher);
		_topics = (TextView) rootView.findViewById(R.id.TitleDetails_Topics);
		
		Bundle args = getArguments();
	    ExtendedTitle title = (ExtendedTitle) args.getSerializable(StoredDataName.ARGS_EXTENDED_TITLE);
		
		if (title != null)
		{
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
		    _ISBN10.setText(title.TitleInformation.ISBN10);
		    _ISBN13.setText(title.TitleInformation.ISBN13);
		    _publicationYear.setText(title.TitleInformation.EditionYear.toString());
		    _publisher.setText(title.TitleInformation.Publisher.Name);

		    String topics = "";
		    for (int i = 0; i < title.Topics.size(); i++)
		    {
		    	if (i == 0)
		    		topics += title.Topics.get(i).TopicName;
		    	else
		    		topics = topics + ", " + title.Topics.get(i).TopicName;
		    }
		    _topics.setText(topics);
		}
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}

