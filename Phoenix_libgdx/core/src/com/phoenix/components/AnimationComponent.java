package com.phoenix.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class AnimationComponent implements Component, Serializable
{
	public HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();

	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		
	}
}
