package com.phoenix.trigger;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.components.NameComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.utility.GameWorldUtility;

public class UnitAtPositionCondition implements TriggerCondition
{
	public UnitAtPositionCondition()
	{
		this("any", "any", new Vector2(), 1.0f);
	}
	
	public UnitAtPositionCondition(String unitOwner, String unitTypeName, Vector2 targetPosition, float targetRange)
	{
		this.unitOwner = unitOwner;
		this.unitTypeName = unitTypeName;
		this.targetPosition = targetPosition;
		this.targetRadius = targetRange;
	}

	public String unitOwner;
	public String unitTypeName;
	public Vector2 targetPosition;
	public float targetRadius;

	// TODO 2 implements condition logic

	/**
	 * if a unit: 
	 * 	- of the unit type defined by the [unitTypeName] attribute 
	 * 	- is owned by the player defined by the [unitOwner] attribute 
	 * 	- is located within a radius of [targetRange] of the map position defined by the [targetPosition] attribute 
	 * the condition is then met
	 * 
	 * if unit type reads "any", then the unit type is not verified 
	 * if unit owner reads "any" then the unit's owner is not verified
	 */
	@Override
	public boolean isConditionMet(GameScreen map)
	{
		boolean conditionMet = false;
		
		// get units that are located within a circle of radius [targetRadius] centered
		// on [targetPosition]
		ArrayList<Entity> entitiesWithinTarget = GameWorldUtility.getProxyEntities(map.engine, targetPosition,
				targetRadius, Family.all(PositionComponent.class, OwnershipComponent.class).get());

		ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
		
		// filter out entities that are not of the correct unit type
		if(!unitTypeName.equals("any"))
		{
			
			for(Entity e : entitiesWithinTarget)
			{
				String unitType = e.getComponent(NameComponent.class).name;
				
				if(!unitType.equals(this.unitTypeName))
				{
					entitiesToRemove.add(e);
				}
			}
			entitiesWithinTarget.removeAll(entitiesToRemove);
			entitiesToRemove.clear();
		}
		
		// filter out entities that are not of the correct ownership
		if(!this.unitOwner.equals("any"))
		{
			for(Entity e : entitiesWithinTarget)
			{
				String uOwner = e.getComponent(OwnershipComponent.class).owner;
				
				if(!uOwner.equals(this.unitOwner))
				{
					entitiesToRemove.add(e);
				}
			}
			entitiesWithinTarget.removeAll(entitiesToRemove);
			entitiesToRemove.clear();
		}
		
		// at this point, we singled out unit that are within the target, are owned by the right owner and are of the right type
		// if there are any units left in the collection, the condition is met
		
		if(!entitiesWithinTarget.isEmpty())
		{
			conditionMet = true;
		}
		
		return conditionMet;
	}

	@Override
	public void write(Json json)
	{
		json.writeValue("unit_owner", unitOwner);
		json.writeValue("unit_type", unitTypeName);

		json.writeValue("target_position_x", targetPosition.x);
		json.writeValue("target_position_y", targetPosition.y);

		json.writeValue("target_radius", targetRadius);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.unitOwner = jsonData.get("unit_owner").asString();
		this.unitTypeName = jsonData.get("unit_type").asString();		
		
		this.targetPosition.set(new Vector2(jsonData.get("target_position_x").asFloat(),
				jsonData.get("target_position_y").asFloat()));
		
		this.targetRadius = jsonData.get("target_radius").asFloat();
	}

}
