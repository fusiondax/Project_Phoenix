package com.phoenix.blueprint;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.game.Resource;

public class BlueprintData implements Serializable
{
	public String buildableEntityName;
	public float buildRate;
	public float salvageRate;
	public float buildRange;
	
	public ArrayList<Resource> resourceList = new ArrayList<Resource>();

	public BlueprintData()
	{
		this("", 0, 0, 1, new ArrayList<Resource>());
	}
	
	public BlueprintData(String buildableEntityName, int buildTime, int salvageTime, float buildRange,
			ArrayList<Resource> resourceList)
	{
		this.buildableEntityName = buildableEntityName;
		this.buildRate = buildTime;
		this.salvageRate = salvageTime;
		this.buildRange = buildRange;
		this.resourceList = resourceList;
	}

	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.buildableEntityName = jsonData.get("buildableEntityName").asString();
		this.buildRate = jsonData.get("buildTime").asFloat();
		this.salvageRate = jsonData.get("salvageTime").asFloat();
		this.buildRange = jsonData.get("buildRange").asFloat();
		
		JsonValue resources = jsonData.get("resourceList").child;
		while(resources != null)
		{
			Resource r = json.fromJson(Resource.class, resources.toString());
			resourceList.add(r);
			resources = resources.next();
		}
	}
}
