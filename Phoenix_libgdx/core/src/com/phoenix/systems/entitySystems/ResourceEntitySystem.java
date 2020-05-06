package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.phoenix.components.ResourceComponent;

public class ResourceEntitySystem extends IteratingSystem
{
	private ComponentMapper<ResourceComponent> rm = ComponentMapper.getFor(ResourceComponent.class);
	
	public ResourceEntitySystem()
	{
		super(Family.all(ResourceComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		ResourceComponent rc = rm.get(entity);
		
		if(rc.resource.amount <= 0)
		{
			getEngine().removeEntity(entity);
		}
	}
}
