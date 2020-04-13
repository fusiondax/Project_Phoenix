package com.phoenix.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phoenix.input.InputManager;
import com.phoenix.io.JsonUtility;
import com.phoenix.screens.GameScreen;

public class Phoenix extends Game
{
	public SpriteBatch gameBatcher;
	//public InputManager inputManager;

	@Override
	public void create()
	{
		gameBatcher = new SpriteBatch();
		//inputManager = new InputManager();
		//Gdx.input.setInputProcessor(inputManager);
		setScreen(new GameScreen(this));
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		
	}

	@Override
	public void dispose()
	{
		// batch.dispose();
		// img.dispose();
	}
}
