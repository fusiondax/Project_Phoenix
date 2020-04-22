package com.phoenix.io;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.GraphicComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.NameComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.VelocityComponent;

public class MapLoader
{
	public static final String GAME_CONFIG_PATH = "game_config";

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
				e = getInitializedEntity(nameComp.name);
			}
			
			// now, we give the entity its map-specific attributes
			e = addMapSpecificAttributeToEntity(e, compList);
			
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
				Entity e = getInitializedEntity(mo.getName());
				
				// once the entity was initialized, we need to give it its's map-specific attributes
				PositionComponent p = e.getComponent(PositionComponent.class);

				p.pos.x = Float.parseFloat(mo.getProperties().get("x").toString());
				p.pos.y = Float.parseFloat(mo.getProperties().get("y").toString());

				/*System.out.println("terrain: " + mo.getName() + " X:" + mo.getProperties().get("x").toString() + " Y:"
						+ mo.getProperties().get("y").toString());*/

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

	/**
	 * creates a brand new entity of the given name and initializes it with its default configuration, giving it its components
	 * and default values for those component's attributes
	 * @param entityName the name of the entity
	 * @return
	 */
	private static Entity getInitializedEntity(String entityName)
	{
		Entity entity = new Entity();

		JsonValue entityConfig = getEntityConfigFile(entityName);

		for (JsonValue componentsJson : entityConfig.get("components"))
		{
			String componentType = componentsJson.get("component_type").asString();

			Component comp = null;
			switch (componentType)
			{
				case "Name":
				{
					comp = new NameComponent();
					if (componentsJson.get("name") != null)
					{
						((NameComponent) comp).name = componentsJson.get("name").asString();
					}
					break;
				}
				case "Graphic":
				{
					comp = new GraphicComponent();
					if (componentsJson.get("texture") != null)
					{
						((GraphicComponent) comp).textureName = componentsJson.get("texture").asString();
					}
					break;
				}
				case "Position":
				{
					comp = new PositionComponent();
					break;
				}
	
				case "Velocity":
				{
					comp = new VelocityComponent();
					break;
				}
	
				case "CollisionHitbox":
				{
					comp = new CollisionHitboxComponent();
					//TODO import stuff for collisionhitbox
					if(componentsJson.get("shape") != null && componentsJson.get("size") != null)
					{
						((CollisionHitboxComponent) comp).hitboxShape = componentsJson.get("shape").asString();
						((CollisionHitboxComponent) comp).size = componentsJson.get("size").asFloat();
					}	
					break;
				}
	
				case "MovementAI":
				{
					comp = new MovementAIComponent();
					if (componentsJson.get("unitMaxSpeed") != null)
					{
						((MovementAIComponent) comp).unitMaxSpeed = componentsJson.get("unitMaxSpeed").asFloat();
					}
					
					if (componentsJson.get("passable_terrain") != null)
					{
						String[] terrains = componentsJson.get("passable_terrain").asStringArray();
						
						for(int i = 0; i < terrains.length; i++)
						{
							((MovementAIComponent) comp).passableTerrains.add(terrains[i]);
						}
					}
					
					break;
				}
	
				case "Selection":
				{
					comp = new SelectionComponent();
					
					//TODO instanciate "mode" parameter 
					break;
				}
	
				case "Terrain":
				{
					comp = new TerrainComponent();
					if (componentsJson.get("type") != null)
					{
						((TerrainComponent)comp).type = componentsJson.get("type").asString();
					}
					break;
				}
			}
			entity.add(comp);

		}
		return entity;
	}
	
	private static Entity addMapSpecificAttributeToEntity(Entity initialEntity, ArrayList<Component> mapLoadedComponents)
	{
		for(Component comp : mapLoadedComponents)
		{
			String className = comp.getClass().getSimpleName();
			//System.out.println(className);
			switch(className)
			{
				case "PositionComponent":
				{
					PositionComponent entityPos = initialEntity.getComponent(PositionComponent.class);
					entityPos.pos.set(((PositionComponent) comp).pos);
					break;
				}
			}
		}
		
		return initialEntity;
	}

	private static JsonValue getEntityConfigFile(String entityFileName)
	{
		return JsonUtility.readJson(GAME_CONFIG_PATH + "/" + entityFileName + ".json");
	}
}
