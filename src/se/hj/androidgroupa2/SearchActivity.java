package se.hj.androidgroupa2;

import java.util.ArrayList;
import java.util.List;

import se.hj.androidgroupa2.objects.ExtendedTitle;
import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.Title;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class SearchActivity extends Fragment {
	
	private ListView _listview;
	private List<ExtendedTitle> _items = new ArrayList<ExtendedTitle>();
	
	private OnFragmentCompleteListener _callbackActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		_callbackActivity = (OnFragmentCompleteListener) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_search, container, false);
		
		getActivity().setTitle(R.string.title_activity_search);
		
		_listview = (ListView) mainView.findViewById(R.id.fragment_search);
		
		SearchAdapter adapter = new SearchAdapter(getActivity(), R.layout.fragment_search, R.id.search_item_title, _items);
		
		_listview.setAdapter(adapter);
		_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				_callbackActivity.onFragmentComplete(SearchActivity.this, _items.get(position));
			}
		});
		
		Bundle bundle = getArguments();
		String query = bundle.getString(StoredDataName.ARGS_SEARCH_QUERY);
		ExtendedTitle.getTitlesFromSearch(query, new ExtendedTitle.CallbackReference() {
			@Override
			public void callbackFunction(List<ExtendedTitle> titles) {
				/*SearchAdapter adapter = (SearchAdapter) _listview.getAdapter();
				adapter.clear();
				_items = titles;
				adapter.addAll(titles);
				adapter.notifyDataSetChanged();*/
				//TODO: FIXXX!!
			}
		});
		
		return mainView;
	}
}
