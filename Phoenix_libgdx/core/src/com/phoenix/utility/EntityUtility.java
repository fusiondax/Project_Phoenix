package com.phoenix.utility;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
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
}
