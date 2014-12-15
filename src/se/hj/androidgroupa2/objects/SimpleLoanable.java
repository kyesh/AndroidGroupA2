package se.hj.androidgroupa2.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class SimpleLoanable {
	
	public String Barcode;
	public String Category;
	public String Location;
	public String TitleId;
	
	public static void addLoanableToDatabase(final SimpleLoanable loanable, final DoelibsNotification.CallbackReference callback)
	{
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				

				String url = "http://doelibs-001-site1.myasp.net/api/title/" + loanable.TitleId;
				
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("Barcode", loanable.Barcode));
				pairs.add(new BasicNameValuePair("Category", loanable.Category));
				pairs.add(new BasicNameValuePair("Location", loanable.Location));
				
				int responseCode = -1;
				try {
					responseCode = ApiHelper.postToApi(url, new UrlEncodedFormEntity(pairs));
				} catch (UnsupportedEncodingException e) {
					Log.e("ERROR API POST", "Add loanable");
				}
				return (responseCode == 200 || responseCode == 201);
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				callback.callbackFunction(result);
			}
		};
		task.execute();
	}
	
	public boolean validateFields()
	{
		if (Barcode == null || Barcode.isEmpty() || Barcode == "null") return false;
		if (Category == null || Category.isEmpty() || Category == "null") return false;
		if (Location == null || Location.isEmpty() || Location == "null") return false;
		if (TitleId == null || TitleId.isEmpty() || TitleId == "null") return false;
		return true;
	}
}
