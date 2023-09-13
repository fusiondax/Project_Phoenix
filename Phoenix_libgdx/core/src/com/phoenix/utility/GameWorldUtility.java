package com.phoenix.utility;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.game.Phoenix;

public class GameWorldUtility
{
	public static int[] getMapDimension(Engine engine)
	{
		ImmutableArray<Entity> allTerrainEntity = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());

		int[] mapDimension = new int[2];

		float leftMostCoordinate = Float.MAX_VALUE;
		float rightMostCoordinate = Float.MIN_VALUE;
		float bottomMostCoordinate = Float.MAX_VALUE;
		float topMostCoordinate = Float.MIN_VALUE;

		for (Entity e : allTerrainEntity)
		{
			PositionComponent entityPosition = e.getComponent(PositionComponent.class);
			Vector2 position2D = new Vector2(entityPosition.pos2D);

			leftMostCoordinate = Math.min(position2D.x, leftMostCoordinate);
			rightMostCoordinate = Math.max(position2D.x, rightMostCoordinate);
			bottomMostCoordinate = Math.min(position2D.y, bottomMostCoordinate);
			topMostCoordinate = Math.max(position2D.y, topMostCoordinate);
		}

		// System.out.println("---------------------------------");
		//
		// System.out.println("left:" + leftMostCoordinate);
		// System.out.println("right:" + rightMostCoordinate);
		// System.out.println("bottom:" + bottomMostCoordinate);
		// System.out.println("top:" + topMostCoordinate);

		mapDimension[0] = (int) ((rightMostCoordinate - leftMostCoordinate) / Phoenix.TERRAIN_SIZE) + 1;
		mapDimension[1] = (int) ((topMostCoordinate - bottomMostCoordinate) / Phoenix.TERRAIN_SIZE) + 1;

		// System.out.println("x: " + mapDimension[0] + ";y: " + mapDimension[1]);

		return mapDimension;
	}

	public static Vector2 getWorldPositionFromScreenLocation(int screenX, int screenY, OrthographicCamera cam)
	{
		Vector2 worldPos = new Vector2();

		Vector2 adjustedScreenPos = getCenterScreenOriginScreenPos(screenX, screenY);

		worldPos.x = cam.position.x + ((cam.viewportWidth / 2) * adjustedScreenPos.x);
		worldPos.y = cam.position.y + ((cam.viewportHeight / 2) * adjustedScreenPos.y);

		return worldPos;
	}

	public static Vector2 getCenterScreenOriginScreenPos(int screenX, int screenY)
	{
		Vector2 adjustedScreenPos = new Vector2();

		adjustedScreenPos.x = screenX - (Gdx.graphics.getWidth() / 2);
		adjustedScreenPos.y = (screenY - Gdx.graphics.getHeight() / 2) * -1.0f;

		adjustedScreenPos.x /= (float) (Gdx.graphics.getWidth() / 2);
		adjustedScreenPos.y /= (float) (Gdx.graphics.getHeight() / 2);

		return adjustedScreenPos;
	}

	/**
	 * retrieves entities that are within the given range of the given position, does not includes Terrain since they do not have a position component
	 * 
	 * @param location
	 * @return a list of entities
	 */
	public static ArrayList<Entity> getProxyEntities(Engine engine, Vector2 location, float range, Family whitelist)
	{
		ImmutableArray<Entity> allEntities = new ImmutableArray<Entity>(new Array<Entity>());

		if (whitelist == null)
		{
			allEntities = engine.getEntities();
		}
		else
		{
			allEntities = engine.getEntitiesFor(whitelist);
		}

		ArrayList<Entity> proxyEntities = new ArrayList<Entity>();

		for (Entity e : allEntities)
		{
			// TODO 4 the 'entity.getComponent()' method is technically slower than to be
			// using a componentMapper
			PositionComponent entityPosition = e.getComponent(PositionComponent.class);
			if (entityPosition != null)
			{
				Vector2 entityPos2d = new Vector2(entityPosition.pos2D);
				if (entityPos2d.dst(location) <= range)
				{
					proxyEntities.add(e);
				}
			}
		}
		return proxyEntities;
	}

	/**
	 * Returns an entity with a TerrainComponent that exists within the given
	 * position. If multiple terrains exists within the given position, the highest
	 * terrain will be returned.
	 * 
	 * @param engine
	 * @param location
	 * @return
	 */
	public static Entity getTopTerrainEntityAtLocation(Engine engine, Vector2 position)
	{
		ImmutableArray<Entity> allTerrainEntities = new ImmutableArray<Entity>(new Array<Entity>());
		allTerrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());
		
		ArrayList<Entity> containedEntities = new ArrayList<Entity>();
		
		for(Entity e : allTerrainEntities)
		{
			TerrainComponent terrain = e.getComponent(TerrainComponent.class);
			
			Polygon terrainPolygon = EntityUtility.getPolygonFromTerrain(e);
			
			if(terrainPolygon.contains(position))
			{
				containedEntities.add(e);
			}
		}
		// TODO 2 does not get the highest terrain, merely returns the first one in the list
		return containedEntities.get(0);
	}

	public static Entity getEntityAtLocation(Engine engine, Vector2 location)
	{
		return getEntityAtLocation(engine, location, -1.0f, null);
	}

	public static Entity getEntityAtLocation(Engine engine, Vector2 location, float maxDistance)
	{
		return getEntityAtLocation(engine, location, maxDistance, null);
	}

	public static Entity getEntityAtLocation(Engine engine, Vector2 location, Family whitelist)
	{
		return getEntityAtLocation(engine, location, -1.0f, whitelist);
	}

	/**
	 * @param engine
	 * @param location
	 * @param maxDistance
	 *            the maximum distance the algorithm will search for, negative
	 *            values will be considered as no max distance
	 * @param whitelist
	 * @return a single Entity, the one closest to {@link location}, may return null
	 */
	public static Entity getEntityAtLocation(Engine engine, Vector2 location, float maxDistance, Family whitelist)
	{
		ImmutableArray<Entity> allEntities = new ImmutableArray<Entity>(new Array<Entity>());

		if (whitelist == null)
		{
			allEntities = engine.getEntities();
		}
		else
		{
			allEntities = engine.getEntitiesFor(whitelist);
		}

		Entity closestEntity = null;
		float closestDistance = Float.MAX_VALUE;

		for (Entity e : allEntities)
		{
			PositionComponent entityPos = e.getComponent(PositionComponent.class);
			if (entityPos != null)
			{
				Vector2 entityPos2d = new Vector2(entityPos.pos2D);
				float distance = entityPos2d.dst(location);

				if (distance < closestDistance)
				{
					closestEntity = e;
					closestDistance = distance;
				}
			}
		}

		if (maxDistance >= 0)
		{
			if (closestDistance > maxDistance)
			{
				closestEntity = null;
			}
		}

		return closestEntity;
	}
}
