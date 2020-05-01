package com.phoenix.game;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;

public class Player
{
	public String name = "";
	
	//TODO set the player's vision/fog of war
	
	public ArrayList<Entity> selectedEntities;
	
	public ArrayList<Blueprint> blueprintLibrary;
	
	public Player(String name)
	{
		this.name = name;
		this.selectedEntities = new ArrayList<Entity>();
		this.blueprintLibrary = new ArrayList<Blueprint>();
	}


	public void addBlueprintToCollection(Blueprint blueprint)
	{
		// find an exisiting blueprint that is equivalent to the given blueprint
		Blueprint existingBlueprint = null;
		for(Blueprint b : blueprintLibrary)
		{
			if(b.isEquivalent(blueprint))
			{
				existingBlueprint = b;
				break;
			}
		}
		
		if(existingBlueprint != null)
		{
			existingBlueprint.addAmount(blueprint.getAmount());
		}
		else
		{
			blueprintLibrary.add(blueprint);
		}
	}
}
