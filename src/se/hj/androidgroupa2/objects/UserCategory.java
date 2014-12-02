package se.hj.androidgroupa2.objects;

import java.io.Serializable;

import org.json.JSONObject;

public class UserCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
