package com.phoenix.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.io.JsonUtility;
import com.phoenix.screens.TitleScreen;

public class TitleMenu extends Stage
{
	private TitleScreen titleScreen;
	
	private MainMenuType activeMenu = null;
	
	public Skin activeSkin;

	public enum MainMenuType
	{
		Title, Options, LevelSelect, LoadGame
	}
	
	public TitleMenu(Viewport port, TitleScreen ts)
	{
		super(port);
		this.titleScreen = ts;
	}
	
	public MainMenuType getActiveMenu()
	{
		return activeMenu;
	}

	public void changeMenu(MainMenuType activeMenu)
	{
		if(activeSkin == null)
		{
			activeSkin = PhoenixAssetManager.getInstance().activeSkin;
		}
		
		super.clear();
		switch (activeMenu)
		{
			case Title:
				buildTitleMenu();
				break;
				
			case Options:
				buildOptionsMenu();
				break;
			
			case LevelSelect:
				buildLevelSelectMenu();
				break;
			
			case LoadGame:
				buildLoadGameMenu();
				break;			
		}
		this.activeMenu = activeMenu;
	}

	private void buildTitleMenu()
	{
		Pixmap mainMenuBackgroundColor = new Pixmap(100, 100, Pixmap.Format.RGB888);
		mainMenuBackgroundColor.setColor(Color.FOREST);
		mainMenuBackgroundColor.fill();
		Drawable mainMenuBackgroundDrawable = (Drawable) new Image(new Texture(mainMenuBackgroundColor)).getDrawable();
		
		//main GUI container
		Table tButtonContainer = new Table();
		tButtonContainer.setBackground(mainMenuBackgroundDrawable);
		
		//creating labels for each of the main menu buttons
		Label lTitle = new Label("Project Phoenix", activeSkin, "default_label");
		lTitle.setFontScale(2.0f);
		
		//creating the Main Menu Button
		TextButton bPlay = new TextButton("Start New Level", activeSkin, "default_text_button");//(lPlayButton, skin, "default_text_button");
		bPlay.getLabelCell().pad(20);
		TextButton bLoadGame = new TextButton("Load Existing Game", activeSkin, "default_text_button");
		bLoadGame.getLabelCell().pad(20);
		TextButton bOptions = new TextButton("Options", activeSkin, "default_text_button");
		bOptions.getLabelCell().pad(20);
		//TODO create icon for the language button
		//ImageButton ibLanguage = new ImageButton(skin);
		TextButton bExitGame = new TextButton("Exit Game", activeSkin, "default_text_button");
		bExitGame.getLabelCell().pad(20);
		
		//adding actions to every button
		bPlay.addListener(new ClickListener()
		{
			public void clicked (InputEvent event, float x, float y) 
			{
				changeMenu(MainMenuType.LevelSelect);
			}
		});
		
		// adding actors to main gui container
		tButtonContainer.add(lTitle).pad(5.0f);
		tButtonContainer.row();
		tButtonContainer.add(bPlay).pad(5.0f);
		tButtonContainer.row();
		tButtonContainer.add(bLoadGame).pad(5.0f);
		tButtonContainer.row();
		tButtonContainer.add(bOptions).pad(5.0f);
		tButtonContainer.row();
		tButtonContainer.add(bExitGame).pad(5.0f);
		tButtonContainer.row();
		
		tButtonContainer.center();
		tButtonContainer.setFillParent(true);
		
		addActor(tButtonContainer);
	}
	
	private void buildLevelSelectMenu()
	{
		Pixmap mainMenuBackgroundColor = new Pixmap(100, 100, Pixmap.Format.RGB888);
		mainMenuBackgroundColor.setColor(Color.FIREBRICK);
		mainMenuBackgroundColor.fill();
		Drawable mainMenuBackgroundDrawable = (Drawable) new Image(new Texture(mainMenuBackgroundColor)).getDrawable();
		
		Table tButtonContainer = new Table();
		tButtonContainer.setBackground(mainMenuBackgroundDrawable);
		
		for(String mapName : JsonUtility.getAllGameMapNames())
		{
			TextButton bLevel = new TextButton(mapName, activeSkin, "default_text_button");
			bLevel.getLabelCell().pad(20);
			
			bLevel.addListener(new ClickListener()
			{
				public void clicked (InputEvent event, float x, float y) 
				{
					titleScreen.game.gameScreen.loadGameMap( ((TextButton)event.getListenerActor()).getText().toString() );
					titleScreen.game.setScreen(titleScreen.game.gameScreen);
				}
			});
			
			tButtonContainer.add(bLevel).pad(5.0f);
			tButtonContainer.row();
		}
		
		tButtonContainer.center();
		tButtonContainer.setFillParent(true);
		
		addActor(tButtonContainer);
	}
	
	private void buildOptionsMenu()
	{
		
	}
	
	private void buildLoadGameMenu()
	{
		
	}
}
