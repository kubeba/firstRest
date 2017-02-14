package com.example;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.MyConstants;

public class Helpers {
	
	public static JSONObject getBestResponse(JSONArray jsonResponses) {
		JSONObject jsonFinal = new JSONObject();

		for (Object json : jsonResponses) {
			if (json instanceof JSONObject) {
				Iterator<?> keys = ((JSONObject) json).keys();

				while (keys.hasNext()) {
					String key = (String) keys.next();
					if (jsonFinal.has(key) == false) {
						jsonFinal.put(key, ((JSONObject) json).get(key));
					} else {
						if (jsonFinal.get(MyConstants.getHttpCode()).equals(
								((JSONObject) json).get(MyConstants
										.getHttpCode()))) {
							break;
						} else {
							if ((int) jsonFinal.get(MyConstants.getHttpCode()) < (int) (((JSONObject) json)
									.get(MyConstants.getHttpCode()))) {
								jsonFinal
										.put(key, ((JSONObject) json).get(key));
							} else {
								break;
							}
						}
					}
				}
			}

		}
		return jsonFinal;
	}

}
