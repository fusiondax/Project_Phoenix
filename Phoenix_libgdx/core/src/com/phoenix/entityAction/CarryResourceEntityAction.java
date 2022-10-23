package com.phoenix.entityAction;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.entityAction.MoveEntityAction.MoveEntityActionParameters;
import com.phoenix.resource.Resource;

public class CarryResourceEntityAction extends EntityAction
{
	public int maxCapacity;
	
	private CarryResourceEntityActionParameters commandParams;
	
	public CarryResourceEntityAction()
	{
		
	}
	
	public CarryResourceEntityAction(int maxCap)
	{
		this.maxCapacity = maxCap;
	}
	
	public void setCommandParameters(CarryResourceEntityActionParameters params)
	{
		
	}
	
	@Override
	public int validate()
	{
		return 0;
	}

	@Override
	protected void execute()
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
	
	public static class CarryResourceEntityActionParameters
	{
		public Resource targetResource;
		public int desiredAmount;
		
		public CarryResourceEntityActionParameters(Resource targetResource, int desiredAmount)
		{
			this.targetResource = targetResource;
			this.desiredAmount = desiredAmount;
		}
	}
}
