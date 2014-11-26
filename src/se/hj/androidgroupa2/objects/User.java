package se.hj.androidgroupa2.objects;

import org.json.JSONObject;

public class User {

    public Integer UserId;
    public UserCategory Category;
    public String EMail;
    public String FirstName;
    public String LastName;

    public String Room;
    public String UniPhone;
	public String RegistrationDate;
	public String UserExpirationDate;
	public boolean IsPendingExpiration;
	
	public static User parseUserFromJSONObject(JSONObject json)
	{
		User user = new User();
		
		user.UserId = (Integer)json.opt("UserId");
		user.EMail = json.optString("EMail");
		user.FirstName = json.optString("FirstName");
		user.LastName = json.optString("LastName");
		user.Room = json.optString("Room");
		user.UniPhone = json.optString("UniPhone");
		user.RegistrationDate = json.optString("RegistrationDate");
		user.UserExpirationDate = json.optString("UserExpirationDate");
		user.IsPendingExpiration = json.optBoolean("IsPendingExpiration", false);
		user.Category = UserCategory.parseCategoryFromJSONObject(json.optJSONObject("Category"));
		
		return user;
	}
}
