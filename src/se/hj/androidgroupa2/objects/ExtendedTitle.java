package se.hj.androidgroupa2.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ExtendedTitle implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Author> Authors = new ArrayList<Author>();
	public List<Author> Editors = new ArrayList<Author>();
	public List<Loanable> Loanables = new ArrayList<Loanable>();
	public Title TitleInformation;
	public List<Topic> Topics = new ArrayList<Topic>();
    
	public static ExtendedTitle parseExtendedTitleFromJSONObject(JSONObject json)
	{
		if (json == null) return null;
		ExtendedTitle et = new ExtendedTitle();

		JSONArray authors = json.optJSONArray("Authors");
		for (int i = 0; authors != null && i < authors.length(); i++)
			et.Authors.add(Author.parseAuthorFromJSONObject(authors.optJSONObject(i)));
		
		JSONArray editors = json.optJSONArray("Editors");
		for (int i = 0; editors != null && i < editors.length(); i++)
			et.Editors.add(Author.parseAuthorFromJSONObject(editors.optJSONObject(i)));

		JSONArray loanables = json.optJSONArray("Loanables");
		for (int i = 0; loanables != null && i < loanables.length(); i++)
			et.Loanables.add(Loanable.parseLoanableFromJSONObject(loanables.optJSONObject(i)));

		JSONArray topics = json.optJSONArray("Topics");
		for (int i = 0; topics != null && i < topics.length(); i++)
			et.Topics.add(Topic.parseTopicFromJSONObject(topics.optJSONObject(i)));
		
		et.TitleInformation = Title.parseTitleFromJSONObject(json.optJSONObject("Title"));
		
		return et;
	}
	
    public static void getTitlesFromSearch(String query, final CallbackReference callback)
    {
    	if (query.isEmpty()) callback.callbackFunction(null);
    	
    	AsyncTask<String, Void, List<ExtendedTitle>> task = new AsyncTask<String, Void, List<ExtendedTitle>>() {
			@Override
			protected List<ExtendedTitle> doInBackground(String... params) {

				ArrayList<ExtendedTitle> returnTitles = new ArrayList<ExtendedTitle>();
				
				JSONObject raw = ApiHelper.getFromApi("http://doelibs-001-site1.myasp.net/api/search/?searchTerm=" + params[0] + "&searchOption=Title");
				JSONArray titles = raw.optJSONArray("Titles");
				
				for (int i = 0; titles != null && i < titles.length(); i++)
					returnTitles.add(ExtendedTitle.parseExtendedTitleFromJSONObject(titles.optJSONObject(i)));
				
				if (returnTitles.size() == 0) return null;
				else return returnTitles;
			}
			
			@Override
			protected void onPostExecute(List<ExtendedTitle> result) {
				callback.callbackFunction(result);
			}
		};
		task.execute(query);
    }

	public interface CallbackReference {
		abstract void callbackFunction(List<ExtendedTitle> titles);
	}
}
