package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.ui.radialMenu.RadialMenuButton;
import com.badlogic.gdx.utils.JsonValue;

/**
 * An entity with this component has the ability to display a
 * {@link RadialMenu} (usually by right-clicking it) around itself to
 * allow for contextual information or control options for that entity
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
		
	}
}
