package com.phoenix.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.phoenix.game.Phoenix;
import com.phoenix.ui.TitleMenu;
import com.phoenix.ui.TitleMenu.MainMenuType;

public class TitleScreen extends ScreenAdapter
{
	public Phoenix game;
	public TitleMenu menuStage;
	public OrthographicCamera camera;
	public InputMultiplexer inputs;

	
	public TitleScreen(Phoenix game)
	{
		this.game = game;
		
		this.menuStage = new TitleMenu(new ScreenViewport(), this);
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		
		inputs = new InputMultiplexer(menuStage);

		Gdx.input.setInputProcessor(inputs);
	}

	@Override
	public void show()
	{
		menuStage.changeMenu(MainMenuType.Title);
		Gdx.input.setInputProcessor(inputs);
	}

	@Override
	public void render(float delta)
	{
		menuStage.act(delta);
		menuStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = Gdx.graphics.getWidth();
		menuStage.getViewport().update(width, height, true);
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void dispose()
	{

	}

}
