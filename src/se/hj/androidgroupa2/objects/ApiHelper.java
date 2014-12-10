package se.hj.androidgroupa2.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class ApiHelper {

	public static BasicHeader AuthentificationHeader;
	public static User LoggedInUser;
	
	public static JSONTokener getFromApi(String url)
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		if (AuthentificationHeader != null) get.addHeader(AuthentificationHeader);
		
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();

			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			
			String line;
			while ((line = reader.readLine()) != null)
				builder.append(line).append("\n");
			
			String allLines = builder.toString();
			if (allLines.isEmpty()) return null;
				
			JSONTokener tokener = new JSONTokener(allLines);
			return tokener;
			
		} catch (Exception e) {
			Log.e("API", e.getMessage());
			return null;
		}
	}
	
	public static int postToApi(String url)
	{
		return ApiHelper.postToApi(url, null);
	}
	
	public static int postToApi(String url, HttpEntity entity)
	{
		int responseCode = -1;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		if (AuthentificationHeader != null) post.addHeader(AuthentificationHeader);
		if (entity != null) post.setEntity(entity);
		
		try
		{
			HttpResponse response = client.execute(post);
			responseCode = response.getStatusLine().getStatusCode();		
		}
		catch(Exception e)
		{
			Log.e("API", "postToApi | " + e.getMessage());
		}
		return responseCode;
	}
	
	public static int deleteFromApi(String url)
	{
		int responseCode = -1;
		try {
			URL urlObj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
			connection.setRequestMethod("DELETE");
			if (AuthentificationHeader != null)
				connection.addRequestProperty(AuthentificationHeader.getName(), AuthentificationHeader.getValue());
			connection.connect();
			responseCode = connection.getResponseCode();
			
		} catch (Exception e) {
			Log.e("API", "deleteFromApi | " + e.getMessage());
		}
		return responseCode;
	}
	
	public static String removeTimeFromDateString(String date)
	{
		if (date == null || date.isEmpty() || date == "null") return null;
		String returnDate;
		try {
			returnDate = date.substring(0, date.indexOf("T"));
		} catch (Exception e) {
			Log.e("DATE PARSE", "in ApiHelper removing time from date");
			return null;
		}
		return returnDate;
	}
	
	public static Calendar parseDateFromString(String date)
	{
		
		if (date == null || date.isEmpty()) return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'");
		Calendar returnDate = Calendar.getInstance();
		try {
			returnDate.setTime(format.parse(date));
		} catch (Exception e) {
			Log.e("DATE PARSING", "in ApiHelper");
			return null;
		}
		return returnDate;
	}
}
