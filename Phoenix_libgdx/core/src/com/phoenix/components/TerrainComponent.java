package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.trigger.TriggerCondition;
import com.badlogic.gdx.utils.JsonValue;

public class TerrainComponent implements Component, Serializable
{
	public ArrayList<Vector2> vertices = new ArrayList<Vector2>();
	/**
	 * lower value means the terrain will be rendered first, so below higher numbers
	 */
	public int heightPriority = 0;
	public String type = "";

	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{	
		JsonValue list = jsonData.get("vertices");
		
		for (JsonValue entry = list.child; entry != null; entry = entry.next)
		{
			Array a = json.readValue(null, entry);
			Vector2 vertex = new Vector2((float)a.get(0), (float)a.get(1));
			vertices.add(vertex);
		}
		heightPriority = jsonData.get("height_priority").asInt();
	}
}
