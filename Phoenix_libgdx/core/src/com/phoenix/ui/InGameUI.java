package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.phoenix.screens.GameScreen;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;
	
	private Label framerateCounter;
	private HorizontalGroup selectionDisplayPrimary;
	private Table minimapPrimary;
	private BlueprintBar blueprintBar;
	
	
	public InGameUI(GameScreen gs)
	{
		super();
		this.gameScreen = gs;
		setupUI();
	}
	
	
	private void setupUI()
	{
		// create the framerate counter
		
		framerateCounter = new FramerateCounterLabel("test", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		
		// create the minimap widget	
		minimapPrimary = new Table();
		minimapPrimary.setDebug(true);
		
		Image terrainDisplay = new MinimapTerrainImage();
		
		minimapPrimary.setBounds(0, 0, 200, 200);
		
		minimapPrimary.setDebug(true);
		
		// create the selection display widget
		
		HorizontalGroup selectionDisplayPrimary = new HorizontalGroup();
		//selectionDisplayPrimary.setBounds(x, y, width, height);
		
		VerticalGroup selectionDisplayUnitResource = new VerticalGroup();
		
		VerticalGroup selectionDisplayUnitOverview = new VerticalGroup();
		VerticalGroup selectionDisplayUnitDetail = new VerticalGroup();
		
		//selectionDisplayPrimary.addActor();
		
		selectionDisplayPrimary.setDebug(true);
		selectionDisplayUnitResource.setDebug(true);
		
		// create the units ability list widget
		
		
		// create the blueprint bar widget
		blueprintBar = new BlueprintBar(gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME));
		//blueprintBar.setTouchable(Touchable.enabled);
		blueprintBar.debug();
		
		addActor(blueprintBar);
		//this.addActor(minimapPrimary);
		addActor(framerateCounter);
		
		// TODO I don't like that only one actor has the keyboard focus... might want to think of something else here...
		setKeyboardFocus(blueprintBar);
	}
}
