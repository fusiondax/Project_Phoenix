package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.pathfinding.MovementNodeGraph;
import com.phoenix.pathfinding.SearchNode;

public class MovementAIComponent implements Component, Serializable
{
	public static final int START_PATHFINDING_DELAY_MAX = 60;
	public MovementNodeGraph knownPathGraph = new MovementNodeGraph();
	public ArrayList<String> passableTerrains = new ArrayList<String>();
	
	public ArrayList<Vector2> destinations = new ArrayList<Vector2>();
	
	public SearchNode initialNode;
	
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
