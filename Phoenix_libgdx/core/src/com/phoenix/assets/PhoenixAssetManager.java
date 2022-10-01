package com.phoenix.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PhoenixAssetManager
{
	private static PhoenixAssetManager phoenixManager;
	
	public AssetManager manager;
	
	public Skin activeSkin;
	
	private PhoenixAssetManager()
	{
		manager = new AssetManager();
		loadAssets();
	}
	
	/**
	 * maybe this should not be in this class... but I don't know where would be a good place to put it...
	 */
	public void initializeDefaultSkin()
	{
		TextureAtlas texAtlas = manager.get("graphics/atlas/ui.atlas");
		activeSkin = new Skin(texAtlas);
		
		Pixmap windowBackgroundColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		windowBackgroundColor.setColor(Color.NAVY);
		windowBackgroundColor.fill();
		
		Drawable windowBackgroundDrawable = (Drawable) new Image(new Texture(windowBackgroundColor)).getDrawable();
		
		WindowStyle windowStyle = new WindowStyle(new BitmapFont(), Color.WHITE,
				windowBackgroundDrawable);
		
		LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.WHITE);
		
		/*
		Pixmap buttonBackgroundColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		buttonBackgroundColor.setColor(Color.CYAN);
		buttonBackgroundColor.fill();
		*/
		
		TextButtonStyle bStyle = new TextButtonStyle();
		bStyle.up = (Drawable) new Image(activeSkin, "ui_button_up").getDrawable();
		bStyle.down = (Drawable) new Image(activeSkin, "ui_button_down").getDrawable();
		bStyle.font = new BitmapFont();
		bStyle.fontColor = Color.WHITE;
		
		ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.up = (Drawable) new Image(activeSkin, "ui_button_up").getDrawable();
		buttonStyle.down = (Drawable) new Image(activeSkin, "ui_button_down").getDrawable();
		
		activeSkin.add("default_label", labelStyle);
		activeSkin.add("default_text_button", bStyle);
		activeSkin.add("default_button", buttonStyle);
		activeSkin.add("default_ui_background", windowBackgroundDrawable);
		activeSkin.add("default_window", windowStyle);
		
	}
	
	private void loadAssets()
	{
		loadTextures();
	}
	
	private void loadTextures()
	{
		manager.load("graphics/atlas/entities.atlas", TextureAtlas.class);
		manager.load("graphics/atlas/ui.atlas", TextureAtlas.class);
		
		/*
		manager.load("graphics/terrain_grass.png", TextureRegion.class);
		manager.load("graphics/terrain_rocky_grass.png", TextureRegion.class);
		manager.load("graphics/terrain_ground.png", TextureRegion.class);
		manager.load("graphics/terrain_sand.png", TextureRegion.class);
		manager.load("graphics/terrain_water.png", TextureRegion.class);
		
		manager.load("graphics/unit_duck.png", TextureRegion.class);
		
		manager.load("graphics/resource_cog.png", TextureRegion.class);
		*/
	}
	
	public static PhoenixAssetManager getInstance()
	{
		if(phoenixManager == null)
		{
			phoenixManager = new PhoenixAssetManager();
		}
		
		return phoenixManager;
	}

}
