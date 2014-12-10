package se.hj.androidgroupa2.objects;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DoelibsNotification implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum NOTIFICATION_TYPE {

        REGISTRATION_ACCEPT_REQUEST(0),
        TITLE_RESERVATION(1),
		EXPIRED_RECALL(2),
        RECALL(3),
        LOAN_RENEWED(4),
        RENEW_EXPIRE_DATE_REQUEST(5),
        RENEW_EXPIRE_DATE_REQUEST_ALLOWED(6),
        UNKNOWN(-1);
	
	    private int numVal;
	
	    NOTIFICATION_TYPE(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	}
	
    public Integer NotificationId;
    public User Recipient;
    public User Sender; //sender can be null in case of the notification was send by the system
    public boolean Read;
    public boolean Done;
    public String Message;
    public String SendDate;
    public int Type;
    
    public static DoelibsNotification parseNotificationFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	DoelibsNotification not = new DoelibsNotification();
    	
    	not.NotificationId = (Integer) json.opt("NotificationId");
    	not.Recipient = User.parseUserFromJSONObject(json.optJSONObject("Recipient"));
    	not.Sender = User.parseUserFromJSONObject(json.optJSONObject("Sender"));
    	not.Read = json.optBoolean("Read", false);
    	not.Done = json.optBoolean("Done", false);
    	not.Message = json.optString("Message");
    	not.SendDate = ApiHelper.removeTimeFromDateString(json.optString("SendDate"));
    	not.Type = (Integer) json.opt("Type");
    	
    	return not;
    }
    
    public static void acceptUserRegistrationRequest(int userId, final CallbackReference callback)
    {
    	AsyncTask<Integer, Void, Boolean> task = new AsyncTask<Integer, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Integer... params) {
				// TODO: finish
				return null;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				callback.callbackFunction(result);
			}
		};
    }
    
    public static void deleteNotification(int notId, final CallbackReference callback)
    {
    	AsyncTask<Integer, Void, Boolean> task = new AsyncTask<Integer, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Integer... params) {

				Integer notId = params[0];
				if (notId == null) return null;
				int responseCode = ApiHelper.deleteFromApi("http://doelibs-001-site1.myasp.net/api/notification/" + notId.toString());
				return (responseCode == 200);
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				callback.callbackFunction(result);
			}
		};
		task.execute(notId);
    }
    
    public static void getNotificationsForCurrentUser(final CallbackReference callback)
    {
    	AsyncTask<Void, Void, ArrayList<DoelibsNotification>> task = new AsyncTask<Void, Void, ArrayList<DoelibsNotification>>() {
			@Override
			protected ArrayList<DoelibsNotification> doInBackground(Void... params) {
				
				ArrayList<DoelibsNotification> result = new ArrayList<DoelibsNotification>();
				
				JSONArray rawArray;
				try {
					rawArray = new JSONArray(ApiHelper.getFromApi("http://doelibs-001-site1.myasp.net/api/notification/"));
				} catch (JSONException e) {
					Log.e("JSON PARSE", "get notifications from api");
					return null;
				}
				
				for (int i = 0; i < rawArray.length(); i++)
				{
					JSONObject jsonNot = rawArray.optJSONObject(i);
					if (jsonNot != null) result.add(DoelibsNotification.parseNotificationFromJSONObject(jsonNot));
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(ArrayList<DoelibsNotification> result) {
				callback.callbackFunction(result);
			}
		};
		task.execute();
    }

	public interface CallbackReference {
		abstract void callbackFunction(Object result);
	}
}