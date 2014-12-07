package se.hj.androidgroupa2.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
}
