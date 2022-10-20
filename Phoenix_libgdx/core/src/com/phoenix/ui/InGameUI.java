package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.payne.games.piemenu.PieMenu;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.window.PhoenixWindowBuilder;
import com.phoenix.ui.window.WindowOpenerBar;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;

	public PhoenixWindowBuilder builder;
	
	private boolean isSetup = false;
	
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
		
		this.isSetup = true;
		// addActor(builder.getWindow("misc_info"));
		// addActor(builder.getWindow("blueprint_bar"));
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	public boolean isSetup()
	{
		return isSetup;
	}

	
	// TODO 1 this is pretty gross way to check if a radial menu is already being display. recommend finding a cleverer way of determining
	/**
	 * @return true if this stage has at least one actor of type {@link PieMenu} in his actors list
	 */
	public boolean hasRadialMenu()
	{
		boolean hasRadialMenu = false;
		
		for(Actor a : getActors())
		{
			if(a instanceof PieMenu)
			{
				hasRadialMenu = true;
				break;
			}
		}
		return hasRadialMenu;
	}
}
