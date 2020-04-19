package com.phoenix.pathfinding;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.GraphicComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.game.Phoenix;


public class MovementNodeGraph implements IndexedGraph<MovementNode>
{
	MovementNodeHeuristic heurustic = new MovementNodeHeuristic();
	Array<MovementNode> nodes = new Array<MovementNode>();
	Array<MovementNodeConnection> paths = new Array<MovementNodeConnection>();
	
	private int lastNodeIndex = 0;
	
	ObjectMap<MovementNode, Array<Connection<MovementNode>>> pathsMap = new ObjectMap<>();
	
	public MovementNodeGraph()
	{
		
		
		
	}
	
	public void addNode(MovementNode node)
	{
		node.index = lastNodeIndex;
		lastNodeIndex++;
		
		nodes.add(node);
	}
	
	public void connectNodes(MovementNode fromNode, MovementNode toNode)
	{
		MovementNodeConnection path = new MovementNodeConnection(fromNode, toNode);
		
		if(pathsMap.containsKey(fromNode))
		{
			pathsMap.put(fromNode, new Array<Connection<MovementNode>>());
		}
		pathsMap.get(fromNode).add(path);
		paths.add(path);
	}
	
	public GraphPath<MovementNode> findGraphPath(MovementNode startNode, MovementNode goalNode)
	{
		GraphPath<MovementNode> nodePath = new DefaultGraphPath<>();
		new IndexedAStarPathFinder<>(this).searchNodePath(startNode, goalNode, heurustic, nodePath);
		return nodePath;
	}
	
	public MovementNodeSmoothGraphPath getSmoothedPath(GraphPath<MovementNode> roughPath)
	{
		MovementNodeSmoothGraphPath smoothPath = new MovementNodeSmoothGraphPath(roughPath);
		
		PathSmoother<MovementNode, Vector2> smoother = new PathSmoother<>(new MovementNodeRaycastCollisionDetector());
		
		smoother.smoothPath(smoothPath);
		
		return smoothPath;
	}
	
	@Override
	public int getIndex(MovementNode node)
	{
		return node.index;
	}
	
	@Override
	public int getNodeCount()
	{
		return lastNodeIndex;
	}
	
	@Override
	public Array<Connection<MovementNode>> getConnections(MovementNode fromNode)
	{
		Array<Connection<MovementNode>> connections = new Array<>(0);
		if(pathsMap.containsKey(fromNode))
		{
			connections = pathsMap.get(fromNode);
		}
		return connections;
	}
}
