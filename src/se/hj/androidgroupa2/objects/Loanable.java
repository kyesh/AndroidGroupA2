package se.hj.androidgroupa2.objects;

import java.io.Serializable;

import org.json.JSONObject;

import se.hj.androidgroupa2.objects.Loan.CallbackReference;
import android.os.AsyncTask;

public class Loanable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Integer LoanableId;
    public String Barcode;
    public String Category;
    public User Owner;
    public String Location;
    public Title TitleInformation;
    public STATUS Status;

	public enum STATUS {
		AVAILABLE(0),
		BORROWED(1),
		RESERVED(2),
		UNAVAILABLE_FROM_OWNER(3),
		RECALLED(4),
		DELETED(5),
		UNKNOWN(-1);

	    private int numVal;

	    STATUS(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	    
	    public static STATUS getStatus(int i) {
	    	if (i == 0) return AVAILABLE;
	    	if (i == 1) return BORROWED;
	    	if (i == 2) return RESERVED;
	    	if (i == 3) return UNAVAILABLE_FROM_OWNER;
	    	if (i == 4) return RECALLED;
	    	if (i == 5) return DELETED;
	    	else return UNKNOWN;
	    }
	}
    
    public static Loanable parseLoanableFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	Loanable loanable = new Loanable();
    	
    	loanable.LoanableId = (Integer) json.opt("LoanableId");
    	loanable.Barcode = json.optString("Barcode");
    	JSONObject category = json.optJSONObject("Category");
    	loanable.Category = category.optString("Name");
    	loanable.Owner = User.parseUserFromJSONObject(json.optJSONObject("Owner"));
    	loanable.Location = json.optString("Location");
    	loanable.Status = STATUS.getStatus(json.optInt("Status", -1));
    	loanable.TitleInformation = Title.parseTitleFromJSONObject(json.optJSONObject("Title"));
    	
    	return loanable;
    }
    
    public static void checkOutLoanable(int loanableId, final CallbackReference callback)
	{
		AsyncTask<Integer, Void, Boolean> task = new AsyncTask<Integer, Void, Boolean>() {
			
			@Override
			protected Boolean doInBackground(Integer... params) {
				
				int loanableId = params[0];
				if (ApiHelper.postToApi("http://doelibs-001-site1.myasp.net/api/loanable/" + Integer.toString(loanableId)) == 201)
					return true;
				else
					return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				callback.callbackFunction(result);
			}
		};
		task.execute(loanableId);
	}

	public interface CallbackReference {
		abstract void callbackFunction(Object result);
	}
}
