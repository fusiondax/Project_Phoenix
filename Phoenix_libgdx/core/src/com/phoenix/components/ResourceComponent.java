package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.game.Resource;

public class ResourceComponent implements Component, Serializable
{
	public Resource type;

	@Override
	public void write(Json json)
	{
		
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		// TODO Auto-generated method stub
		
	}

}
