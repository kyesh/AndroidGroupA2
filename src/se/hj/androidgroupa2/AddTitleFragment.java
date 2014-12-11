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
import org.json.JSONException;
import org.json.JSONObject;

import se.hj.androidgroupa2.objects.ApiHelper;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.simpleTitle;

import com.google.zxing.integration.android.IntentIntegrator;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link AddTitleFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AddTitleFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AddTitleFragment extends Fragment implements OnClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	private MainActivity _ref;
	
	private class addTitle extends AsyncTask<simpleTitle, Integer, String>{
		protected String doInBackground(simpleTitle... simpleTitles) {
			
			
			Log.i("kyesh","doInBackgroundRunning");
			return ApiHelper.AddTitle(simpleTitles[0]);
	    	 
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(String result) {
	    	 
	    	 Log.i("kyesh","onPostExecuteRunning");
	    	 
	    	 try {
				JSONObject resultObject = new JSONObject(result);
				Fragment fragment = new TitlePageFragment();
		        Bundle args = new Bundle();
		        args.putString(StoredDataName.ARGS_TITLEID, resultObject.getString("TitleId"));
		        Log.i("kyesh",resultObject.getString("TitleId"));
		        fragment.setArguments(args);
		        ((MainActivity) getActivity()).setActiveFragment(fragment, "Title Page", true);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast toast = Toast.makeText(getActivity().getApplicationContext(), 
			    		result, Toast.LENGTH_LONG);
				toast.show();
				e.printStackTrace();
			}
	    	 
	    	 
	    	 
	     }
	}
	
	private class getFromGoogleAPI extends AsyncTask<String, Integer, String>{
		protected String doInBackground(String... ISBN) {
			Log.i("kyesh","doInBackgroundRunning");
			
			String URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+ISBN[0];
			
			StringBuilder bookBuilder = new StringBuilder();
			
			HttpClient bookClient = new DefaultHttpClient();
			
			try {
			    //get the data
				HttpGet bookGet = new HttpGet(URL);
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
					
				}
			}
			catch(Exception e){ e.printStackTrace(); }
			
			
			return bookBuilder.toString();
	    	 
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(String result) {
	    	 Log.i("kyesh","onPostExecuteRunning");
	    	 
	    	 try{
	    		//parse results
	    		 JSONObject resultObject = new JSONObject(result);
	    		 JSONArray bookArray = resultObject.getJSONArray("items");
	    		 JSONObject bookObject = bookArray.getJSONObject(0);
	    		 JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
	    		 
	    		 try{ _title.setText(volumeObject.getString("title")); }
	    		 catch(JSONException jse){ 
	    		     jse.printStackTrace(); 
	    		 }
	    		 
	    		 StringBuilder authorBuild = new StringBuilder("");
	    		 try{
	    		     JSONArray authorArray = volumeObject.getJSONArray("authors");
	    		     for(int a=0; a<authorArray.length(); a++){
	    		         if(a>0) authorBuild.append(", ");
	    		         authorBuild.append(authorArray.getString(a));
	    		     }
	    		     _Authors.setText(authorBuild.toString());
	    		 }
	    		 catch(JSONException jse){ 
	    		     jse.printStackTrace(); 
	    		 }
	    		 
	    		 try{ _Year.setText(volumeObject.getString("publishedDate").substring(0, 4)); }
	    		 catch(JSONException jse){ 
	    		     jse.printStackTrace(); 
	    		 }
	    		 
	    		 try{_Publisher.setText(volumeObject.getString("publisher")); }
	    		 catch(JSONException jse){ 
	    		     jse.printStackTrace(); 
	    		     Log.i("kyesh","Publisher Failed");
	    		 }
	    		 
	    		 /*try{
	    			 _ISBN10.setText(volumeObject.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier"));
	    			 }
	    		 catch(JSONException jse){ 
	    		     jse.printStackTrace(); 
	    		 }*/	    		 
	    		 
	    		}
	    		catch (Exception e) {
	    		//no result
	    			e.printStackTrace();
	    			Toast toast = Toast.makeText(getActivity().getApplicationContext(), 
	    		    		"Uh Oh! Something went wrong", Toast.LENGTH_SHORT);
	    		    toast.show();
	    		}
	    	 
	     }
	}
	
	private EditText _title, _ISBN10, _ISBN13, _Authors, _Year, _FirstEditionYear, _Publisher, _Topics, _Edition;
	private Button _addTitle;
	private View _addByCamera;


	public AddTitleFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Log.i("kyesh","AddTitleFragment-OnCreateView");
		
		String ISBN13;
		
		View rootView =  inflater.inflate(R.layout.fragment_add_title, container, false);
		getActivity().setTitle(R.string.title_activity_addTitle);
		
		if (getActivity().getClass() == MainActivity.class)
		{
			MainActivity mainActivity = (MainActivity) getActivity();
			_ref = mainActivity;
			mainActivity.updateCheckedMenuItem(mainActivity.getMenuItemPosition(MainActivity.NAV_ITEM_STAFF.ADD_TITLE.getNumVal()));
		}
		
		_title = (EditText) rootView.findViewById(R.id.TitleField);
		_ISBN10 = (EditText) rootView.findViewById(R.id.ISBN10Field);
		_ISBN13 = (EditText) rootView.findViewById(R.id.ISBN13Field);
		_Authors = (EditText) rootView.findViewById(R.id.AuthorsField);
		_Year = (EditText) rootView.findViewById(R.id.YearField);
		_FirstEditionYear = (EditText) rootView.findViewById(R.id.FirstEditionYearField);
		_Publisher = (EditText) rootView.findViewById(R.id.PublisherField);
		_Topics = (EditText) rootView.findViewById(R.id.TopicsField);
		_Edition = (EditText) rootView.findViewById(R.id.EditionField);
		
		_addTitle = (Button) rootView.findViewById(R.id.addTitleButton);
		_addByCamera = rootView.findViewById(R.id.addByCamera);
		
		_addTitle.setOnClickListener(this);
		_addByCamera.setOnClickListener(this);
		
		Bundle passedArugments = getArguments();
		
		//Log.i("kyesh",passedArugments.toString());
		if(passedArugments != null && passedArugments.getString("ISBN13") != null){
			ISBN13 = passedArugments.getString("ISBN13");
			_ISBN13.setText(ISBN13);
			new getFromGoogleAPI().execute(ISBN13);
		}
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		/*try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}*/
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.addTitleButton){
			//addTitle
			simpleTitle titleValues = new simpleTitle();
			titleValues.titleInfo = _title.getText().toString();
			titleValues.ISBN10info = _ISBN10.getText().toString();
			titleValues.ISBN13info = _ISBN13.getText().toString();
			titleValues.AuthorsInfo = _Authors.getText().toString();
			titleValues.YearInfo = _Year.getText().toString();
			titleValues.FirstEditonYearInfo = _FirstEditionYear.getText().toString();
			titleValues.Edition = _Edition.getText().toString();
			titleValues.Publisher = _Publisher.getText().toString();
			titleValues.Topics = _Topics.getText().toString();
			
			Log.i("kyesh","Started AsyncTask");
			new addTitle().execute(titleValues);
			
		}else if(v.getId()==R.id.addByCamera){

//			FragmentManager fragmentManager = getFragmentManager();
//	        Fragment fragment = new BarcodeScanner();
//	        fragmentManager.beginTransaction()
//	        				.add(R.id.content_frame, fragment)
//	        				.commit();
			_ref.setActiveFragment(new BarcodeScanner(), R.string.title_activity_barcode_scanner, true);

		}
		
	}

}
