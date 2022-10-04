package com.phoenix.resource;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class Resource implements Serializable
{
	public String type = "";
	public int amount = 1;
	
	public Resource()
	{
		this("", 1);
	}
	
	public Resource(String type, int amount)
	{
		this.type = type;
		this.amount = amount;
	}

	@Override
	public void write(Json json)
	{
		json.writeValue("type", type);
		json.writeValue("amount", amount);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		// TODO 1 use json.readValue
		type = jsonData.get("type").asString();
		amount = jsonData.get("amount").asInt();
	}
	
	public String toString()
	{
		return "Resource: Type: " + type + " Amount: " + amount;
	}
}
