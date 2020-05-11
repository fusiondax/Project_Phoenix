package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FramerateCounterLabel extends Label
{
	public FramerateCounterLabel(Skin skin, String styleName)
	{
		super("waddup", skin, styleName);
	}
	
	@Override
	public void act(float delta)
	{
		setText("FPS: " + Gdx.graphics.getFramesPerSecond());
	}
}
