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
		resource = json.readValue(Resource.class, jsonData.get("resource"));
	}

}
