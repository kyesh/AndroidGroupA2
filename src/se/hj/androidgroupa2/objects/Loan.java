package se.hj.androidgroupa2.objects;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Loan implements Serializable {

	private static final long serialVersionUID = 1L;
	
    public Integer LoanId;
    public Loanable LoanLoanable;
    public User Borrower;
    public String BorrowDate;
    public String ReturnDate;
    public String ToBeReturnedDate;
	public String RecallExpiredDate; // CAn be null
	
	public static Loan parseLoanFromJSONObject(JSONObject json)
	{
		if (json == null) return null;
		Loan loan = new Loan();
		
		loan.LoanId = (Integer) json.opt("LoanId");
		loan.LoanLoanable = Loanable.parseLoanableFromJSONObject(json.optJSONObject("Loanable"));
		loan.Borrower = User.parseUserFromJSONObject(json.optJSONObject("Borrower"));
		loan.BorrowDate = ApiHelper.removeTimeFromDateString(json.optString("BorrowDate"));
		loan.ReturnDate = ApiHelper.removeTimeFromDateString(json.optString("ReturnDate"));
		loan.ToBeReturnedDate = ApiHelper.removeTimeFromDateString(json.optString("ToBeReturnedDate"));
		loan.RecallExpiredDate = ApiHelper.removeTimeFromDateString(json.optString("RecallExpiredDate"));
		
		return loan;
	}
	
	public static void getCurrentUserLoans(final CallbackReference callback)
	{
		AsyncTask<Void, Void, ArrayList<Loan>> task = new AsyncTask<Void, Void, ArrayList<Loan>>() {
			@Override
			protected ArrayList<Loan> doInBackground(Void... params) {

				ArrayList<Loan> result = new ArrayList<Loan>();
				
				JSONArray loans;
				try {
					loans = new JSONArray(ApiHelper.getFromApi("http://doelibs-001-site1.myasp.net/api/loan/"));
				} catch (JSONException e) {
					Log.e("JSON PARSE", "get loans from user api");
					return null;
				}
				
				for (int i = 0; i < loans.length(); i++)
				{
					JSONObject loan = loans.optJSONObject(i);
					if (loan != null) result.add(Loan.parseLoanFromJSONObject(loan));
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(ArrayList<Loan> result) {
				callback.callbackFunction(result);
			}
		};
		task.execute();
	}
	
	public static void checkInLoan(int loanId, final CallbackReference callback)
	{
		AsyncTask<Integer, Void, Boolean> task = new AsyncTask<Integer, Void, Boolean>() {
			
			@Override
			protected Boolean doInBackground(Integer... params) {
				
				int loanId = params[0];
				if (ApiHelper.deleteFromApi("http://doelibs-001-site1.myasp.net/api/loan/" + Integer.toString(loanId)) == 200)
					return true;
				else
					return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				callback.callbackFunction(result);
			}
		};
		task.execute(loanId);
	}

	public interface CallbackReference {
		abstract void callbackFunction(Object result);
	}
}
