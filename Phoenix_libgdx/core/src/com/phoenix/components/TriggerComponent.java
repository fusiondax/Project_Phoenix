package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.trigger.TriggerAction;
import com.phoenix.trigger.TriggerCondition;

public class TriggerComponent implements Component, Serializable
{
	public ArrayList<TriggerCondition> conditions = new ArrayList<TriggerCondition>();
	public ArrayList<TriggerAction> actions = new ArrayList<TriggerAction>();
	
	/**
	 * A persistant trigger will not be removed from the logic engine once all its conditions have been met and all its actions have been executed
	 */
	public boolean persistant = false;
	
	/**
	 * If true, all conditions must be met before the actions can be executed
	 */
	public boolean allConditionsMet = true;

	@Override
	public void write(Json json)
	{
		json.writeArrayStart("conditions");
		for(TriggerCondition tCond : this.conditions)
		{
			json.writeValue(tCond);
		}
		json.writeArrayEnd();
		
		json.writeArrayStart("actions");
		for(TriggerAction tAct : this.actions)
		{
			json.writeValue(tAct);
		}
		json.writeArrayEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		JsonValue list = jsonData.get("conditions");
		
		for (JsonValue entry = list.child; entry != null; entry = entry.next)
		{
			TriggerCondition condition = json.readValue(null, entry);
			conditions.add(condition);
		}
		
		list = jsonData.get("actions");
		for (JsonValue entry = list.child; entry != null; entry = entry.next)
		{
			TriggerAction action = json.readValue(null, entry);
			actions.add(action);
		}
		
		persistant = jsonData.get("persistant").asBoolean();
		allConditionsMet = jsonData.get("all_conditions_met").asBoolean();
	}

}
