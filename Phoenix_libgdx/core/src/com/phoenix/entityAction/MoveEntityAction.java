package com.phoenix.entityAction;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * 
 * @author David Janelle
 *
 */
public class MoveEntityAction extends EntityAction
{
	public float unitMaxSpeed;
	
	private MoveEntityActionParameters commandParams;
	
	public MoveEntityAction()
	{
		
	}

	public MoveEntityAction(float ums)
	{
		this.unitMaxSpeed = ums;
	}
	
	public void setCommandParameters(MoveEntityActionParameters params)
	{
		
	}
	
	public int validate()
	{
		return 0;
	}
	
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
		this.unitMaxSpeed = jsonData.get("unit_max_speed").asFloat();
	}
	
	/**
	 * The command parameters for a 'move' entity action. See {@link MoveEntityAction} for details.
	 * @author David Janelle
	 *
	 */
	public static class MoveEntityActionParameters
	{
		/**
		 * Required. The game world coordinate that the moving entity must attempt to reach
		 */
		public Vector2 targetDestination;
		
		public MoveEntityActionParameters(Vector2 tDest)
		{
			this.targetDestination = tDest;
		}
	}
}
