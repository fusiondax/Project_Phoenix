package com.phoenix.physics;

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
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;

public class CollisionDetector
{

	private Engine engine;

	public CollisionDetector(Engine engine)
	{
		this.engine = engine;
	}
	
	public static boolean isShapeCollisionShape(Shape2D shape1, Shape2D shape2)
	{
		// TODO  this is super ugly, and I don't know how to make it cleaner...
		boolean intersected = false;
		
		if(shape1 instanceof Circle)
		{
			if(shape2 instanceof Circle)
			{
				intersected = Intersector.overlaps((Circle)shape1, (Circle)shape2);
			}
			else if(shape2 instanceof Rectangle)
			{
				intersected = Intersector.overlaps((Circle)shape1, (Rectangle)shape2);
			}
		}
		else if (shape1 instanceof Rectangle)
		{
			if(shape2 instanceof Circle)
			{
				intersected = Intersector.overlaps((Circle)shape2, (Rectangle)shape1);
			}
			else if(shape2 instanceof Rectangle)
			{
				intersected = Intersector.overlaps((Rectangle)shape1, (Rectangle)shape2);
			}
		}
		return intersected;
	}
	
	public boolean isCircleCollisionHitboxes(Circle entityHitbox, ArrayList<CollisionHitboxComponent> hitboxes)
	{
		
		return false;
	}
	
	
	public boolean isRectangleCollisionHitboxes(Rectangle entityHitbox, ArrayList<CollisionHitboxComponent> hitboxes)
	{
		return false;
	}
	

	public boolean isCircleCollisionRectangles(Circle entityHitbox, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;

		for (Rectangle rect : rects)
		{
			if (Intersector.overlaps(entityHitbox, rect))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}

	public boolean isPointCollisionRectangles(Vector2 targetLocation, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;

		for (Rectangle rect : rects)
		{
			if (rect.contains(targetLocation))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}
	
	public boolean isSegmentCollisionRectangles(Vector2 segmentStart, Vector2 segmentEnd, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;

		for (Rectangle rect : rects)
		{
			if (Intersector.intersectSegmentRectangle(segmentStart, segmentEnd, rect))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}

	public ArrayList<Entity> getImpassableTerrains(MovementAIComponent mac)
	{
		// TODO potentially poorly optimized
		// get all terrains
		ImmutableArray<Entity> allTerrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());

		// remove terrains that are accessible to the moving entity to only have
		// terrains that blocks the entity

		ArrayList<Entity> blockingTerrains = new ArrayList<Entity>();

		ArrayList<String> passableTerrains = mac.passableTerrains;

		for (Entity e : allTerrainEntities)
		{
			TerrainComponent t = e.getComponent(TerrainComponent.class);

			if (!passableTerrains.contains(t.type))
			{
				blockingTerrains.add(e);
			}
		}
		return blockingTerrains;
	}

	public static ArrayList<Rectangle> getRectanglesFromTerrains(ArrayList<Entity> terrainEntities)
	{
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

		for (Entity e : terrainEntities)
		{
			PositionComponent terrainPos = e.getComponent(PositionComponent.class);
			CollisionHitboxComponent hitbox = e.getComponent(CollisionHitboxComponent.class);

			if (terrainPos != null && hitbox != null)
			{
				Rectangle terrainRect = new Rectangle(terrainPos.pos.x - hitbox.size / 2,
						terrainPos.pos.y - hitbox.size / 2, hitbox.size, hitbox.size);

				rectangles.add(terrainRect);
			}
		}
		return rectangles;
	}

	/**
	 * retrieves entities that are within the given range of the given position
	 * 
	 * @param location
	 * @return
	 */
	public ArrayList<Entity> getProxyEntities(Vector2 location, float range, Family whitelist)
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
			Vector2 entityPos2d = new Vector2(entityPosition.pos.x, entityPosition.pos.y);
			if (entityPos2d.dst(location) <= range)
			{
				proxyEntities.add(e);
			}
		}
		return proxyEntities;
	}
	
	public Entity getEntityAtLocation(Vector2 location)
	{
		return getEntityAtLocation(location, null);
	}

	public Entity getEntityAtLocation(Vector2 location, Family whitelist)
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
			Vector2 entityPos2d = new Vector2(entityPos.pos.x, entityPos.pos.y);

			float distance = entityPos2d.dst(location);
			if (distance < closestDistance)
			{
				closestEntity = e;
				closestDistance = distance;
			}
		}
		return closestEntity;
	}

	public void debugRectanglesHitbox(ShapeRenderer debug, ArrayList<Rectangle> rects)
	{
		debug.setColor(Color.BROWN);
		for (Rectangle rect : rects)
		{
			debug.rect(rect.x, rect.y, rect.width, rect.height);
		}
	}
}
