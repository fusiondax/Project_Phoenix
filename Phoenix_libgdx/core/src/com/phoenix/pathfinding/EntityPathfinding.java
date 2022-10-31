package com.phoenix.pathfinding;

import java.util.ArrayList;
import java.util.List;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.ValidTerrainTypesComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.entityAction.MoveEntityAction;
import com.phoenix.game.Phoenix;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.utility.EntityUtility;
import com.phoenix.utility.GameWorldUtility;

public class EntityPathfinding
{
	public static void searchNewPath(Engine engine, Vector2 initialPoint, Vector2 startNode, Vector2 endNode, MoveEntityAction mea,
			ValidTerrainTypesComponent vttc)
	{
		ImmutableArray<Entity> allTerrains = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());

		Entity startTerrain = GameWorldUtility.getEntityAtLocation(engine, initialPoint,
				Family.one(TerrainComponent.class).get());
		PositionComponent startTerrainPos = startTerrain.getComponent(PositionComponent.class);

		Entity endTerrain = GameWorldUtility.getEntityAtLocation(engine, endNode,
				Family.one(TerrainComponent.class).get());
		PositionComponent endTerrainPos = endTerrain.getComponent(PositionComponent.class);

		int[] mapDimension = GameWorldUtility.getMapDimension(engine);
		GridCell[][] cells = new GridCell[mapDimension[0]][mapDimension[1]];

		// populate cells with all of the terrain tiles
		for (Entity terrainEntity : allTerrains)
		{
			PositionComponent entityPos = terrainEntity.getComponent(PositionComponent.class);
			TerrainComponent terrainComp = terrainEntity.getComponent(TerrainComponent.class);
			Vector2 terrainPos2D = new Vector2(entityPos.pos2D);

			int x = (int) terrainPos2D.x / Phoenix.TERRAIN_SIZE;
			int y = (int) terrainPos2D.y / Phoenix.TERRAIN_SIZE;
			boolean isPassableTerrain = vttc.types.contains(terrainComp.type);

			cells[x][y] = new GridCell(x, y, isPassableTerrain);
		}

		NavigationGrid<GridCell> navGrid = new NavigationGrid<GridCell>(cells, true);
		GridFinderOptions opt = new GridFinderOptions();
		opt.allowDiagonal = false;
		AStarGridFinder<GridCell> finder = new AStarGridFinder<GridCell>(GridCell.class, opt);

		Vector2 startTerrainPosition2D = new Vector2(startTerrainPos.pos2D);

		Vector2 endTerrainPosition2D = new Vector2(endTerrainPos.pos2D);

		int startXcoord = (int) startTerrainPosition2D.x / Phoenix.TERRAIN_SIZE;
		int startYcoord = (int) startTerrainPosition2D.y / Phoenix.TERRAIN_SIZE;
		int endXcoord = (int) endTerrainPosition2D.x / Phoenix.TERRAIN_SIZE;
		int endYcoord = (int) endTerrainPosition2D.y / Phoenix.TERRAIN_SIZE;

		try
		{
			List<GridCell> pathToEnd = finder.findPath(cells[startXcoord][startYcoord], cells[endXcoord][endYcoord],
					navGrid);

			if (pathToEnd != null)
			{
				ArrayList<Vector2> convertedPath = EntityPathfinding.getConvertedCoordinates(pathToEnd);

				// debugFoundPath(convertedPath);
				if (convertedPath.size() > 0)
				{
					convertedPath = EntityPathfinding.getOptimizedPath(convertedPath, engine, vttc);
				}

				EntityPathfinding.updateEntityDestinations(mea, convertedPath);
			}
			else
			{
				System.out.println("impossible path");
			}

		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println(e.toString());
		}
	}

	public static void updateEntityDestinations(MoveEntityAction mea, ArrayList<Vector2> newPath)
	{
		ArrayList<Vector2> oldDestinations = new ArrayList<Vector2>(mea.destinations);

		mea.destinations.clear();

		mea.destinations.addAll(newPath);
		mea.destinations.addAll(oldDestinations);
	}

	public static ArrayList<Vector2> getConvertedCoordinates(List<GridCell> path)
	{
		ArrayList<Vector2> convertedPath = new ArrayList<Vector2>();
		for (GridCell cell : path)
		{
			Vector2 node = new Vector2(cell.x * Phoenix.TERRAIN_SIZE, cell.y * Phoenix.TERRAIN_SIZE);
			convertedPath.add(node);
		}

		return convertedPath;
	}

	public static ArrayList<Vector2> getOptimizedPath(ArrayList<Vector2> pathToOptimize, Engine engine,
			ValidTerrainTypesComponent vttc)
	{
		ArrayList<Vector2> pathToOptimizeWithMidNodes = new ArrayList<Vector2>();

		pathToOptimizeWithMidNodes.add(pathToOptimize.get(0));
		for (int i = 0; i < pathToOptimize.size() - 1; i++)
		{
			// step 1: create nodes between each existing nodes and add them to the node
			// list in the correct order
			Vector2 node = pathToOptimize.get(i);

			Vector2 nextNode = pathToOptimize.get(i + 1);

			// create the mid node using the average between the current node and the next
			// node's position
			Vector2 midNode = new Vector2((node.x + nextNode.x) / 2, (node.y + nextNode.y) / 2);

			pathToOptimizeWithMidNodes.add(midNode);
			pathToOptimizeWithMidNodes.add(nextNode);

		}

		// step 2: from the first node, attemps to connect to the latest nodes until it
		// finds a node that connects. repeat this step until the last node is reached,
		// each time using the
		// node that the connection was made with
		ArrayList<Vector2> optimizedPath = new ArrayList<Vector2>();

		Vector2 startNodeToConnect = pathToOptimizeWithMidNodes.get(0);

		optimizedPath.add(startNodeToConnect);

		// while the start node is not the last node
		while (!startNodeToConnect.equals(pathToOptimizeWithMidNodes.get(pathToOptimizeWithMidNodes.size() - 1)))
		{
			for (int endNodeIndex = pathToOptimizeWithMidNodes.size() - 1; endNodeIndex > pathToOptimizeWithMidNodes
					.indexOf(startNodeToConnect); endNodeIndex--)
			{
				Vector2 endNodeToConnect = pathToOptimizeWithMidNodes.get(endNodeIndex);

				if (!CollisionDetector.isSegmentCollisionRectangles(startNodeToConnect, endNodeToConnect,
						EntityUtility.getRectanglesFromTerrains(EntityUtility.getImpassableTerrains(engine, vttc))))
				{
					optimizedPath.add(endNodeToConnect);
					startNodeToConnect = endNodeToConnect;
					break;
				}
			}
		}

		return optimizedPath;
	}
	
	@Deprecated
	public void debugFoundPath(ArrayList<Vector2> path)
	{
		/*
		debug.setColor(Color.ORANGE);

		for (Vector2 cell : path)
		{
			debug.circle(cell.x, cell.y, 5);
		}
		*/
	}
}
