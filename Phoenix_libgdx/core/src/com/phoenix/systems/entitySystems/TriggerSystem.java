package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.phoenix.components.TriggerComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.trigger.TriggerAction;
import com.phoenix.trigger.TriggerCondition;

public class TriggerSystem extends IteratingSystem
{
	private ComponentMapper<TriggerComponent> tm = ComponentMapper.getFor(TriggerComponent.class);
	private GameScreen gs;

	public TriggerSystem(GameScreen gs)
	{
		super(Family.all(TriggerComponent.class).get());
		this.gs = gs;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		TriggerComponent trigger = tm.get(entity);

		boolean canExecuteActions = false;

		// depending on the 'allConditionMet' value, either all conditions must be met
		// or any one of the conditions must be met to proceed with the actions

		if (trigger.allConditionsMet)
		{
			canExecuteActions = true;
			// if any of the conditions are not met, break out of the verification and do
			// not execute any of the actions
			for (TriggerCondition condition : trigger.conditions)
			{
				if (!condition.isConditionMet(this.gs))
				{
					canExecuteActions = false;
					break;
				}
			}
		}
		else
		{
			canExecuteActions = false;
			// if any one of the conditions is met, then the verification is skipped and we
			// can proceed with the execution of the actions
			for (TriggerCondition condition : trigger.conditions)
			{
				if (condition.isConditionMet(this.gs))
				{
					canExecuteActions = true;
					break;
				}
			}
		}

		// TODO 2 implement persistent condition behaviour

		if (canExecuteActions)
		{
			for (TriggerAction action : trigger.actions)
			{
				action.execute(this.gs);
			}
			
			if(!trigger.persistant)
			{
				getEngine().removeEntity(entity);
			}
		}
	}
}
