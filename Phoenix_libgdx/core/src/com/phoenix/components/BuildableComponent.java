package com.phoenix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.blueprint.BlueprintData;

public class BuildableComponent implements Component, Serializable
{
	
	public static final float NEW_BUILD_BUILD_PROGRESS_START_VALUE = 0.01f;
	// TODO the component might not even need to have the blueprint data
	//public BlueprintData data;

	/**
	 * this is a mesure of how much the entity is built. The value goes from 0.0 to
	 * 1.0 (or from 0% to 100%).
	 * 
	 * an entity with a buildProgress of 0.0 is meant to be salvaged (the unit is
	 * removed from the game but its resources are salvaged successfully).
	 * 
	 * an entity with a buildProgress of 1.0 is finished building and is fully
	 * operationnal. Its various AI components will start functionning and its
	 * command components will respoond to commands
	 * 
	 * an entity with a buildProgress anywhere between 0.0 and 1.0 is being
	 * constructed. Its Command components will be unresponsive and its AI
	 * components will be inoperative
	 */
	private float buildProgress = 1.0f;

	/**
	 * the build rate is added to the buildProgress at every game update and should
	 * be the only value that affects the buildProgress. its value can vary between
	 * -1.0 and 1.0.
	 * 
	 * a positive value means the entity is being built.
	 * 
	 * a negative value means the entity is being salvaged.
	 * 
	 * once the entity is completely salvaged or is finished building, the buildRate
	 * should be set to 0
	 */
	private float buildRate = 0.0f;

	@Override
	public void write(Json json)
	{
		json.writeValue("build_progress", buildProgress);
		json.writeValue("build_rate", buildRate);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		buildProgress = jsonData.get("build_progress").asFloat();
		buildRate = jsonData.get("build_rate").asFloat();
	}

	public float getBuildRate()
	{
		return buildRate;
	}

	public void setBuildRate(float buildRate)
	{
		if(buildRate > 1.0)
		{
			this.buildRate = 1.0f;
		}
		else if(buildRate < -1.0)
		{
			this.buildRate = -1.0f;
		}
		else
		{
			this.buildRate = buildRate;
		}
	}

	public float getBuildProgress()
	{
		return buildProgress;
	}

	public void setBuildProgress(float buildProgress)
	{
		if(buildProgress > 1.0)
		{
			this.buildProgress = 1.0f;
		}
		else if(buildProgress < 0.0)
		{
			this.buildProgress = 0.0f;
		}
		else
		{
			this.buildProgress = buildProgress;
		}
	}
}