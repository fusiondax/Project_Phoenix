package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class CollectibleBlueprintComponent implements Component, Serializable
{
	public String buildableEntityName = "";
	public int amount = 0;
	public Entity collector;
	
	@Override
	public void write(Json json)
	{
		json.writeValue("buildableEntityName", buildableEntityName);
		json.writeValue("amount", amount);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		buildableEntityName = jsonData.get("buildableEntityName").asString();
		amount = jsonData.get("amount").asInt();
	}

}
