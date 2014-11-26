package se.hj.androidgroupa2.objects;

import org.json.JSONObject;

public class UserCategory {

    public Integer CategoryId;
    public String Name;
    public Integer LoanPeriod;
    
    public static UserCategory parseCategoryFromJSONObject(JSONObject json)
    {
    	UserCategory cat = new UserCategory();
    	
    	cat.CategoryId = (Integer)json.opt("CategoryId");
    	cat.LoanPeriod = (Integer)json.opt("LoanPeriod");
    	cat.Name = json.optString("Name");
    	
    	return cat;
    }
}
