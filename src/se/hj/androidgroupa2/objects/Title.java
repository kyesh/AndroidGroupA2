package se.hj.androidgroupa2.objects;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

public class Title implements Serializable {

	private static final long serialVersionUID = 1L;
	
    public Integer TitleId;
    public String BookTitle;
    public String ISBN10;
    public String ISBN13;
    public Integer EditionNumber;
    public Integer EditionYear;
	public Integer FirstEditionYear;
    public Publisher Publisher;
    
    public static Title parseTitleFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	Title title = new Title();
    	
    	title.TitleId = (Integer)json.opt("TitleId");
    	title.EditionNumber = (Integer)json.opt("EditionNumber");
    	title.EditionYear = (Integer)json.opt("EditionYear");
    	title.FirstEditionYear = (Integer)json.opt("FirstEditionYear");
		title.BookTitle = json.optString("BookTitle");
		title.ISBN10 = json.optString("ISBN10");
		title.ISBN13 = json.optString("ISBN13");
		title.Publisher = se.hj.androidgroupa2.objects.Publisher.parsePublisherFromJSONObject(
				json.optJSONObject("Publisher"));
		
		return title;
    }
    
    public static List<Title> getTitlesFromSearch()
    {
    	// TODO: Fix this function.
    	return null;
    }
}
