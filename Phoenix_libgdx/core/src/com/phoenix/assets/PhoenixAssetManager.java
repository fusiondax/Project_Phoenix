package com.phoenix.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PhoenixAssetManager
{
	private static PhoenixAssetManager phoenixManager;
	
	public AssetManager manager;
	
	private PhoenixAssetManager()
	{
		manager = new AssetManager();
		loadAssets();
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
