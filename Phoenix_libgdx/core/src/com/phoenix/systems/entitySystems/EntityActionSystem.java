package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.phoenix.components.EntityActionsComponent;
import com.phoenix.entityAction.EntityAction;
import com.phoenix.entityAction.EntityAction.EntityActionGenericReturnCodes;

public class EntityActionSystem extends IteratingSystem
{
	private ComponentMapper<EntityActionsComponent> eam = ComponentMapper.getFor(EntityActionsComponent.class);
	
	public EntityActionSystem()
	{
		super(Family.all(EntityActionsComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		EntityActionsComponent eac = eam.get(entity);
		
		for(EntityAction ea : eac.actions.values())
		{
			if(ea.getCommandParameters() != null)
			{
				// attempts to execute the action
				EntityActionGenericReturnCodes errCode = ea.attemptExecute(getEngine(), entity, deltaTime);
				
				// determine if there was an error and display it
				if(!ea.isErrorCodeExecuteSafe(errCode))
				{
					// display ea.getCommandErrorMessage(errCode);
					System.out.println("something went wrong when issuing command... ");
					System.out.println("commanding player: " + "[insert player name here]");
					System.out.println("commanded entity: " + entity.toString());
					System.out.println("command type: " + ea.toString());
					System.out.println("command error code: " + errCode.getMessage());
					// the game crashes when issuing this syso because in most cases, the stop command makes this null
					//System.out.println("command parameters: " + ea.getCommandParameters().toString());
				}
			}
		}
	}
}
