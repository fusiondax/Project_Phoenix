package com.phoenix.entityAction;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.entityAction.CarryResourceEntityAction.CarryResourceEntityActionParameters;

public class DisassembleEntityAction extends EntityAction
{
	/**
	 * the time it takes for a given entity to disassemble itself in game ticks
	 * (amount of logic update, roughly corresponds to 60 ticks per seconds).
	 */
	public int disassemblyTime;

	private DisassembleEntityActionParameters commandParams;
	
	public DisassembleEntityAction()
	{
		
	}
	
	public DisassembleEntityAction(int dt)
	{
		this.disassemblyTime = dt;
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
		this.disassemblyTime = jsonData.get("disassembly_time").asInt();
	}

	public static class DisassembleEntityActionParameters
	{
		// TODO 3 param value added as example, might be removed later
		public boolean emergencyDisassembly;

		public DisassembleEntityActionParameters(boolean emergencyDisassembly)
		{
			this.emergencyDisassembly = emergencyDisassembly;
		}
	}
}
