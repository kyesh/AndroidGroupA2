package se.hj.androidgroupa2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import se.hj.androidgroupa2.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TitlePageFragment extends Fragment {

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
	    		 JSONArray Loanables = resultObject.getJSONArray("Loanables");
	    		 	    			 Log.i("kyesh", "In Else");
	    			 JSONObject titleObject = resultObject.getJSONObject("Title");
	    			 //TitleId = titleObject.getString("TitleId");
	    			 //Log.i("kyesh", "TitleId:"+TitleId);
	    		 
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
	private ListAdapter mAdapter;

	public TitlePageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: Change Adapter to display your content
		mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				DummyContent.ITEMS);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tittlepage, container,
				false);

		// Set the adapter
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

		return view;
	}

}
