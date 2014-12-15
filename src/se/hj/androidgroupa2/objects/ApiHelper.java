package se.hj.androidgroupa2.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class ApiHelper {

	public static BasicHeader AuthentificationHeader;
	public static String AuthentificationCookieValue;
	public static User LoggedInUser;
	

	public static JSONTokener getFromApi(String url)
	{
		return getFromApi(url, false);
	}
	
	public static JSONTokener getFromApi(String url, boolean isLoginRequest)
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		if (AuthentificationHeader != null) get.addHeader(AuthentificationHeader);
		if (AuthentificationCookieValue != null) get.addHeader("Cookie", AuthentificationCookieValue);
		
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			
			if (isLoginRequest)
			{
				Header[] headers = response.getHeaders("Set-Cookie");
				if (headers.length > 0) AuthentificationCookieValue = headers[0].getValue();
				else AuthentificationCookieValue = null;
			}

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
	
	public static int getStatusCodeFromApi(String url)
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		if (AuthentificationHeader != null) get.addHeader(AuthentificationHeader);
		if (AuthentificationCookieValue != null) get.addHeader("Cookie", AuthentificationCookieValue);
		
		int responseCode = -1;
		try {
			HttpResponse response = client.execute(get);
			responseCode = response.getStatusLine().getStatusCode();
			
		} catch (Exception e) {
			Log.e("API", e.getMessage());
		}
		return responseCode;
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

	public static int putToApi(String url)
	{
		return ApiHelper.putToApi(url, null);
	}
	
	public static int putToApi(String url, HttpEntity entity)
	{
		int responseCode = -1;
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		if (AuthentificationHeader != null) put.addHeader(AuthentificationHeader);
		if (entity != null) put.setEntity(entity);
		
		try
		{
			HttpResponse response = client.execute(put);
			responseCode = response.getStatusLine().getStatusCode();		
		}
		catch(Exception e)
		{
			Log.e("API", "putToApi | " + e.getMessage());
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
			if (AuthentificationCookieValue != null) connection.addRequestProperty("Cookie", AuthentificationCookieValue);
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
	
	public static String AddTitle(simpleTitle titleInfo){
		String url = "http://doelibs-001-site1.myasp.net/api/title/";
		
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("Title", titleInfo.titleInfo));
		pairs.add(new BasicNameValuePair("ISBN13", titleInfo.ISBN13info));
		pairs.add(new BasicNameValuePair("ISBN10", titleInfo.ISBN10info));
		pairs.add(new BasicNameValuePair("Authors", titleInfo.AuthorsInfo));
		pairs.add(new BasicNameValuePair("FirstEditionYear", titleInfo.FirstEditonYearInfo));
		pairs.add(new BasicNameValuePair("Edition", titleInfo.Edition));
		pairs.add(new BasicNameValuePair("PublicationYear", titleInfo.YearInfo));
		pairs.add(new BasicNameValuePair("Publisher", titleInfo.Publisher));
		pairs.add(new BasicNameValuePair("Topics", titleInfo.Topics));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.i("kyesh","",e);
			e.printStackTrace();
		}
		
		if (AuthentificationHeader != null) post.addHeader(AuthentificationHeader);
		
		HttpClient client = new DefaultHttpClient();
		
		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();

			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			
			String line;
			while ((line = reader.readLine()) != null)
				builder.append(line).append("\n");
			
			String allLines = builder.toString();
			
			Log.i("kyesh",allLines);
			//if (allLines.isEmpty()) return null;
			return allLines;
			//JSONTokener tokener = new JSONTokener(allLines);
			//return tokener;
			
		} catch (Exception e) {
			Log.e("kyesh", e.getMessage());
			//return null;
		}
		
		return null;
	}
}
