package se.hj.androidgroupa2.objects;

import java.io.Serializable;

import org.json.JSONObject;

public class Topic implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Integer TopicId;
	public String TopicName;
	
	public static Topic parseTopicFromJSONObject(JSONObject json)
	{
		if (json == null) return null;
		Topic topic = new Topic();
		
		topic.TopicId = (Integer) json.opt("TopicId");
		topic.TopicName = json.optString("TopicName");
		
		return topic;
	}
}
