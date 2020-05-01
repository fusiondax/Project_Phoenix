package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class OwnershipComponent implements Component, Serializable
{
	public String owner = "";
	
	@Override
	public void write(Json json)
	{
		json.writeValue("owner", owner);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		owner = jsonData.get("owner").asString();
	}
	
}
