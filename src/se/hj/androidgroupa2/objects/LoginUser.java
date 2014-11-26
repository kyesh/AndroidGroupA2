package se.hj.androidgroupa2.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Base64;

public class LoginUser extends AsyncTask<String, Void, User> {

	private static CallbackReference _callback;
	
	public static void LogIn(String username, String password, CallbackReference callback)
	{
		_callback = callback;
		new LoginUser().execute(username, password);
	}

	@Override
	protected User doInBackground(String... params) {

		String username = params[0];
		String password = params[1];
		
		//connect
	
		String connection = "http://doelibs-001-site1.myasp.net/api/User";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(connection);
		get.addHeader("Authorization", "Basic " + 
				Base64.encodeToString((username + ":" + password).getBytes(),
				Base64.NO_PADDING | Base64.NO_WRAP));
		
		try {
			//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//nameValuePairs.add(new BasicNameValuePair("Authorization", "Cookie " + Base64.encode(username.getBytes(), Base64.DEFAULT)));
			//nameValuePairs.add(new BasicNameValuePair("Password", password));
			//post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();

			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			
			String line;
			while ((line = reader.readLine()) != null)
				builder.append(line).append("\n");
			
			JSONTokener tokener = new JSONTokener(builder.toString());
			JSONObject result = new JSONObject(tokener);
			//String test = result.toString();

			return User.parseUserFromJSONObject(result);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(User user) {
		_callback.callbackFunction(user);
	}
	
	public interface CallbackReference {
		void callbackFunction(User user);
	}
}
