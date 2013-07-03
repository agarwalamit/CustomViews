package com.example.customviews;

import org.json.JSONObject;

public class WidgetItem {

	JSONObject widgetJson = null;
	
	public WidgetItem(JSONObject json){
		this.widgetJson = json;
	}
	
	public JSONObject getValue(){
		return widgetJson.optJSONObject("value");
	}
	
	
	public JSONObject getAction(){
		return widgetJson.optJSONObject("action");
	}
	
}
