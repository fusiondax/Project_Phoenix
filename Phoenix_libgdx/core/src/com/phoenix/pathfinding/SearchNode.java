package com.phoenix.pathfinding;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.physics.CollisionDetector;

public class SearchNode
{
	public static final float SEARCH_RAY_MAX_LENGTH = 100.0f;
	public static final float SEARCH_RAY_MIN_LENGTH = 5.0f;
	public static final int SEARCH_NODES_MAX_GENERATION = /*5*/5;
	public static final int SEARCH_RAY_ANGLE_BETWEEN_SCATTER_NODE = /*10*/30;
	public static final int SEARCH_NODE_MINIMUM_NODE_DISTANCE = 10;
	public static final int SEARCH_NODE_INCREMENT_DISTANCE = /*5*/5;

	public Vector2 position = new Vector2();

	public ArrayList<SearchNode> childNodes = new ArrayList<SearchNode>();

	public SearchNode parentNode;

	public boolean isValidPath = false;
	
	public boolean isSearching = true;
	
	public boolean isActive = true;

	public Color nodeColor;

	public SearchNode()
	{
		this(new Vector2());
	}

	public SearchNode(float x, float y)
	{
		this(new Vector2(x, y));
	}
	
	public SearchNode(float x, float y, SearchNode parent)
	{
		this(new Vector2(x, y), parent, null);
	}

	public SearchNode(float x, float y, SearchNode parent, Color color)
	{
		this(new Vector2(x, y), parent, color);
	}

	public SearchNode(Vector2 initialVector)
	{
		this(initialVector, null, new ArrayList<SearchNode>(), null);
	}

	public SearchNode(Vector2 initialVector, SearchNode parent, Color color)
	{
		this(initialVector, parent, new ArrayList<SearchNode>(), color);
	}

	public SearchNode(Vector2 initialVector, SearchNode parent, ArrayList<SearchNode> childs, Color color)
	{
		if (initialVector != null)
		{
			this.position = initialVector;
		}

		this.parentNode = parent;

		childNodes.addAll(childs);
		
		if(color != null)
		{
			this.nodeColor = color;
		}
		else
		{
			nodeColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
		}
	}

	public int getLegacyCount()
	{
		int legacyCount = 0;
		SearchNode parent = this;

		while (parent.parentNode != null)
		{
			parent = parent.parentNode;
			legacyCount++;
		}

		return legacyCount;
	}

	public int getGenerationCount()
	{
		int genCount = 0;
		SearchNode childNode = this;

		while (!childNode.childNodes.isEmpty())
		{
			childNode = childNode.childNodes.get(0);
			genCount++;
		}
		return genCount;
	}
	
	public static SearchNode getValidPath(SearchNode nextNode)
	{
		SearchNode validPath = null;
		
		//check if current node is valid
		if(nextNode.isValidPath)
		{
			validPath = nextNode;
		}
		else
		{
			for(SearchNode node : nextNode.childNodes)
			{
				validPath = getValidPath(node);
				if(validPath != null)
				{
					break;
				}
			}
		}
		
		return validPath;
	}

	public void startRecursiveSearch(CollisionDetector detector, MovementAIComponent mac, Vector2 startPoint, Vector2 endPoint, ShapeRenderer debug)
	{
		//System.out.println("legacy count: " + getLegacyCount());
		if(isActive)
		{
			if (getLegacyCount() < SEARCH_NODES_MAX_GENERATION)
			{
				if (childNodes.isEmpty())
				{
					generateScatterNodes();
				}
				else
				{
					if (areChildSearching())
					{
						expandSearchingNodes(detector, mac, startPoint, endPoint);
					}
					else
					{
						this.sweepRedundantNodes();
						for (SearchNode childNode : childNodes)
						{
							childNode.startRecursiveSearch(detector, mac, startPoint, endPoint, debug);
						}
					}
					debugChildNode(debug);
				}
			}
		}
	}

	public void generateScatterNodes()
	{
		if (childNodes.isEmpty())
		{
			int angle = 0;
			while (angle < 360)
			{
				SearchNode searchVector = new SearchNode(0, 1, this);
				searchVector.position.setAngle(angle);
				searchVector.position.setLength(SEARCH_RAY_MIN_LENGTH);
				searchVector.position.add(position);
				childNodes.add(searchVector);
				angle += Math.random() * 5 + SEARCH_RAY_ANGLE_BETWEEN_SCATTER_NODE;
			}
		}
	}

	public void expandSearchingNodes(CollisionDetector detector, MovementAIComponent mac, Vector2 startPoint, Vector2 endPoint)
	{
		for (SearchNode node : childNodes)
		{
			Vector2 vec = node.position;

			if (node.isSearching)
			{
				if (detector.isPointCollisionRectangles(vec, CollisionDetector.getRectanglesFromTerrains(detector.getImpassableTerrains(mac)))
						|| (Vector2.dst(position.x, position.y, vec.x, vec.y) >= SEARCH_RAY_MAX_LENGTH))
				{
					node.isSearching = false;
				}
				else
				{
					vec.sub(position).setLength(vec.len() + SEARCH_NODE_INCREMENT_DISTANCE).add(position);
					
					Vector2 intersection = new Vector2();
					
					if(Intersector.intersectSegments(position, vec, startPoint, endPoint, intersection))
					{
						isValidPath = true;
					}
				}
			}
		}
	}

