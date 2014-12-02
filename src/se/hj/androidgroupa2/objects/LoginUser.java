package se.hj.androidgroupa2.objects;

import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;

public class LoginUser extends AsyncTask<String, Void, User> {

	private static CallbackReference _callback;
	
	public static void logIn(String username, String password, CallbackReference callback)
	{
		_callback = callback;
		new LoginUser().execute(username, password);
	}

	@Override
	protected User doInBackground(String... params) {

		String username = params[0];
		String password = params[1];
		
		ApiHelper.AuthentificationHeader = new BasicHeader("Authorization", "Basic " + 
				Base64.encodeToString((username + ":" + password).getBytes(),
				Base64.NO_WRAP));
		JSONObject result = ApiHelper.getFromApi("http://doelibs-001-site1.myasp.net/api/User");
		User loggedInUser = User.parseUserFromJSONObject(result);
		if (loggedInUser != null)
		{
			ApiHelper.LoggedInUser = loggedInUser;
			return loggedInUser;
		}
		else
		{
			ApiHelper.AuthentificationHeader = null;
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(User user) {
		_callback.callbackFunction(user);
	}
	
	public interface CallbackReference {
		abstract void callbackFunction(User user);
	}
}
