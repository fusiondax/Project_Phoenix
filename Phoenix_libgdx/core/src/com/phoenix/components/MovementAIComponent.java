package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.pathfinding.MovementNodeGraph;

public class MovementAIComponent implements Component, Serializable
{
	public MovementNodeGraph knownPathGraph = new MovementNodeGraph();
	public ArrayList<String> passableTerrains = new ArrayList<String>();
	
	//TODO this will be obselete once the movementGraph pathfinding is setup
	public ArrayList<Vector2> destinations = new ArrayList<Vector2>();
	
	public ArrayList<Vector2> expandingNodes = new ArrayList<Vector2>();
	
	public ArrayList<Vector2> finalNodes = new ArrayList<Vector2>();
	
	public ArrayList<Vector2> nextNodes = new ArrayList<Vector2>();
	
	public int counter = 0;
	
	public float unitMaxSpeed = 0.0f;
	
	@Override
	public void write(Json json)
	{
		//TODO we might want to save the entity's known path graph
		
	}
	
	@Override
	public void read(Json json, JsonValue jsonData)
	{
		
		
	}
}
