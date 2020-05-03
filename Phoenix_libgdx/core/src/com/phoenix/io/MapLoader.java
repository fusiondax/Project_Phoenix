package com.phoenix.io;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.phoenix.components.BuildableComponent;
import com.phoenix.components.CollectibleBlueprintComponent;
import com.phoenix.components.NameComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.ResourceComponent;
import com.phoenix.game.Resource;

public class MapLoader
{
	public static GameMap createGameMapFromEngine(Engine engine)
	{
		GameMap gameMap = new GameMap();
		
		for(Entity e : engine.getEntities())
		{
			ArrayList<Component> comps = new ArrayList<Component>();
			
			for(Component c : e.getComponents())
			{
				comps.add(c);
			}
			gameMap.entities.add(comps);
		}
		return gameMap;
	}
	
	public static void addEntitiesToEngine(Engine engine, GameMap gameMap)
	{
		for(ArrayList<Component> compList : gameMap.entities)
		{
			Entity e = null;
			NameComponent nameComp = null;
			// retrieve the NameComponent from an entity's component list
			for (Component comp : compList)
			{
				if(comp instanceof NameComponent)
				{
					nameComp = (NameComponent) comp;
					break;
				}
			}
			
			// if the NameComponent was found, we can initialize the entity
			if(nameComp != null)
			{
				e = EntityLoader.getInitializedEntity(nameComp.name);
			}
			
			// now, we give the entity its map-specific attributes
			e = EntityLoader.updateEntityWithComponents(e, compList);
			
			// unless the entity wasn't able to be created, we add it to the engine
			if(e != null)
			{
				engine.addEntity(e);
			}
			
		}
	}

	public static GameMap getGameMap(String gameMapFileName)
	{
		return JsonUtility.readJsonGameMapFile(gameMapFileName);
	}

	public static void convertTiledMapToGameMap(String tiledMapFileName, String gameMapFileName)
	{
		GameMap gameMap = new GameMap();

		TiledMap visualMap = new TmxMapLoader().load("gamemap/" + tiledMapFileName);

		MapLayers layers = visualMap.getLayers();

		for (MapLayer layer : layers)
		{
			for (MapObject mo : layer.getObjects())
			{
				Entity e = EntityLoader.getInitializedEntity(mo.getName());
				
				// once the entity was initialized, we need to give it its's map-specific attributes
				PositionComponent p = e.getComponent(PositionComponent.class);
				if(p != null)
				{
					p.pos.x = Float.parseFloat(mo.getProperties().get("x").toString());
					p.pos.y = Float.parseFloat(mo.getProperties().get("y").toString());
				}
				
				OwnershipComponent o = e.getComponent(OwnershipComponent.class);
				if(o != null)
				{
					o.owner = mo.getProperties().get("owner").toString();
				}
				
				CollectibleBlueprintComponent coll = e.getComponent(CollectibleBlueprintComponent.class);
				if(coll != null)
				{
					coll.buildableEntityName = mo.getProperties().get("buildableEntityName").toString();
					coll.amount = Integer.parseInt(mo.getProperties().get("amount").toString());
				}
				
				ResourceComponent res = e.getComponent(ResourceComponent.class);
				if(res != null)
				{
					res.resource.amount = Integer.parseInt(mo.getProperties().get("amount").toString());
				}
				
				BuildableComponent build = e.getComponent(BuildableComponent.class);
				if(build != null)
				{
					Object bp = mo.getProperties().get("build_progress");
					if(bp != null)
					{
						build.setBuildProgress(Float.parseFloat(bp.toString()));
					}
					
					Object br = mo.getProperties().get("build_rate");
					if(br != null)
					{
						build.setBuildRate(Float.parseFloat(br.toString()));
					}
				}
					
				// TODO Fetch other custom propertied from TiledMap objects to populate entity
				// components
				
				ArrayList<Component> comps = new ArrayList<Component>();
				
				for(Component c : e.getComponents())
				{
					comps.add(c);
				}

				gameMap.entities.add(comps);
			}
		}

		JsonUtility.writeJsonGameMapFile(gameMapFileName, gameMap);
	}
	
	
}
