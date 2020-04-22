package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class CollisionHitboxComponent implements Component, Serializable
{
	public String hitboxShape;
	public float size;
	
	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		
	}
}
