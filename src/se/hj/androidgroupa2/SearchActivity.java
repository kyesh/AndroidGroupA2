package se.hj.androidgroupa2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class SearchActivity extends Fragment {
	
	private ListView _listview;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_search, container, false);
		getActivity().setTitle(R.string.title_activity_search);
		
		_listview = (ListView) mainView.findViewById(R.id.fragment_search);
		
		return mainView;
	}
}
