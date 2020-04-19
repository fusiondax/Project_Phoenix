package com.phoenix.pathfinding;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.game.Phoenix;

public class CollisionDetector
{
	
	public static final float PROXY_TERRAIN_MAX_DISTANCE = 400;
	
	/**
	 * retrieves entities with the TerrainComponent Component that are close to the given position
	 * @param location
	 * @return
	 */
	public static ArrayList<Entity> getProxyTerrains(Engine engine, Vector2 location)
	{
		//TODO not gonna use it now, but this might be useful for performance later...
		ImmutableArray<Entity> allTerrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());

		ArrayList<Entity> proxyTerrains = new ArrayList<Entity>();
		
		for(Entity e : allTerrainEntities)
		{
			PositionComponent terrainPos = e.getComponent(PositionComponent.class);
			Vector2 terrainPos2d = new Vector2(terrainPos.pos.x, terrainPos.pos.y);			
			if(terrainPos2d.dst(location) <= PROXY_TERRAIN_MAX_DISTANCE)
			{
				proxyTerrains.add(e);
			}
		}
		
		return proxyTerrains;
	}
	
	public static boolean isCollision(Engine engine, MovementAIComponent movementAiComp, Vector2 targetLocation)
	{
		//TODO potentially very poorly optimized
		
		boolean isCollision = false;
		
		// get all terrains
		ImmutableArray<Entity> allTerrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());
		
		//remove terrains that are accessible to the moving entity to only have terrains that blocks the entity
		
		ArrayList<Entity> blockingTerrains = new ArrayList<Entity>();
		
		ArrayList<String> passableTerrains = movementAiComp.passableTerrains;
		
		for(Entity e : allTerrainEntities)
		{
			TerrainComponent t = e.getComponent(TerrainComponent.class);
			
			if(!passableTerrains.contains(t.type))
			{
				blockingTerrains.add(e);
			}
		}
		
		for(Entity e : blockingTerrains)
		{
			PositionComponent terrainPos = e.getComponent(PositionComponent.class);
			
			Rectangle terrainRect = new Rectangle(terrainPos.pos.x - Phoenix.TERRAIN_SIZE / 2, 
					terrainPos.pos.y - Phoenix.TERRAIN_SIZE / 2, 
					Phoenix.TERRAIN_SIZE, 
					Phoenix.TERRAIN_SIZE);
			
			if(terrainRect.contains(targetLocation))
			{
				isCollision = true;
				break;
			}
		}
		
		return isCollision;
	}
}
