package com.phoenix.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class MovementNodeHeuristic implements Heuristic<MovementNode>
{
	@Override
	public float estimate(MovementNode node, MovementNode endNode)
	{
		return Vector2.dst(node.position.x, node.position.y, endNode.position.x, endNode.position.y);
	}
}
