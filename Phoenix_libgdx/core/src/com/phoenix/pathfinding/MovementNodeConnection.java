package com.phoenix.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;

public class MovementNodeConnection implements Connection<MovementNode>
{
	MovementNode fromNode, toNode;
	float cost;
	
	public MovementNodeConnection(MovementNode fromNode, MovementNode toNode)
	{
		super();
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.cost = Vector2.dst(fromNode.position.x, fromNode.position.y, toNode.position.x, toNode.position.y);
	}

	@Override
	public float getCost()
	{
		//TODO cost might change as nodes move?
		return cost;
	}

	@Override
	public MovementNode getFromNode()
	{
		return fromNode;
	}

	@Override
	public MovementNode getToNode()
	{
		return toNode;
	}

}
