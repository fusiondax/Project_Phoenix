package com.phoenix.utility;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.ValidTerrainTypesComponent;

public class EntityUtility
{
	public static ArrayList<Entity> getImpassableTerrains(Engine engine, ValidTerrainTypesComponent vttc)
	{
		ArrayList<Entity> blockingTerrains = new ArrayList<Entity>();
		
		if(vttc != null)
		{
			ImmutableArray<Entity> allTerrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());
	
			// remove terrains that are accessible to the moving entity to only have
			// terrains that blocks the entity
	
			ArrayList<String> passableTerrains = vttc.types;
	
			for (Entity e : allTerrainEntities)
			{
				TerrainComponent t = e.getComponent(TerrainComponent.class);
	
				if (!passableTerrains.contains(t.type))
				{
					blockingTerrains.add(e);
				}
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
				Rectangle terrainRect = new Rectangle(terrainPos.pos2D.x - hitbox.size / 2,
						terrainPos.pos2D.y - hitbox.size / 2, hitbox.size, hitbox.size);

				rectangles.add(terrainRect);
			}
		}
		return rectangles;
	}
	
	/**
	 * 
	 * @param e
	 * @return null if entity does not have a terrain Component
	 */
	public static Polygon getPolygonFromTerrain(Entity e)
	{
		Polygon polygon = null;
		
		TerrainComponent terrain = e.getComponent(TerrainComponent.class);
		
		if(terrain != null)
		{
			float[] vertices = new float[terrain.vertices.size() * 2];
		
			for(Vector2 vertex : terrain.vertices)
			{
				vertices[(terrain.vertices.indexOf(vertex) * 2)] = vertex.x;
				vertices[(terrain.vertices.indexOf(vertex) * 2) + 1] = vertex.y;
			}
			
			polygon = new Polygon(vertices);
		}
		else
		{
			System.out.println("Error: entity parameter does not have a terrain component");
		}
		return polygon;
	}
}
