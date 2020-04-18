package com.phoenix.pathfinding;

import com.badlogic.gdx.math.Vector2;

public class MovementNode
{
	Vector2 position;
	int index;
	String name;
	
	public MovementNode(Vector2 position)
	{
		this(position, "node" + position.x + position.y);
	}
	
	public MovementNode(Vector2 position, String name)
	{
		super();
		this.position = position;
		this.name = name;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}
	
	
	
	

	
	
}
