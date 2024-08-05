package com.phoenix.ui.radialMenu;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * 
 * @author David Janelle
 *
 */
public class RadialMenuButton implements Serializable
{
	public String iconName = "";
	public String type = "";
	
	public RadialMenuButton()
	{
		
	}
	
	public RadialMenuButton(String typeName)
	{
		this(typeName, "");
	}
	
	public RadialMenuButton(String typeName, String iconName)
	{
		this.type = typeName;
		this.iconName = iconName;
	}

	@Override
	public void write(Json json)
	{
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.type = jsonData.get("type").asString();
		this.iconName = jsonData.get("icon_name").asString();
	}
}