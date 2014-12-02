package se.hj.androidgroupa2.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class ApiHelper {

	public static Header AuthentificationHeader;
	public static User LoggedInUser;
	
	public static JSONObject getFromApi(String url)
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.addHeader(AuthentificationHeader);
		
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
			JSONObject result = new JSONObject(tokener);

			return result;
			
		} catch (Exception e) {
			Log.e("API", e.getMessage());
			return null;
		}
	}
}
