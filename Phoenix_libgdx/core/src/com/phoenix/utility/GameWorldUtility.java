package com.phoenix.utility;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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
		
		for(Entity e : allTerrainEntity)
		{
			PositionComponent entityPosition = e.getComponent(PositionComponent.class); 
			Vector2 position2D = new Vector2(entityPosition.pos2D);
			
			leftMostCoordinate = Math.min(position2D.x, leftMostCoordinate);
			rightMostCoordinate = Math.max(position2D.x, rightMostCoordinate);
			bottomMostCoordinate = Math.min(position2D.y, bottomMostCoordinate);
			topMostCoordinate = Math.max(position2D.y, topMostCoordinate);
		}
		
//		System.out.println("---------------------------------");
//		
//		System.out.println("left:" + leftMostCoordinate);
//		System.out.println("right:" + rightMostCoordinate);
//		System.out.println("bottom:" + bottomMostCoordinate);
//		System.out.println("top:" + topMostCoordinate);
		
		mapDimension[0] = (int) ((rightMostCoordinate - leftMostCoordinate) / Phoenix.TERRAIN_SIZE) + 1;
		mapDimension[1] = (int) ((topMostCoordinate - bottomMostCoordinate) / Phoenix.TERRAIN_SIZE) + 1;	
		
//		System.out.println("x: " + mapDimension[0] + ";y: " + mapDimension[1]);
		
		return mapDimension;
	}
	
	/**
	 * retrieves entities that are within the given range of the given position
	 * 
	 * @param location
	 * @return
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
			PositionComponent entityPosition = e.getComponent(PositionComponent.class);
			Vector2 entityPos2d = new Vector2(entityPosition.pos2D);
			if (entityPos2d.dst(location) <= range)
			{
				proxyEntities.add(e);
			}
		}
		return proxyEntities;
	}
	
	public static Entity getEntityAtLocation(Engine engine, Vector2 location)
	{
		return getEntityAtLocation(engine, location, null);
	}

	public static Entity getEntityAtLocation(Engine engine, Vector2 location, Family whitelist)
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
			Vector2 entityPos2d = new Vector2(entityPos.pos2D);

			float distance = entityPos2d.dst(location);
			if (distance < closestDistance)
			{
				closestEntity = e;
				closestDistance = distance;
			}
		}
		return closestEntity;
	}
}
