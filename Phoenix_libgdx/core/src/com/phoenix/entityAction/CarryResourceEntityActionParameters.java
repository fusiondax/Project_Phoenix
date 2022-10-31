package com.phoenix.entityAction;

import com.phoenix.resource.Resource;

public class CarryResourceEntityActionParameters implements EntityActionParameters
{
	public Resource targetResource;
	public int desiredAmount;
	
	public CarryResourceEntityActionParameters(Resource targetResource, int desiredAmount)
	{
		this.targetResource = targetResource;
		this.desiredAmount = desiredAmount;
	}
}
