package com.phoenix.pathfinding;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.game.Phoenix;

public class CollisionDetector
{
	
	public static final float PROXY_TERRAIN_MAX_DISTANCE = 400;
	
	private Engine engine;
	
	public CollisionDetector(Engine engine)
	{
		this.engine = engine;
	}
	
	public boolean isCircleCollision(Circle entityHitbox, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;
		
		for(Rectangle rect : rects)
		{
			if(Intersector.overlaps(entityHitbox, rect))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}
	
	public boolean isPointCollision(Vector2 targetLocation, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;
		
		for(Rectangle rect : rects)
		{
			if(rect.contains(targetLocation))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}

	public ArrayList<Entity> getCollidableTerrains(MovementAIComponent mac)
	{
		// TODO potentially poorly optimized
		// get all terrains
		ImmutableArray<Entity> allTerrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());
		
		//remove terrains that are accessible to the moving entity to only have terrains that blocks the entity
		
		ArrayList<Entity> blockingTerrains = new ArrayList<Entity>();
		
		ArrayList<String> passableTerrains = mac.passableTerrains;
		
		for(Entity e : allTerrainEntities)
		{
			TerrainComponent t = e.getComponent(TerrainComponent.class);
			
			if(!passableTerrains.contains(t.type))
			{
				blockingTerrains.add(e);
			}
		}
		return blockingTerrains;
	}
	
	public static ArrayList<Rectangle> getRectanglesFromTerrains(ArrayList<Entity> terrainEntities)
	{
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
		
		for(Entity e : terrainEntities)
		{
			PositionComponent terrainPos = e.getComponent(PositionComponent.class);
			
			if(terrainPos != null)
			{
				Rectangle terrainRect = new Rectangle(terrainPos.pos.x - Phoenix.TERRAIN_SIZE / 2, 
					terrainPos.pos.y - Phoenix.TERRAIN_SIZE / 2, 
					Phoenix.TERRAIN_SIZE, 
					Phoenix.TERRAIN_SIZE);
				
				rectangles.add(terrainRect);
			}
		}
		return rectangles;
	}
	
	/**
	 * retrieves entities with the TerrainComponent Component that are close to the given position
	 * @param location
	 * @return
	 */
	public ArrayList<Entity> getProxyTerrains(Vector2 location)
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
	
	public void debugCollidableTerrainHitbox(ShapeRenderer debug, ArrayList<Rectangle> rects)
	{
		debug.setColor(Color.BROWN);
		for(Rectangle rect : rects)
		{
			debug.rect(rect.x, rect.y, rect.width, rect.height);
		}
	}
}
