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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.components.GraphicComponent;
import com.phoenix.components.HitboxComponent;
import com.phoenix.components.MovementAIComponent;
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
			Entity e = new Entity();
			for (Component comp : compList)
			{
				e.add(comp);
			}
			engine.addEntity(e);
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
				Entity e = getEntity(mo.getName());

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

	private static Entity getEntity(String entityName)
	{
		Entity entity = new Entity();

		JsonValue entityConfig = getEntityConfigFile(entityName);

		for (JsonValue componentsJson : entityConfig.get("components"))
		{
			String componentType = componentsJson.get("component_type").asString();

			Component comp = null;
			switch (componentType)
			{
				case "Graphic":
				{
					comp = new GraphicComponent();
					if (componentsJson.get("texture") != null)
					{
						
						((GraphicComponent) comp).textureName = componentsJson.get("texture").asString();
						//((GraphicComponent) comp).textureRegion = new TextureRegion(new Texture(componentsJson.get("texture").asString()));
					}
					break;
				}
				case "Position":
				{
					comp = new PositionComponent();
					if (componentsJson.get("position_x") != null)
					{
						((PositionComponent) comp).pos.x = componentsJson.get("position_x").asFloat();
					}
					if (componentsJson.get("position_y") != null)
					{
						((PositionComponent) comp).pos.y = componentsJson.get("position_y").asFloat();
					}
	
					break;
				}
	
				case "Velocity":
				{
					comp = new VelocityComponent();
					if (componentsJson.get("delta_x") != null)
					{
						((VelocityComponent) comp).velocity.x = componentsJson.get("delta_x").asFloat();
					}
	
					if (componentsJson.get("delta_y") != null)
					{
						((VelocityComponent) comp).velocity.y = componentsJson.get("delta_Y").asFloat();
					}
	
					break;
				}
	
				case "Hitbox":
				{
					comp = new HitboxComponent();
					((HitboxComponent) comp).radius = componentsJson.get("radius").asFloat();
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
						String[] types = componentsJson.get("type").asStringArray();
						
						for(int i = 0; i < types.length; i++)
						{
							((TerrainComponent)comp).types.add(types[i]);
						}
						
					}
					break;
				}
			}
			entity.add(comp);

		}
		return entity;
	}

	private static JsonValue getEntityConfigFile(String entityFileName)
	{
		return JsonUtility.readJson(GAME_CONFIG_PATH + "/" + entityFileName + ".json");
	}
}
