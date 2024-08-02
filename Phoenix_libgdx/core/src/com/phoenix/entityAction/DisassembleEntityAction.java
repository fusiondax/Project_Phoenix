package com.phoenix.entityAction;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * 
 * @author David Janelle
 *
 */
public class DisassembleEntityAction extends EntityAction
{
	/**
	 * the time it takes for a given entity to disassemble itself in game ticks
	 * (amount of logic update, roughly corresponds to 60 ticks per seconds).
	 */
	public int disassemblyTime;

	public DisassembleEntityAction()
	{
		
	}
	
	public DisassembleEntityAction(int dt)
	{
		this.disassemblyTime = dt;
	}
	
	@Override
	public EntityActionGenericReturnCodes validate(Engine engine, Entity entity)
	{
		return EntityActionGenericReturnCodes.DefaultCode;
	}

	@Override
	public boolean isErrorCodeExecuteSafe(EntityActionGenericReturnCodes errCode)
	{
		boolean safe = false;
		switch(errCode)
		{
			case DefaultCode:
				break;
			case GoalReached:
				break;
			default:
				break;
		}
		return safe;
	}

	@Override
	protected void execute(Engine engine, Entity entity, EntityActionGenericReturnCodes errCode, float deltaTime)
	{
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(Json json)
	{
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.disassemblyTime = jsonData.get("disassembly_time").asInt();
	}

	@Override
	protected Class getCommandParametersClass()
	{
		return DisassembleEntityActionParameters.class;
	}
}
