package com.phoenix.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.screens.GameScreen;

public class Phoenix extends Game
{
	public static final int TERRAIN_SIZE = 64;
	
	public SpriteBatch gameBatcher;
	//public InputManager inputManager;
	
	public GameScreen gameScreen;

	@Override
	public void create()
	{
		gameBatcher = new SpriteBatch();
		//inputManager = new InputManager();
		//Gdx.input.setInputProcessor(inputManager);
		
		this.gameScreen = new GameScreen(this);
		
	}
	
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		AssetManager manager = PhoenixAssetManager.getInstance().manager;
		if(manager.update())
		{
			if(getScreen() == null)
			{
				setScreen(this.gameScreen);
			}
		}
		else
		{
			System.out.println(manager.getProgress());
		}
		super.render();
	}
	
	@Override
	public void dispose()
	{
		// batch.dispose();
		// img.dispose();
	}
}
