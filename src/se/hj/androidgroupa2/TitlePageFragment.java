package se.hj.androidgroupa2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import se.hj.androidgroupa2.dummy.DummyContent;
import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.Title;


/*
 * How to call a Title Page Fragment
 * 
   			FragmentManager fragmentManager = getFragmentManager();
	        Fragment fragment = new TitlePageFragment();
	        Bundle args = new Bundle();
	        args.putString("TitleId", TitleId);
	        fragment.setArguments(args);
	        fragmentManager.beginTransaction()
	        				.replace(R.id.content_frame, fragment)
	        				.commit();
 */
public class TitlePageFragment extends Fragment {

	private TextView bookTitle ;
	private ListView loanables;
	private LoanableAdapter loanableAdapter;
	
	private OnFragmentCompleteListener _callbackActivity;
	private Title _title;
	
	 private class getBookTitle extends AsyncTask<String, Integer, String> {
	     protected String doInBackground(String... TitleId) {
	    	 
	    	 Log.i("kyesh", "doInBackground Running:"+TitleId[0]);
	    	 String APIURL = "http://doelibs-001-site1.myasp.net/api/title/" + TitleId[0];
	    	 StringBuilder bookBuilder = new StringBuilder();
	    	 HttpClient bookClient = new DefaultHttpClient();
	    	 Log.i("kyesh", "APIURL:"+APIURL);
	    	 try {
	    		    //get the data
	    		 HttpGet bookGet = new HttpGet(APIURL);
	    		 HttpResponse bookResponse = bookClient.execute(bookGet);
	    		 
	    		 StatusLine bookSearchStatus = bookResponse.getStatusLine();
	    		 if (bookSearchStatus.getStatusCode()==200) {
	    		     //we have a result
	    			 HttpEntity bookEntity = bookResponse.getEntity();
	    			 
	    			 InputStream bookContent = bookEntity.getContent();
	    			 InputStreamReader bookInput = new InputStreamReader(bookContent);
	    			 BufferedReader bookReader = new BufferedReader(bookInput);
	    			 
	    			 String lineIn;
	    			 while ((lineIn=bookReader.readLine())!=null) {
	    			     bookBuilder.append(lineIn);
	    			 }
	    			 Log.i("kyesh", "Server Response:"+bookBuilder.toString());
	    			 return bookBuilder.toString();
	    			 
	    		 }
	    		 
	    		}
	    		catch(Exception e){ e.printStackTrace(); }
	    	 
	         return null;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(String result) {
	    	 String TitleId;
	    	 Log.i("kyesh", "onPostExecute running");
	    	 try{
	    		//parse results
	    		 JSONObject resultObject = new JSONObject(result);
	    		 JSONArray LoanablesJSON = resultObject.getJSONArray("Loanables");
	    		 JSONObject titleObject = resultObject.getJSONObject("Title");
	    		 _title = Title.parseTitleFromJSONObject(titleObject);
	    		 
	    		 ArrayList<JSONObject> loanableList = new ArrayList<JSONObject>();
	    		 bookTitle.setText(titleObject.getString("BookTitle"));
	    		 
	    		 
	    		 
	    		 for(int i = 0; i < LoanablesJSON.length(); i++){
	    			 loanableList.add(LoanablesJSON.getJSONObject(i));
	    		 }
	    		 
	    		 	loanableAdapter = new LoanableAdapter( getActivity(), R.layout.loanable_layout, R.id.DoeLibsId, loanableList);
	    			//LoanableAdapter  loanableAdapter = new LoanableAdapter( TitlePageFragment.this , R.layout.loanable_layout, R.id.DoeLibsId ,loanableList );
	    			loanables.setAdapter(loanableAdapter);
	    		 
	    		}
	    		catch (Exception e) {
	    		//no result
	    			Log.i("kyesh", "Exeception",e);
	    			e.printStackTrace();
	    		}
	     }
	 }
	
	private AbsListView mListView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */


	public TitlePageFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		_callbackActivity = (OnFragmentCompleteListener) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//loanableAdapter.
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tittlepage, container,
				false);
		
		loanables = (ListView) view.findViewById(R.id.loanables);
		bookTitle = (TextView) view.findViewById(R.id.Title);
		//loanables.setAdapter(loanableAdapter);
		
		Bundle args = getArguments();
		new getBookTitle().execute(args.getString("TitleId"));
		
		bookTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putSerializable("TAG_TO_TITLE", _title);
				Fragment fragment = new TitleDetailFragment();
				fragment.setArguments(args);
				_callbackActivity.onFragmentComplete(TitlePageFragment.this, fragment);
			}
		});

		return view;
	}
}
