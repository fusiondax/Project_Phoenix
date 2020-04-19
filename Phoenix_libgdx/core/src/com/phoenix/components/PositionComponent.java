package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class PositionComponent implements Component, Serializable
{
	public final Vector3 pos = new Vector3();
	public final Vector2 scale = new Vector2(1.0f, 1.0f);
	public float rotation = 0.0f;
	
	@Override
	public void write(Json json)
	{
		json.writeValue("position_x", pos.x);
		json.writeValue("position_y", pos.y);
		
		//TODO the z value might be useful later for render priority
		//json.writeValue("position_z", pos.z);
		
		//TODO for now, scale and rotation are not used, but will be eventually
	}
	@Override
	public void read(Json json, JsonValue jsonData)
	{
		pos.set(new Vector3(jsonData.get("position_x").asFloat(),
				jsonData.get("position_y").asFloat(),
				/*jsonData.get("position_z").asFloat()*/0.0f));
		
		//TODO eventually, set scale and rotation attributes
		
	}
}
