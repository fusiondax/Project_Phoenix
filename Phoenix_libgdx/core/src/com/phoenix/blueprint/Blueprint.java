package com.phoenix.blueprint;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.ResourceComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.ValidTerrainTypesComponent;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.resource.Resource;
import com.phoenix.utility.GameWorldUtility;

public class Blueprint
{
	public BlueprintData data;

	public Circle validBuildIndicator;

	private int amount;
	private BlueprintBuildStatus validity = BlueprintBuildStatus.InvalidTerrain;

	public Blueprint(BlueprintData data, int amount)
	{
		this.data = data;
		this.amount = amount;
		this.validBuildIndicator = new Circle();
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		if (amount > 0)
		{
			this.amount = amount;
		}
	}

	public void addAmount(int amount)
	{
		this.amount += amount;
	}

	public void subAmount(int amount)
	{
		this.amount -= amount;
	}

	public boolean isEquivalent(Blueprint blueprint)
	{
		return data.buildableEntityName.equals(blueprint.data.buildableEntityName);
	}

	public BlueprintBuildStatus getValidity()
	{
		return this.validity;
	}

	/**
	 * a blueprint will be valid for the given position on the map if:
	 * 
	 * - The build location is situated on a terrain of valid type for the built
	 * entity (an entity with a TerrainComponent which type is included in the built
	 * entity's ValidTerrainTypesComponent's types list)
	 * 
	 * - The required resources, in sufficient amounts, are located within the built
	 * entity's build range
	 * 
	 * @return
	 */
	public void validate(Engine engine, Vector2 worldPos, Entity entity)
	{
		if (!isTerrainValid(engine, worldPos, entity))
		{
			this.validity = BlueprintBuildStatus.InvalidTerrain;
		}
		else if (!areResourcesValid(engine, worldPos, entity))
		{
			this.validity = BlueprintBuildStatus.MissingResources;
		}
		else
		{
			this.validity = BlueprintBuildStatus.Valid;
		}
	}

	private boolean isTerrainValid(Engine engine, Vector2 worldPos, Entity entity)
	{
		boolean validTerrain = false;

		TerrainComponent terrainComp = GameWorldUtility.getEntityAtLocation(engine, worldPos, Family.all(TerrainComponent.class).get())
				.getComponent(TerrainComponent.class);

		if (terrainComp != null)
		{
			ValidTerrainTypesComponent vttc = entity.getComponent(ValidTerrainTypesComponent.class);

			if (vttc != null)
			{
				String terrainType = terrainComp.type;
				for (String type : vttc.types)
				{
					if (type.equals(terrainType))
					{
						validTerrain = true;
						break;
					}
				}
			}
			else // if the unit doesn't have a validTerrainTypeComponent, we consider that the
					// unit has no restrictions
			{
				validTerrain = true;
			}
		}
		// if no terrain were found at that location, we assume it is in the "void",
		// which should always be an invalid location

		return validTerrain;
	}

	private boolean areResourcesValid(Engine engine, Vector2 worldPos, Entity entity)
	{
		boolean validResources = true;

		// get all the resources included within the build range. similar typed
		// resources will have their
		// amounts added and the resource will be added only once in the list

		ArrayList<Resource> proxyResources = new ArrayList<Resource>();

		for (Entity e : GameWorldUtility.getProxyEntities(engine, worldPos, data.buildRange, Family.all(ResourceComponent.class).get()))
		{
			Resource temp = e.getComponent(ResourceComponent.class).resource;
			Resource proxyRes = new Resource(temp.type, temp.amount);
			boolean duplicate = false;

			for (Resource storedProxyRes : proxyResources)
			{
				if (storedProxyRes.type.equals(proxyRes.type))
				{
					duplicate = true;
					storedProxyRes.amount += proxyRes.amount;
					break;
				}
			}

			if (!duplicate)
			{
				proxyResources.add(proxyRes);
			}
		}

		// for every resource listed in the required resource list
		for (Resource requiredRes : data.resourceList)
		{
			boolean resTypeValid = false;
			// find a corresponding resource in the proxy resource list
			for (Resource proxyRes : proxyResources)
			{
				// is the proxy resource of the right type?
				if (proxyRes.type.equals(requiredRes.type))
				{
					// is the amount sufficient?
					if (proxyRes.amount >= requiredRes.amount)
					{
						resTypeValid = true;
						break;
					}
				}
			}

			if (!resTypeValid)
			{
				validResources = false;
				break;
			}
		}

		// System.out.println("proxy resources: " + proxyResources);

		return validResources;
	}

	public enum BlueprintBuildStatus
	{
		Valid, MissingResources, InvalidTerrain
	}
}
