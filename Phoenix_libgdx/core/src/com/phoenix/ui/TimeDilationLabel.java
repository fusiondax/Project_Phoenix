package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.phoenix.screens.GameScreen;

public class TimeDilationLabel extends Label
{
	private GameScreen gameScreen;
	
	public TimeDilationLabel(Skin skin, String styleName, GameScreen gs)
	{
		super("", skin, styleName);
		this.gameScreen = gs;
	}
	
	@Override
	public void act(float delta)
	{
		setText("game speed: " + gameScreen.getTimeDilation());
	}
}
