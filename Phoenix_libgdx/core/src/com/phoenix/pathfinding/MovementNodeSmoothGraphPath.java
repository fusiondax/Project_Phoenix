package com.phoenix.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector2;

public class MovementNodeSmoothGraphPath implements SmoothableGraphPath<MovementNode, Vector2>
{
	public final ArrayList<MovementNode> nodes = new ArrayList<MovementNode>();

	public MovementNodeSmoothGraphPath(GraphPath<MovementNode> roughPath)
	{
		for(MovementNode n : roughPath)
		{
			nodes.add(n);
		}
	}
	
	@Override
	public int getCount()
	{
		return nodes.size();
	}

	@Override
	public MovementNode get(int index)
	{
		return nodes.get(index);
	}

	@Override
	public void add(MovementNode node)
	{
		nodes.add(node);
	}

	@Override
	public void clear()
	{
		nodes.clear();
	}

	@Override
	public void reverse()
	{
		ArrayList<MovementNode> tempNodes = new ArrayList<MovementNode>();
		for(int i = nodes.size() - 1; i < 0; i--)
		{
			tempNodes.add(nodes.get(i));
		}
		
		nodes.clear();
		nodes.addAll(tempNodes);
	}

	@Override
	public Iterator<MovementNode> iterator()
	{
		return nodes.iterator();
	}

	@Override
	public Vector2 getNodePosition(int index)
	{
		return nodes.get(index).position;
	}

	@Override
	public void swapNodes(int index1, int index2)
	{
		MovementNode tempNode = nodes.get(index1);
		nodes.set(index1, nodes.get(index2));
		nodes.set(index2, tempNode);
		
	}

	@Override
	public void truncatePath(int newLength)
	{
		while(nodes.size() > newLength)
		{
			nodes.remove(nodes.size() - 1);
		}
	}

}
