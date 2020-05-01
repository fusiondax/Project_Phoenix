package com.phoenix.screens;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.game.Phoenix;
import com.phoenix.game.Player;
import com.phoenix.input.InputManager;
import com.phoenix.io.JsonUtility;
import com.phoenix.io.MapLoader;
import com.phoenix.systems.BlueprintCollectionSystem;
import com.phoenix.systems.CollisionSystem;
import com.phoenix.systems.MovementAISystem;
import com.phoenix.systems.MovementSystem;
import com.phoenix.systems.RenderSystem;
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
	
	public ShapeRenderer shapeRenderer;
	
	public HashMap<String, Player> playerList;
	
	public Vector2 prevCamDragPos;
	public Rectangle selectionBox;
	
	public GameScreen(Phoenix game)
	{
		this.game = game;
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		
		playerList = new HashMap<String, Player>();
		playerList.put(ACTIVE_PLAYER_NAME, new Player(ACTIVE_PLAYER_NAME));
		
		guiStage = new InGameUI(this);
		
		inputs = new InputMultiplexer(new InputManager(this), guiStage);
		
		Gdx.input.setInputProcessor(inputs);
		
		//TODO put this somewhere else
		MapLoader.convertTiledMapToGameMap("maze_alt_2_compatible.tmx", "test_map_write.json");
		
		engine = new Engine();
		loadGameMap("test_map_write.json");
		
		selectionBox = new Rectangle();
		shapeRenderer = new ShapeRenderer();
		
		prevCamDragPos = new Vector2();
		
		engine.addSystem(new RenderSystem(this));
		engine.addSystem(new MovementSystem());
		engine.addSystem(new MovementAISystem(shapeRenderer));
		engine.addSystem(new CollisionSystem(shapeRenderer));
		engine.addSystem(new BlueprintCollectionSystem(this));
		
		//engine.addSystem(new MovementSystem());

		//engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderSystem.class).getCamera());

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
		engine.getSystem(MovementSystem.class).setProcessing(false);
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
		shapeRenderer.setProjectionMatrix(camera.combined);
		game.gameBatcher.begin();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		
		engine.update(delta);
		shapeRenderer.rect(selectionBox.x, selectionBox.y, selectionBox.width, selectionBox.height);
		
		game.gameBatcher.end();
		shapeRenderer.end();
		
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
		shapeRenderer.dispose();
		guiStage.dispose();
	}

	

}
