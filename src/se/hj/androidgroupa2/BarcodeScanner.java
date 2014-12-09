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
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanner extends Fragment implements OnClickListener{

	 private class SearchBarcode extends AsyncTask<String, Integer, String> {
	     protected String doInBackground(String... ISBN) {
	    	 //For Testing Purposes
	    	 ISBN[0]="9781118235959";
	    	 //
	    	 Log.i("kyesh", "doInBackground Running:"+ISBN[0]);
	    	 String APIURL = "http://doelibs-001-site1.myasp.net/api/search/?searchTerm=" + ISBN[0] + "&searchOption=ISBN-13";
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
	    		 JSONArray searchResult = resultObject.getJSONArray("Titles");
	    		 if(searchResult.length()<1){
	    			 // google API and Add Title page?
	    			 Log.i("kyesh", "less than 1 title");
	    		 }else if(searchResult.length()>1){
	    			//Maybe provide a choice or something
	    			 Log.i("kyesh", "more than 1 title");
	    		 }else{
	    			 Log.i("kyesh", "In Else");
	    			 JSONObject titleObject = searchResult.getJSONObject(0).getJSONObject("Title");
	    			 TitleId = titleObject.getString("TitleId");
	    			 Log.i("kyesh", "TitleId:"+TitleId);
	    			 FragmentManager fragmentManager = getFragmentManager();
	    		        Fragment fragment = new TitlePageFragment();
	    		        Bundle args = new Bundle();
	    		        args.putString("TitleId", TitleId);
	    		        fragment.setArguments(args);
	    		        fragmentManager.beginTransaction()
	    		        				.replace(R.id.content_frame, fragment)
	    		        				.commit();
	    		        
	    		        Log.i("kyesh", "Fragment should have launched");
	    		 }
	    		}
	    		catch (Exception e) {
	    		//no result
	    			Log.i("kyesh", "Exeception",e);
	    			e.printStackTrace();
	    		}
	     }
	 }
	
	private Button scanBtn;
	private TextView formatTxt, contentTxt;

	
	
	/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.activity_barcode_scanner, container, false);

		scanBtn = (Button)rootView.findViewById(R.id.scan_button);
		formatTxt = (TextView)rootView.findViewById(R.id.scan_format);
		contentTxt = (TextView)rootView.findViewById(R.id.scan_content);
		
		scanBtn.setOnClickListener(this);
		
		
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
		
		return rootView;
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.scan_button){
			//scan
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
			}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		Log.i("kyesh","onActivityResult");
		if (scanningResult.getContents() != null) {
			//we have a result
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			
			//formatTxt.setText("FORMAT: " + scanFormat);
			//contentTxt.setText("CONTENT: " + scanContent);
			Log.i("kyesh", "Sucessful Scan");
			if(scanFormat.equalsIgnoreCase("EAN_13")){
				//Search by ISBN 13
				Log.i("kyesh", "Valid Barcode:"+scanContent);
				new SearchBarcode().execute(scanContent);
			}
			//Does not scan ISBN 10 digit barcode
			/*else if(scanFormat=="EAN_10"){
				//Searh by ISBN 10
			}*/
			//super.onActivityResult(requestCode, resultCode, intent);
			/*Toast toast = Toast.makeText(getActivity().getApplicationContext(), 
		    		"Not ISBN 13!", Toast.LENGTH_SHORT);
		    toast.show();*/
		}else{
			//super.onActivityResult(requestCode, resultCode, intent);
		    Toast toast = Toast.makeText(getActivity().getApplicationContext(), 
		    		"No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		    getFragmentManager().popBackStack();
		}
		//super.onActivityResult(requestCode, resultCode, intent);
		
		
		
	}
}
