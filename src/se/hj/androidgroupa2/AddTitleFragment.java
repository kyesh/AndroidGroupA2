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

import se.hj.androidgroupa2.objects.ApiHelper;
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
	     }
	}

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private EditText _title, _ISBN10, _ISBN13, _Authors, _Year, _FirstEditionYear, _Publisher, _Topics, _Edition;
	private Button _addTitle;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment AddTitleFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddTitleFragment newInstance(String param1, String param2) {
		AddTitleFragment fragment = new AddTitleFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public AddTitleFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView =  inflater.inflate(R.layout.fragment_add_title, container, false);
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
		
		_addTitle.setOnClickListener(this);
		
		return rootView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
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
		mListener = null;
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
			
			}
		
	}

}
