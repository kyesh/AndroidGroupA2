package se.hj.androidgroupa2.objects;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

public class Title implements Serializable {

	private static final long serialVersionUID = 1L;
	
    public Integer TitleId;
    public String BookTitle;
    public String ISBN10;
    public String ISBN13;
    public Integer EditionNumber;
    public Integer EditionYear;
    public Publisher Publisher;
    
    public static Title parseTitleFromJSONObject(JSONObject json)
    {
    	if (json == null) return null;
    	Title title = new Title();
    	
    	// TODO: Fix Integers can be null OAOAOAOAOAO. Error while parsing.
    	title.TitleId = (Integer)json.opt("TitleId");
    	title.EditionNumber = (Integer)json.opt("EditionNumber");
    	title.EditionYear = (Integer)json.opt("EditionYear");
		title.BookTitle = json.optString("BookTitle");
		title.ISBN10 = json.optString("ISBN10");
		title.ISBN13 = json.optString("ISBN13");
		title.Publisher = se.hj.androidgroupa2.objects.Publisher.parsePublisherFromJSONObject(
				json.optJSONObject("Publisher"));
		
		//Converts ISBN10 to ISBN13
		if(title.ISBN13 == "" || title.ISBN13 == "null" && title.ISBN10.length() == 10)
		{
			String oldISBN = title.ISBN10;
			oldISBN = oldISBN.substring(0,oldISBN.length()-1);
			String newISBN = "";
			int lastDigit=0;
			int sum=0;	
			int multiplier = 1;
			String code = "978" + oldISBN;
			
			for(int i=0;i <= code.length()-1; i++){
				int num = Character.getNumericValue(code.charAt(i));
				sum = sum + (num * multiplier);

	            if(multiplier == 1)
	            	multiplier = 3;
	            else
	            	multiplier = 1;
			}

			sum = sum % 10;
			if(sum == 0)
				lastDigit = 0;
			else
				lastDigit = 10-sum;
			
			newISBN = code + lastDigit;
			title.ISBN13 = newISBN;
		}
		else
			title.ISBN13 = "Not Avaiable";
		
		return title;
    }
}
