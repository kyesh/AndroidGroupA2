package se.hj.androidgroupa2.objects;

import java.io.Serializable;

import org.json.JSONObject;

public class Publisher implements Serializable {

	private static final long serialVersionUID = 1L;
	
    public Integer PublisherId;
    public String Name;
    
    public static Publisher parsePublisherFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	Publisher pub = new Publisher();
    	
    	pub.PublisherId = (Integer)json.opt("PublisherId");
    	pub.Name = json.optString("Name");
    	
    	return pub;
    }
}
