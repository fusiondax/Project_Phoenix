package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class MoveCommandComponent implements Component, Serializable
{
	public static final int START_PATHFINDING_DELAY_MAX = 1000;
	
	public ArrayList<Vector2> destinations = new ArrayList<Vector2>();
	public float unitMaxSpeed = 0.0f;
	public int startPathfindingDelay = START_PATHFINDING_DELAY_MAX;
	
	@Override
	public void write(Json json)
	{
		
	}
	
	@Override
	public void read(Json json, JsonValue jsonData)
	{
		
	}
}
