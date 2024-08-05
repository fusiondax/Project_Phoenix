package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class ValidTerrainTypesComponent implements Component, Serializable
{
	public ArrayList<String> types = new ArrayList<String>();
			
	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.types = new ArrayList<String>();
		
		JsonValue list = jsonData.get("types");
		
		for (JsonValue entry = list.child; entry != null; entry = entry.next)
		{
			this.types.add(entry.asString());
		}
	}
}
