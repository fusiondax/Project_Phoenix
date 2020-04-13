package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.phoenix.screens.GameScreen;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;
	
	private HorizontalGroup selectionDisplayPrimary;
	private Table minimapPrimary;
	
	
	public InGameUI(GameScreen gs)
	{
		super();
		this.gameScreen = gs;
		setupUI();
	}
	
	
	private void setupUI()
	{
		
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
		
		
		this.addActor(minimapPrimary);
	}
}
