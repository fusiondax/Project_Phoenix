package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class FramerateCounterLabel extends Label
{

	public FramerateCounterLabel(CharSequence text, LabelStyle style)
	{
		super(text, style);
	}
	
	
	@Override
	public void act(float delta)
	{
		setBounds(Gdx.graphics.getWidth() - 75, Gdx.graphics.getHeight() / 2, 0, 0);
		setText("FPS: " + Gdx.graphics.getFramesPerSecond());
	}

}
