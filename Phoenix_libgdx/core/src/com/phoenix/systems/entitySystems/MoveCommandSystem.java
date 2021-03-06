package com.phoenix.systems.entitySystems;

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
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.MoveCommandComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.ValidTerrainTypesComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.game.Phoenix;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.utility.EntityUtility;
import com.phoenix.utility.GameWorldUtility;

public class MoveCommandSystem extends IteratingSystem
{
	private ComponentMapper<MoveCommandComponent> mam = ComponentMapper.getFor(MoveCommandComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> mm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionHitboxComponent> chm = ComponentMapper.getFor(CollisionHitboxComponent.class);
	private ComponentMapper<ValidTerrainTypesComponent> vttm = ComponentMapper.getFor(ValidTerrainTypesComponent.class);

	private ShapeRenderer debug;

	public MoveCommandSystem()
	{
		this(null);
	}

	public MoveCommandSystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class, MoveCommandComponent.class,
				CollisionHitboxComponent.class, ValidTerrainTypesComponent.class).get(), 0);
		this.debug = debug;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		MoveCommandComponent mac = mam.get(entity);

		if (!mac.destinations.isEmpty())
		{
			Engine engine = getEngine();
			PositionComponent entityPosition = pm.get(entity);
			VelocityComponent entityMovement = mm.get(entity);
			CollisionHitboxComponent entityHitbox = chm.get(entity);
			ValidTerrainTypesComponent entityValidTerrainTypes = vttm.get(entity);

			Vector2 entityPosition2d = new Vector2(entityPosition.pos2D);
			float unitMaxSpeed = mac.unitMaxSpeed;

			Vector2 nextDestination = mac.destinations.get(0);

			if (nextDestination.dst(entityPosition2d) >= entityHitbox.size / 2)
			{
				Vector2 nextDestinationVector = new Vector2(nextDestination.x - entityPosition2d.x,
						nextDestination.y - entityPosition2d.y);
				nextDestinationVector.setLength(unitMaxSpeed);

				Vector2 nextPathStartPoint = new Vector2().add(entityPosition2d).add(nextDestinationVector);

				Vector2 immediateMovementLocation = new Vector2();
				immediateMovementLocation
						.add(new Vector2(nextDestinationVector.x * deltaTime, nextDestinationVector.y * deltaTime));
				immediateMovementLocation.add(entityPosition2d);

				Circle hitboxCircle = new Circle(immediateMovementLocation, entityHitbox.size);

				// debug.setColor(Color.GREEN);
				// debug.line(nextPathStartPoint, nextDestination);
				// debug.setColor(Color.YELLOW);
				// debug.circle(hitboxCircle.x, hitboxCircle.y, hitboxCircle.radius);

				// if the unit is colliding with something for more than a second
				// TODO add more refined pathfinding start conditions: ignore moving friendly
				// units, ask stationary friendly units who are blocking to move around
				if (CollisionDetector.isCircleCollisionRectangles(hitboxCircle, EntityUtility.getRectanglesFromTerrains(
						EntityUtility.getImpassableTerrains(engine, entityValidTerrainTypes))))
				{
					if (mac.startPathfindingDelay <= 0)
					{
						searchNewPath(entityPosition2d, nextPathStartPoint, nextDestination, mac,
								entityValidTerrainTypes);
					}
					else
					{
						mac.startPathfindingDelay--;
					}
				}
				else // if not, proceed on current vector
				{
					mac.startPathfindingDelay = MoveCommandComponent.START_PATHFINDING_DELAY_MAX;

					// debug.setColor(Color.RED);
					// debug.line(new Vector2(), velocityVector);

				}
				// debugFoundPath(mac.destinations);
				// System.out.println("units wants to move by: " + entityVelocity.velocity);
				
				entityMovement.velocity.add(nextDestinationVector);
				
//				debug.setColor(Color.WHITE);
//				debug.line(entityPosition2d, entityPosition2d.cpy().add(nextDestinationVector));
//				
//				debug.circle(entityPosition2d.x + nextDestinationVector.x, entityPosition2d.y + nextDestinationVector.y, nextDestinationVector.len() / 10);
				

			}
			else // unit's hitbox is at destination, remove current destination point and stops
					// the unit there
			{
				mac.destinations.remove(0);
				entityMovement.velocity.setZero();
			}
		}
	}

	private void searchNewPath(Vector2 initialPoint, Vector2 startNode, Vector2 endNode,
			MoveCommandComponent mac, ValidTerrainTypesComponent vttc)
	{
		Engine engine = getEngine();
		ImmutableArray<Entity> allTerrains = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());

		Entity startTerrain = GameWorldUtility.getEntityAtLocation(engine, initialPoint,
				Family.one(TerrainComponent.class).get());
		PositionComponent startTerrainPos = startTerrain.getComponent(PositionComponent.class);

		Entity endTerrain = GameWorldUtility.getEntityAtLocation(engine, endNode,
				Family.one(TerrainComponent.class).get());
		PositionComponent endTerrainPos = endTerrain.getComponent(PositionComponent.class);

		int[] mapDimension = GameWorldUtility.getMapDimension(engine);
		GridCell[][] cells = new GridCell[mapDimension[0]][mapDimension[1]];

		for (Entity terrainEntity : allTerrains)
		{
			PositionComponent entityPos = terrainEntity.getComponent(PositionComponent.class);
			TerrainComponent terrainComp = terrainEntity.getComponent(TerrainComponent.class);
			Vector2 terrainPos2D = new Vector2(entityPos.pos2D);

			int x = (int) terrainPos2D.x / Phoenix.TERRAIN_SIZE;
			int y = (int) terrainPos2D.y / Phoenix.TERRAIN_SIZE;
			boolean isPassableTerrain = vttc.types.contains(terrainComp.type);

			// if (isPassableTerrain)
			// {
			// debug.setColor(Color.YELLOW);
			// }
			// else
			// {
			// debug.setColor(Color.RED);
			// }
			// debug.circle(terrainPos2D.x, terrainPos2D.y, 10);

			cells[x][y] = new GridCell(x, y, isPassableTerrain);
		}

		NavigationGrid<GridCell> navGrid = new NavigationGrid<GridCell>(cells, true);
		GridFinderOptions opt = new GridFinderOptions();
		opt.allowDiagonal = false;
		AStarGridFinder<GridCell> finder = new AStarGridFinder<GridCell>(GridCell.class, opt);

		Vector2 startTerrainPosition2D = new Vector2(startTerrainPos.pos2D);
		// debug.setColor(Color.WHITE);
		// debug.circle(startTerrainPosition2D.x, startTerrainPosition2D.y, 15);

		Vector2 endTerrainPosition2D = new Vector2(endTerrainPos.pos2D);
		// debug.setColor(Color.BLACK);
		// debug.circle(endTerrainPosition2D.x, endTerrainPosition2D.y, 15);

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
				ArrayList<Vector2> convertedPath = getConvertedCoordinates(pathToEnd);

				// debugFoundPath(convertedPath);
				if (convertedPath.size() > 0)
				{
					convertedPath = getOptimizedPath(convertedPath, engine, vttc);
				}

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
	}

	private void updateEntityDestinations(MoveCommandComponent mac, ArrayList<Vector2> newPath)
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

	private ArrayList<Vector2> getOptimizedPath(ArrayList<Vector2> pathToOptimize, Engine engine,
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

	public void debugFoundPath(ArrayList<Vector2> path)
	{
		debug.setColor(Color.ORANGE);

		for (Vector2 cell : path)
		{
			debug.circle(cell.x, cell.y, 5);
		}
	}
}
