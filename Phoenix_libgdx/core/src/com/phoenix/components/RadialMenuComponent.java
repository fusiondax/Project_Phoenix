package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.ui.radialMenu.RadialMenuButton;
import com.badlogic.gdx.utils.JsonValue;

/**
 * 
 * @author David Janelle
 *
 */
public class RadialMenuComponent implements Component, Serializable
{
	public ArrayList<RadialMenuButton> buttons = new ArrayList<RadialMenuButton>();

	@Override
	public void write(Json json)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		/*
		JsonValue list = jsonData.get("buttons");
		
		if(list != null)
		{
			for (JsonValue entry = list.child; entry != null; entry = entry.next)
			{
				RadialMenuButton rmb = json.readValue(null, entry);
				buttons.add(rmb);
			}
		}
		*/
	}
}
