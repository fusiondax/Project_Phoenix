package com.phoenix.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.entityAction.EntityAction;

public class EntityActionsComponent implements Component, Serializable
{
	public HashMap<String, EntityAction> actions = new HashMap<String, EntityAction>();
	
	@Override
	public void write(Json json)
	{
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		JsonValue list = jsonData.get("actions");
		
		for (JsonValue entry = list.child; entry != null; entry = entry.next)
		{
			String key = entry.get("action_name").asString();
			EntityAction act = json.readValue(null, entry.get("action_obj"));
			actions.put(key, act);
		}
	}
}
