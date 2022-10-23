package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.entityAction.EntityAction;

public class EntityActionsComponent implements Component, Serializable
{
	public ArrayList<EntityAction> actions = new ArrayList<EntityAction>();
	
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
			EntityAction act = json.readValue(null, entry);
			actions.add(act);
		}
	}
}
