package com.phoenix.io;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.components.BuildableComponent;
import com.phoenix.components.CollectibleBlueprintComponent;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.NameComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.ParticleComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.RadialMenuComponent;
import com.phoenix.components.ResourceComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.TextureComponent;
import com.phoenix.components.TriggerComponent;
import com.phoenix.components.EntityActionsComponent;
import com.phoenix.components.ValidTerrainTypesComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.resource.Resource;
import com.phoenix.ui.radialMenu.RadialMenuButton;

public class EntityLoader
{
	public static final String ENTITY_CONFIG_PATH = "entity_config";

	/**
	 * creates a brand new entity of the given name and initializes it with its
	 * default configuration, giving it its components and default values for those
	 * component's attributes
	 * 
	 * @param entityName
	 *            the name of the entity
	 * @return
	 */
	public static Entity getInitializedEntity(String entityName)
	{
		Entity entity = new Entity();
		
		Json json = new Json();

		JsonValue entityConfig = getEntityConfigFile("entity_" + entityName);

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
				case "Texture":
				{
					comp = new TextureComponent();
					if (componentsJson.get("texture") != null)
					{
						((TextureComponent) comp).textureName = componentsJson.get("texture").asString();
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
					if (componentsJson.get("friction") != null)
					{
						((VelocityComponent) comp).friction = componentsJson.get("friction").asFloat();
					}
					break;
				}

				case "CollisionHitbox":
				{
					comp = new CollisionHitboxComponent();
					if (componentsJson.get("shape") != null && componentsJson.get("size") != null)
					{
						((CollisionHitboxComponent) comp).hitboxShape = componentsJson.get("shape").asString();
						((CollisionHitboxComponent) comp).size = componentsJson.get("size").asFloat();
					}

					break;
				}

				case "ValidTerrainTypes":
				{
					comp = new ValidTerrainTypesComponent();
					if (componentsJson.get("types") != null)
					{
						String[] terrains = componentsJson.get("types").asStringArray();

						for (int i = 0; i < terrains.length; i++)
						{
							((ValidTerrainTypesComponent) comp).types.add(terrains[i]);
						}
					}
					break;
				}

				case "Selection":
				{
					comp = new SelectionComponent();

					// TODO 3 instanciate "mode" parameter
					break;
				}

				case "Terrain":
				{
					comp = new TerrainComponent();
					if (componentsJson.get("type") != null)
					{
						((TerrainComponent) comp).type = componentsJson.get("type").asString();
					}
					break;
				}

				case "Ownership":
				{
					comp = new OwnershipComponent();
					break;
				}

				case "CollectibleBlueprint":
				{
					comp = new CollectibleBlueprintComponent();
					break;
				}

				case "Buildable":
				{
					comp = new BuildableComponent();
					if (componentsJson.get("build_progress") != null)
					{
						((BuildableComponent) comp).setBuildProgress(componentsJson.get("build_progress").asFloat());
					}
					if (componentsJson.get("build_rate") != null)
					{
						((BuildableComponent) comp).setBuildRate(componentsJson.get("build_rate").asFloat());
					}
					break;
				}

				case "Resource":
				{
					comp = new ResourceComponent();
					((ResourceComponent) comp).resource = new Resource();
					if (componentsJson.get("type_name") != null)
					{
						((ResourceComponent) comp).resource.type = componentsJson.get("type_name").asString();
					}
					break;
				}

				case "Trigger":
				{
					comp = new TriggerComponent();
					break;
				}

				case "RadialMenu":
				{
					
					comp = new RadialMenuComponent();

					for (JsonValue jsonButton : componentsJson.get("buttons"))
					{
						((RadialMenuComponent) comp).buttons.add(new RadialMenuButton(jsonButton.get("type").asString(),
								jsonButton.get("icon_name").asString()));
					}
					
					break;
				}
				
				case "EntityActions":
				{
					comp = json.readValue(EntityActionsComponent.class, componentsJson);
					break;
				}
				
				case "Particle":
				{
					comp = new ParticleComponent();
					if (componentsJson.get("particle_name") != null)
					{
						((ParticleComponent) comp).particleName = componentsJson.get("particle_name").asString();
					}
					break;
				}
			}
			
			
			entity.add(comp);
		}
		return entity;
	}

	// TODO 3 this method could be updated to be more generic: its role, as of right
	// now, is very specific and might even work in a similar fashion
	// to the "MapLoader.convertTiledMapToGameMap" method

	/**
	 * 
	 * 
	 * @param initialEntity
	 *            the entity to be updated
	 * @param updatedComponents
	 *            the list of components to be used to update the initialEntity's
	 *            existing components
	 * @return the initialEntity with its components attributes updated using the
	 *         updatedComponents list
	 */
	public static Entity updateEntityWithComponents(Entity initialEntity, ArrayList<Component> updatedComponents)
	{
		for (Component comp : updatedComponents)
		{
			String className = comp.getClass().getSimpleName();

			switch (className)
			{
				case "PositionComponent":
				{
					PositionComponent entityPos = initialEntity.getComponent(PositionComponent.class);
					entityPos.pos2D.set(((PositionComponent) comp).pos2D);
					break;
				}
				
				case "TerrainComponent":
				{
					TerrainComponent entityTerrain = initialEntity.getComponent(TerrainComponent.class);
					entityTerrain.vertices = ((TerrainComponent)comp).vertices;
					entityTerrain.heightPriority = ((TerrainComponent)comp).heightPriority;
					break;
				}

				case "OwnershipComponent":
				{
					OwnershipComponent entityOwner = initialEntity.getComponent(OwnershipComponent.class);
					entityOwner.owner = ((OwnershipComponent) comp).owner;
					break;
				}

				case "CollectibleBlueprintComponent":
				{
					CollectibleBlueprintComponent entityBlueprint = initialEntity
							.getComponent(CollectibleBlueprintComponent.class);
					entityBlueprint.buildableEntityName = ((CollectibleBlueprintComponent) comp).buildableEntityName;
					entityBlueprint.amount = ((CollectibleBlueprintComponent) comp).amount;
					break;
				}

				case "ResourceComponent":
				{
					ResourceComponent entityResource = initialEntity.getComponent(ResourceComponent.class);

					// TODO 4 might only need to fetch the resource's amount
					entityResource.resource.amount = ((ResourceComponent) comp).resource.amount;
					// entityResource.resource = ((ResourceComponent) comp).resource;
					break;
				}

				case "BuildableComponent":
				{
					BuildableComponent build = initialEntity.getComponent(BuildableComponent.class);

					build.setBuildProgress(((BuildableComponent) comp).getBuildProgress());
					build.setBuildRate(((BuildableComponent) comp).getBuildRate());
					break;
				}

				case "TriggerComponent":
				{
					TriggerComponent trigger = initialEntity.getComponent(TriggerComponent.class);

					trigger.conditions.addAll(((TriggerComponent) comp).conditions);
					trigger.actions.addAll(((TriggerComponent) comp).actions);

					trigger.allConditionsMet = ((TriggerComponent) comp).allConditionsMet;
					trigger.persistant = ((TriggerComponent) comp).persistant;
					break;
				}
			}
		}

		return initialEntity;
	}

	public static JsonValue getEntityConfigFile(String entityFileName)
	{
		return JsonUtility.readJson(ENTITY_CONFIG_PATH + "/" + entityFileName + ".json");
	}
}
