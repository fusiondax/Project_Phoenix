package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.resource.Resource;
import com.badlogic.gdx.utils.JsonValue;

public class ResourceComponent implements Component, Serializable
{
	public Resource resource;

	@Override
	public void write(Json json)
	{
		json.writeValue("resource", resource);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		// TODO 1 shouldn't the Resource be able to build itself since it is serializable and defines a "read" function ?
		
		resource = new Resource();
		resource.type = jsonData.get("resource").get("type").asString();
		resource.amount = jsonData.get("resource").get("amount").asInt();
	}

}
