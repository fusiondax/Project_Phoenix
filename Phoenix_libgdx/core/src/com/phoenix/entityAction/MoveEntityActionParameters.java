package com.phoenix.entityAction;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;

/**
 * The command parameters for a 'move' entity action. See {@link MoveEntityAction} for details.
 * @author David Janelle
 *
 */
public class MoveEntityActionParameters implements EntityActionParameters
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
