package se.hj.androidgroupa2.objects;

import java.io.Serializable;

import org.json.JSONObject;

public class UserCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public enum CATEGORY {
	    STAFF(1), STUDENT(2), UNKNOWN(0);

	    private int numVal;

	    CATEGORY(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	}
	
	public Integer CategoryId;
    public String Name;
    public Integer LoanPeriod;
    
    public static UserCategory parseCategoryFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	UserCategory cat = new UserCategory();
    	
    	cat.CategoryId = (Integer)json.opt("CategoryId");
    	cat.LoanPeriod = (Integer)json.opt("LoanPeriod");
    	cat.Name = json.optString("Name");
    	
    	return cat;
    }
}
