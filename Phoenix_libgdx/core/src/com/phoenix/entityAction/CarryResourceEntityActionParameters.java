package com.phoenix.entityAction;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class CarryResourceEntityActionParameters implements EntityActionParameters
{
	/**
	 * Entity with the resource component that was selected by the player
	 */
	public Entity targetResourceEntity;
	
	/**
	 * Location selected by player
	 */
	public Vector2 targetLocation;
	
	
	public int desiredAmount;
	
	public CarryResourceEntityActionParameters(Entity targetResourceEntity, Vector2 targetLocation)
	{
		this(targetResourceEntity, targetLocation, -1);
	}
	
	public CarryResourceEntityActionParameters(Entity targetResourceEntity, Vector2 targetLocation, int desiredAmount)
	{
		this.targetResourceEntity = targetResourceEntity;
		this.targetLocation = targetLocation;
		this.desiredAmount = desiredAmount;
	}
}
