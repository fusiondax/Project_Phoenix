package com.phoenix.screens;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.game.Phoenix;
import com.phoenix.game.Player;
import com.phoenix.input.BlueprintInputManager;
import com.phoenix.input.InputManager;
import com.phoenix.io.JsonUtility;
import com.phoenix.io.MapLoader;
import com.phoenix.systems.BlueprintValidationIndicatorRenderSystem;
import com.phoenix.systems.SelectionBoxRenderSystem;
import com.phoenix.systems.entitySystems.BlueprintCollectionSystem;
import com.phoenix.systems.entitySystems.CollisionSystem;
import com.phoenix.systems.entitySystems.MovementAISystem;
import com.phoenix.systems.entitySystems.MovementSystem;
import com.phoenix.systems.entitySystems.SelectedEntityCircleRenderSystem;
import com.phoenix.systems.entitySystems.TextureRenderSystem;
import com.phoenix.ui.InGameUI;

public class GameScreen extends ScreenAdapter
{
	// TODO revamp this
	public static final String ACTIVE_PLAYER_NAME = "Player1";
	
	public Phoenix game;

	public InGameUI guiStage;
	
	public OrthographicCamera camera;
	public InputMultiplexer inputs;
	
	public Engine engine;
	
	public ShapeRenderer shapeRendererLine;
	public ShapeRenderer shapeRendererFilled;
	
	public HashMap<String, Player> playerList;
	
	public GameScreen(Phoenix game)
	{
		this.game = game;
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		
		playerList = new HashMap<String, Player>();
		playerList.put(ACTIVE_PLAYER_NAME, new Player(ACTIVE_PLAYER_NAME));
		
		shapeRendererLine = new ShapeRenderer();
		shapeRendererFilled = new ShapeRenderer();
		
		guiStage = new InGameUI(this);
		
		inputs = new InputMultiplexer(new BlueprintInputManager(this, shapeRendererLine), new InputManager(this, shapeRendererLine), guiStage);
		
		Gdx.input.setInputProcessor(inputs);
		
		//TODO put this somewhere else
		MapLoader.convertTiledMapToGameMap("maze_alt_2_compatible.tmx", "test_map_write.json");
		
		engine = new Engine();
		loadGameMap("test_map_write.json");
		
		// add the systems that manages entities
		engine.addSystem(new TextureRenderSystem(this));
		engine.addSystem(new SelectedEntityCircleRenderSystem(this));
		engine.addSystem(new MovementSystem());
		engine.addSystem(new MovementAISystem(shapeRendererLine));
		engine.addSystem(new CollisionSystem(shapeRendererLine));
		engine.addSystem(new BlueprintCollectionSystem(this));
		
		// add the systems that does not manage entities
		engine.addSystem(new BlueprintValidationIndicatorRenderSystem(this));
		engine.addSystem(new SelectionBoxRenderSystem(this));
		
		//pauseSystems();
	}
	
	private void loadGameMap(String gameMapFileName)
	{
		MapLoader.addEntitiesToEngine(this.engine, MapLoader.getGameMap(gameMapFileName));
	}
	
	private void saveGameMap(String gameMapFileName)
	{
		JsonUtility.writeJsonGameMapFile(gameMapFileName, MapLoader.createGameMapFromEngine(this.engine));
	}
	
	private void pauseSystems()
	{
//		engine.getSystem(MovementSystem.class).setProcessing(false);
	}

	private void resumeSystems()
	{

	}
	
	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		camera.update();
		
		game.gameBatcher.setProjectionMatrix(camera.combined);
		game.gameBatcher.begin();
		engine.getSystem(TextureRenderSystem.class).update(delta);
		game.gameBatcher.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRendererLine.setProjectionMatrix(camera.combined);
		shapeRendererFilled.setProjectionMatrix(camera.combined);
		
		shapeRendererLine.begin(ShapeType.Line);
		shapeRendererFilled.begin(ShapeType.Filled);
		
		engine.update(delta);
		
		shapeRendererLine.end();
		shapeRendererFilled.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		if(!guiStage.isSetup)
		{
			guiStage.setupUI();
		}
		
		guiStage.act(delta);
		guiStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		guiStage.getViewport().update(width, height, true);
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = Gdx.graphics.getWidth();
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
		shapeRendererLine.dispose();
		guiStage.dispose();
	}

	

}
