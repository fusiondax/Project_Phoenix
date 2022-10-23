package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class VelocityComponent implements Component, Serializable
{
	//TODO 4 potential game exploit where saving and loading a game kills the momentum of a unit... not sure if desirable or not...
	public Vector2 velocity = new Vector2();
	
	public float friction = 0;
	
	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		
	}
}
