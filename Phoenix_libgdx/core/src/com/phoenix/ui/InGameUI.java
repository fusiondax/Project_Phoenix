package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.screens.GameScreen;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;

	public PhoenixWindowBuilder builder;

	public boolean isSetup = false;

	public InGameUI(Viewport port, GameScreen gs)
	{
		super(port);
		this.gameScreen = gs;
	}

	public void setupUI()
	{
		Skin skin = PhoenixAssetManager.getInstance().activeSkin;
		
		// initialize primary actors
		
		builder = new PhoenixWindowBuilder(skin, "default_label", "default_window", gameScreen);
		
		WindowOpenerBar wob = new WindowOpenerBar(skin);
		
		addActor(wob);
		
//		addActor(builder.getWindow("misc_info"));
//		addActor(builder.getWindow("blueprint_bar"));

		isSetup = true;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}
}
