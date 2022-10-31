package com.phoenix.entityAction;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.resource.Resource;

/**
 * 
 * @author David Janelle
 *
 */
public class CarryResourceEntityAction extends EntityAction
{
	public int maxCapacity;
	
	public CarryResourceEntityAction()
	{
		
	}
	
	public CarryResourceEntityAction(int maxCap)
	{
		this.maxCapacity = maxCap;
	}
	
	@Override
	public int validate()
	{
		return 0;
	}

	@Override
	public boolean isErrorCodeExecuteSafe(int errCode)
	{
		boolean safe = false;
		switch(errCode)
		{
			case 0:
			{
				safe = true;
				break;
			}
		}
		return safe;
	}

	@Override
	protected void execute(Engine engine, Entity entity, float deltaTime)
	{
		
	}
	
	@Override
	public boolean isGoalReached()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stopAction(Entity entity)
	{
		
	}

	@Override
	public void write(Json json)
	{
	}
	
	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.maxCapacity = jsonData.get("max_capacity").asInt();
	}
	
	@Override
	protected Class getCommandParametersClass()
	{
		return CarryResourceEntityActionParameters.class;
	}
}
