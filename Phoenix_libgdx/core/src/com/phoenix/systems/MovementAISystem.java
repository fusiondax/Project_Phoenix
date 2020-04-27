package com.phoenix.systems;

import java.util.ArrayList;
import java.util.List;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.game.Phoenix;
import com.phoenix.pathfinding.SearchNode;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.utility.GameUtility;

public class MovementAISystem extends IteratingSystem
{
	private ComponentMapper<MovementAIComponent> mam = ComponentMapper.getFor(MovementAIComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionHitboxComponent> chm = ComponentMapper.getFor(CollisionHitboxComponent.class);

	private ShapeRenderer debug;

	public MovementAISystem()
	{
		this(null);
	}

	public MovementAISystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class, MovementAIComponent.class,
				CollisionHitboxComponent.class).get());
		this.debug = debug;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		MovementAIComponent mac = mam.get(entity);

		// TODO new pathfinding system
		if (!mac.destinations.isEmpty())
		{
			Engine engine = getEngine();
			PositionComponent entityPosition = pm.get(entity);
			VelocityComponent entityVelocity = vm.get(entity);
			CollisionHitboxComponent entityHitbox = chm.get(entity);

			Vector2 entityPosition2d = new Vector2(entityPosition.pos.x, entityPosition.pos.y);
			float unitMaxSpeed = mac.unitMaxSpeed;

			Vector2 nextDestination = mac.destinations.get(0);

			if (nextDestination.dst(entityPosition2d) >= entityHitbox.size / 2)
			{
				CollisionDetector detector = new CollisionDetector(engine);

				Vector2 nextDestinationVector = new Vector2(nextDestination.x - entityPosition2d.x,
						nextDestination.y - entityPosition2d.y);
				nextDestinationVector.setLength(unitMaxSpeed);

				Vector2 nextPathStartPoint = new Vector2().add(entityPosition2d).add(nextDestinationVector);

				Vector2 immediateMovementLocation = new Vector2();
				immediateMovementLocation
						.add(new Vector2(nextDestinationVector.x * deltaTime, nextDestinationVector.y * deltaTime));
				immediateMovementLocation.add(entityPosition2d);

				Circle hitboxCircle = new Circle(immediateMovementLocation, entityHitbox.size);

				debug.setColor(Color.GREEN);
				debug.line(nextPathStartPoint, nextDestination);
				// debug.setColor(Color.YELLOW);
				// debug.circle(hitboxCircle.x, hitboxCircle.y, hitboxCircle.radius);

				// detector.debugRectanglesHitbox(debug,
				// CollisionDetector.getRectanglesFromTerrains(detector.getImpassableTerrains(mac)));

				// if the unit's movements speed is not at its max speed
				if (detector.isCircleCollisionRectangles(hitboxCircle,
						CollisionDetector.getRectanglesFromTerrains(detector.getImpassableTerrains(mac))))
				{
					if (mac.startPathfindingDelay <= 0)
					{
						searchNewPath(detector, entityPosition2d, nextPathStartPoint, nextDestination, mac);
					}
					else
					{
						mac.startPathfindingDelay--;
					}
				}
				else // if not, proceed on current vector
				{
					mac.initialNode = null;
					mac.startPathfindingDelay = MovementAIComponent.START_PATHFINDING_DELAY_MAX;

					// debug.setColor(Color.RED);
					// debug.line(new Vector2(), velocityVector);

				}
				entityVelocity.velocity.set(nextDestinationVector);

			}
			else // unit's hitbox is at destination, remove current destination point and stops
					// the unit there
			{
				mac.initialNode = null;
				mac.destinations.remove(0);
				entityVelocity.velocity.setZero();
			}
		}
	}

	private void searchNewPath(CollisionDetector detector, Vector2 initialPoint, Vector2 startNode, Vector2 endNode,
			MovementAIComponent mac)
	{
		Engine engine = getEngine();
		ImmutableArray<Entity> allTerrains = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());

		Entity startTerrain = detector.getEntityAtLocation(initialPoint, Family.one(TerrainComponent.class).get());
		PositionComponent startTerrainPos = startTerrain.getComponent(PositionComponent.class);

		Entity endTerrain = detector.getEntityAtLocation(endNode, Family.one(TerrainComponent.class).get());
		PositionComponent endTerrainPos = endTerrain.getComponent(PositionComponent.class);

		int[] mapDimension = GameUtility.getMapDimension(engine);
		GridCell[][] cells = new GridCell[mapDimension[0]][mapDimension[1]];

		for (Entity terrainEntity : allTerrains)
		{
			PositionComponent entityPos = terrainEntity.getComponent(PositionComponent.class);
			TerrainComponent terrainComp = terrainEntity.getComponent(TerrainComponent.class);
			Vector2 terrainPos2D = new Vector2(entityPos.pos.x, entityPos.pos.y);

			int x = (int) terrainPos2D.x / Phoenix.TERRAIN_SIZE;
			int y = (int) terrainPos2D.y / Phoenix.TERRAIN_SIZE;
			boolean isPassableTerrain = mac.passableTerrains.contains(terrainComp.type);

//			if (isPassableTerrain)
//			{
//				debug.setColor(Color.YELLOW);
//			}
//			else
//			{
//				debug.setColor(Color.RED);
//			}
//			debug.circle(terrainPos2D.x, terrainPos2D.y, 10);

			cells[x][y] = new GridCell(x, y, isPassableTerrain);
		}

		NavigationGrid<GridCell> navGrid = new NavigationGrid<GridCell>(cells, true);
		GridFinderOptions opt = new GridFinderOptions();
		opt.allowDiagonal = false;
		AStarGridFinder<GridCell> finder = new AStarGridFinder<GridCell>(GridCell.class, opt);

		Vector2 startTerrainPosition2D = new Vector2(startTerrainPos.pos.x, startTerrainPos.pos.y);