	public void sweepRedundantNodes()
	{
		ArrayList<SearchNode> dirtyNodes = new ArrayList<SearchNode>();
		
		// as a node, I must make sure that my childs aren't too close to eachother
		dirtyNodes.addAll(getTooCloseChildNodes());
		childNodes.removeAll(dirtyNodes);
		dirtyNodes.clear();
		
		// as a node, I must make sure that my child aren't to close to my brother's childs
		dirtyNodes.addAll(getTooCloseBrotherChildsNodes());
		childNodes.removeAll(dirtyNodes);
		dirtyNodes.clear();
		
		
		// as a node, I must make sure that my childs are farther away from my parent that I was
		dirtyNodes.addAll(getPoorPerformingChildNodes());
		childNodes.removeAll(dirtyNodes);
		dirtyNodes.clear();
		
		// if all of my nodes were swept, I am no longer useful
		if(childNodes.isEmpty())
		{
			isActive = false;
		}
	}
	
	private ArrayList<SearchNode> getTooCloseBrotherChildsNodes()
	{
		ArrayList<SearchNode> dirtyNodes = new ArrayList<SearchNode>();
		
		if(parentNode != null)
		{
			ArrayList<SearchNode> brotherNodes = new ArrayList<SearchNode>(parentNode.childNodes);
			brotherNodes.remove(this);
			
			for(SearchNode brother : brotherNodes)
			{
				ArrayList<SearchNode> brotherChildNodes = new ArrayList<SearchNode>(brother.childNodes);
				for(SearchNode child : childNodes)
				{
					for(SearchNode brotherChild : brotherChildNodes)
					{
						if (Vector2.dst(child.position.x, child.position.y, brotherChild.position.x,
								brotherChild.position.y) < SEARCH_NODE_MINIMUM_NODE_DISTANCE)
						{
							dirtyNodes.add(child);
						}
					}
				}
			}
		}
		return dirtyNodes;
	}
	
	private ArrayList<SearchNode> getPoorPerformingChildNodes()
	{
		ArrayList<SearchNode> dirtyNodes = new ArrayList<SearchNode>();
		
		if(parentNode != null)
		{
			for(SearchNode child : childNodes)
			{
				// if the distance between this node and it's parent's parent is smaller than
				// the distance between the parent and his parent
				if (Vector2.dst(child.position.x, child.position.y, parentNode.position.x,
						parentNode.position.y) <= Vector2.dst(this.position.x, this.position.y,
								parentNode.position.x, parentNode.position.y))
				{
					dirtyNodes.add(child);
				}
			}
		}
		
		return dirtyNodes;
	}
	
	private ArrayList<SearchNode> getTooCloseChildNodes()
	{
		ArrayList<SearchNode> dirtyNodes = new ArrayList<SearchNode>();
		
		ArrayList<SearchNode> otherChilds = new ArrayList<SearchNode>(childNodes);
		
		for (SearchNode child : childNodes)
		{
			for(SearchNode otherChild : otherChilds)
			{
				if(!child.equals(otherChild))
				{
					if (Vector2.dst(child.position.x, child.position.y, otherChild.position.x,
						otherChild.position.y) < SEARCH_NODE_MINIMUM_NODE_DISTANCE)
					{
						dirtyNodes.add(child);
					}
				}
			}
		}
		return dirtyNodes;
	}
	
//	private ArrayList<SearchNode> getTooCloseCousinNodes(SearchNode node)
//	{
//		ArrayList<SearchNode> dirtyNodes = new ArrayList<SearchNode>();
//		
//		if(parentNode != null)
//		{
//			if(parentNode.parentNode != null)
//			{
//				SearchNode grandParent = parentNode.parentNode;
//				ArrayList<SearchNode> cousinNodes = new ArrayList<SearchNode>(grandParent.childNodes);
//			}
//		}
//		
//		
//		
//		cousinNodes.remove(node);
//		
//		// if node is too close to brother nodes
//		for (SearchNode otherNode : cousinNodes)
//		{
//			if (Vector2.dst(node.position.x, node.position.y, otherNode.position.x,
//					otherNode.position.y) < SEARCH_NODE_MINIMUM_NODE_DISTANCE)
//			{
//				dirtyNodes.add(otherNode);
//			}
//
//		}
//		return dirtyNodes;
//	}
	
	

	/**
	 * 
	 * 
	 * @return true if any child is still searching, false if all childs are done
	 *         searching
	 */
	public boolean areChildSearching()
	{
		boolean areSearching = false;
		for (SearchNode node : childNodes)
		{
			if (node.isSearching)
			{
				areSearching = true;
				break;
			}
		}
		return areSearching;
	}
	
	public static void debugLegacy(SearchNode parentNode, ShapeRenderer debug)
	{
		SearchNode parent = parentNode;

		if (parent.parentNode != null)
		{
			parent.debugChildNode(debug);
			debugLegacy(parent.parentNode, debug);
		}
	}

	public void debugChildNode(ShapeRenderer debug)
	{
		for (SearchNode node : childNodes)
		{
//			if (node.isSearching)
//			{
//				debug.setColor(Color.ORANGE);
//			}
//			else
//			{
//				debug.setColor(Color.RED);
//			}
			
			if(nodeColor != null)
			{
				debug.setColor(nodeColor);
			}
			else
			{
				debug.setColor(Color.RED);
			}
			
			debug.line(position, node.position);
			debug.circle(node.position.x, node.position.y, SEARCH_NODE_MINIMUM_NODE_DISTANCE / 2);
		}

		debug.setColor(Color.GREEN);

	}
}
