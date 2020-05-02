package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.screens.GameScreen;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;
	private Skin skin;

	private Label framerateCounter;
	private HorizontalGroup selectionDisplayPrimary;
	private Table minimapPrimary;
	private BlueprintBar blueprintBar;

	public boolean isSetup = false;
	
	public InGameUI(GameScreen gs)
	{
		super();
		this.gameScreen = gs;
	}
	
	public void setupUI()
	{
		// initialize the skin
		
		AssetManager manager = PhoenixAssetManager.getInstance().manager;
		TextureAtlas texAtlas = manager.get("graphics/atlas/entities.atlas");
		skin = new Skin(texAtlas);
		
		ImageTextButtonStyle imgTextBtnStyle = new ImageTextButtonStyle();
		imgTextBtnStyle.font = new BitmapFont();
		imgTextBtnStyle.fontColor = Color.WHITE;
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont();
		labelStyle.fontColor = Color.WHITE;
		
		skin.add("default_image_text_button", imgTextBtnStyle);
		skin.add("default_label", labelStyle);
		
		// create the framerate counter
		
		framerateCounter = new FramerateCounterLabel("test", skin);
		
		
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
		blueprintBar = new BlueprintBar(skin, gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME));
		//blueprintBar.setTouchable(Touchable.enabled);
		blueprintBar.debug();
		
		addActor(blueprintBar);
		//this.addActor(minimapPrimary);
		addActor(framerateCounter);
		
		// TODO I don't like that only one actor has the keyboard focus... might want to think of something else here...
		setKeyboardFocus(blueprintBar);
		
		isSetup = true;
	}
}