//		debug.setColor(Color.WHITE);
//		debug.circle(startTerrainPosition2D.x, startTerrainPosition2D.y, 15);

		Vector2 endTerrainPosition2D = new Vector2(endTerrainPos.pos.x, endTerrainPos.pos.y);
//		debug.setColor(Color.BLACK);
//		debug.circle(endTerrainPosition2D.x, endTerrainPosition2D.y, 15);

		int startXcoord = (int) startTerrainPosition2D.x / Phoenix.TERRAIN_SIZE;
		int startYcoord = (int) startTerrainPosition2D.y / Phoenix.TERRAIN_SIZE;
		int endXcoord = (int) endTerrainPosition2D.x / Phoenix.TERRAIN_SIZE;
		int endYcoord = (int) endTerrainPosition2D.y / Phoenix.TERRAIN_SIZE;

		try
		{
			// long startTime = System.nanoTime();

			List<GridCell> pathToEnd = finder.findPath(cells[startXcoord][startYcoord], cells[endXcoord][endYcoord],
					navGrid);

			// long timeToInstantiateGrid = System.nanoTime();
			// System.out.println("---------------------------------");
			// System.out.println("time to find path in nanoseconds: " +
			// (timeToInstantiateGrid - startTime));
			if (pathToEnd != null)
			{
				// System.out.println("pathToEnd: " + pathToEnd.toString());

				ArrayList<Vector2> convertedPath = getConvertedCoordinates(pathToEnd);

				//debugFoundPath(convertedPath);
				
				convertedPath = getOptimizedPath(convertedPath);

				updateEntityDestinations(mac, convertedPath);
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

		// if (mac.initialNode == null)
		// {
		// PositionComponent startTilePosition =
		// detector.getEntityAtLocation(initialPoint).getComponent(PositionComponent.class);
		// if(startTilePosition != null)
		// {
		// Vector2 startTile = new Vector2(startTilePosition.pos.x,
		// startTilePosition.pos.y);
		// mac.initialNode = new SearchNode(startTile);
		// mac.initialNode.isSearching = false;
		// }
		// }
		// else
		// {
		// SearchNode validPath = SearchNode.getValidPath(mac.initialNode);
		// if (validPath != null)
		// {
		// SearchNode.debugLegacy(validPath, debug);
		// }
		// else
		// {
		// mac.initialNode.startRecursiveSearch(detector, mac, startNode, endNode,
		// debug);
		// }
		// }
	}

	private void updateEntityDestinations(MovementAIComponent mac, ArrayList<Vector2> newPath)
	{
		ArrayList<Vector2> oldDestinations = new ArrayList<Vector2>(mac.destinations);

		mac.destinations.clear();

		mac.destinations.addAll(newPath);
		mac.destinations.addAll(oldDestinations);
	}

	private ArrayList<Vector2> getConvertedCoordinates(List<GridCell> path)
	{
		ArrayList<Vector2> convertedPath = new ArrayList<Vector2>();
		for (GridCell cell : path)
		{
			Vector2 node = new Vector2(cell.x * Phoenix.TERRAIN_SIZE, cell.y * Phoenix.TERRAIN_SIZE);
			convertedPath.add(node);
		}

		return convertedPath;
	}
	
	private ArrayList<Vector2> getOptimizedPath(ArrayList<Vector2> pathToOptimize)
	{
		ArrayList<Vector2> optimizedPath = new ArrayList<Vector2>();
		for (Vector2 node : pathToOptimize)
		{
			//step 1: 
			
			
			
			optimizedPath.add(node);
		}
		

		return optimizedPath;
	}
	

	public void debugFoundPath(ArrayList<Vector2> path)
	{
		debug.setColor(Color.ORANGE);

		for (Vector2 cell : path)
		{
			debug.circle(cell.x, cell.y, 5);
		}
	}
}
