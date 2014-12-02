package se.hj.androidgroupa2.objects;

import java.io.Serializable;

import org.json.JSONObject;

public class Author implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Integer Aid;
    public String Name;
    public boolean isEditor;
    
    public static Author parseAuthorFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	Author author = new Author();
    	
    	author.Aid = (Integer) json.opt("Aid");
    	author.Name = json.optString("Name");
    	author.isEditor = json.optBoolean("isEditor");
    	
    	return author;
    }
}
