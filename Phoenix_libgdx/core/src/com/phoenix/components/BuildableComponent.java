package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.game.Resource;

public class BuildableComponent implements Component, Serializable
{
	public int buildTime;
	public int salvageTime;
	public float buildRange;
	
	public ArrayList<Resource> resourceList = new ArrayList<Resource>();
	
	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		
	}

}
