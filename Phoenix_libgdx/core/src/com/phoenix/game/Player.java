package com.phoenix.game;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.blueprint.Blueprint;

public class Player
{
	public String name = "";
	
	//TODO set the player's vision/fog of war
	
	private ArrayList<Blueprint> blueprintLibrary;

	public ArrayList<Entity> selectedEntities;

	public Blueprint selectedBlueprint;
	
	public Vector2 prevCamDragPos;
	public Rectangle selectionBox;
	
	public Player(String name)
	{
		this.name = name;
		this.selectedEntities = new ArrayList<Entity>();
		this.blueprintLibrary = new ArrayList<Blueprint>();
		this.selectedBlueprint = null;
		
		this.prevCamDragPos = new Vector2();
		this.selectionBox = new Rectangle();
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
	
	public void dispenseBlueprintFromCollection(Blueprint blueprint)
	{
		dispenseBlueprintFromCollection(blueprint, 1);
	}
	
	public void dispenseBlueprintFromCollection(Blueprint blueprint, int amountToRemove)
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
			existingBlueprint.subAmount(amountToRemove);
			
			if(existingBlueprint.getAmount() <= 0)
			{
				blueprintLibrary.remove(existingBlueprint);
			}
		}
		else
		{
			System.out.println("no such blueprint exists in this library");
		}
	}

	public ArrayList<Blueprint> getBlueprintLibrary()
	{
		return blueprintLibrary;
	}
}
