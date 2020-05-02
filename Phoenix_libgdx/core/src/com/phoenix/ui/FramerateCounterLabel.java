package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FramerateCounterLabel extends Label
{

	public FramerateCounterLabel(CharSequence text, Skin skin)
	{
		super(text, skin, "default_label");
	}
	
	
	@Override
	public void act(float delta)
	{
		setBounds(Gdx.graphics.getWidth() - 75, Gdx.graphics.getHeight() / 2, 0, 0);
		setText("FPS: " + Gdx.graphics.getFramesPerSecond());
	}

}
