package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class NameComponent implements Component, Serializable
{
	public String name = "";

	@Override
	public void write(Json json)
	{
		json.writeValue("name", name);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		name = jsonData.get("name").asString();
	}
	
}
