package com.phoenix.blueprint;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Blueprint
{
	public BlueprintData data;

	private int amount;

	public Circle validBuildIndicator;

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

	/**
	 * a blueprint will be valid for the given position on the map if:
	 * 
	 * - The build location is situated on a terrain of valid type for the built
	 * entity (an entity with a TerrainComponent which type is included in the built
	 * entity's ValidTerrainTypesComponent's types list)
	 * 
	 * - The required resources, in sufficient amounts, are located within the built entity's build range 
	 * 
	 * @return
	 */
	public boolean isValid()
	{
		boolean valid = true;

		// is the current terrain a valid terrain type?

		// are all the needed resources included in the build range in sufficient quantities?

		return valid;
	}
}
