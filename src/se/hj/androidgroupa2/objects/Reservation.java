package se.hj.androidgroupa2.objects;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;

    public Integer ReservationId;
    public Title ResTitle;
    public User ResUser;
    public String ReserveDate;
    public String AvailableDate;
    public boolean LoanRecalled;
    
    public static Reservation parseReservationFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	Reservation res = new Reservation();
    	
    	res.ReservationId = (Integer) json.opt("ReservationId");
    	res.ResTitle = Title.parseTitleFromJSONObject(json.optJSONObject("Title"));
    	res.ResUser = User.parseUserFromJSONObject(json.optJSONObject("User"));
    	res.ReserveDate = ApiHelper.removeTimeFromDateString(json.optString("ReserveDate"));
    	res.AvailableDate = ApiHelper.removeTimeFromDateString(json.optString("AvailableDate"));
    	res.LoanRecalled = json.optBoolean("LoanRecalled");
    	
    	return res;
    }

	public static void getCurrentUserReservations(final CallbackReference callback)
	{
		AsyncTask<Void, Void, ArrayList<Reservation>> task = new AsyncTask<Void, Void, ArrayList<Reservation>>() {
			@Override
			protected ArrayList<Reservation> doInBackground(Void... params) {

				ArrayList<Reservation> result = new ArrayList<Reservation>();
				
				JSONArray reservations;
				try {
					reservations = new JSONArray(ApiHelper.getFromApi("http://doelibs-001-site1.myasp.net/api/reservation/"));
				} catch (Exception e) {
					Log.e("JSON PARSE", "get reservations from user api");
					return null;
				}
				
				for (int i = 0; i < reservations.length(); i++)
				{
					JSONObject reservation = reservations.optJSONObject(i);
					if (reservation != null) result.add(Reservation.parseReservationFromJSONObject(reservation));
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Reservation> result) {
				callback.callbackFunction(result);
			}
		};
		task.execute();
	}

	public interface CallbackReference {
		abstract void callbackFunction(ArrayList<Reservation> reservations);
	}
}
