package com.phoenix.screens;

import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.phoenix.blueprint.BlueprintData;
import com.phoenix.game.Phoenix;
import com.phoenix.input.BlueprintInputManager;
import com.phoenix.input.InputManager;
import com.phoenix.io.BlueprintDataLoader;
import com.phoenix.io.JsonUtility;
import com.phoenix.io.MapLoader;
import com.phoenix.player.Player;
import com.phoenix.systems.BlueprintValidationIndicatorRenderSystem;
import com.phoenix.systems.SelectionBoxRenderSystem;
import com.phoenix.systems.entitySystems.BlueprintCollectionSystem;
import com.phoenix.systems.entitySystems.CollisionSystem;
import com.phoenix.systems.entitySystems.EntityBuildingSystem;
import com.phoenix.systems.entitySystems.MoveCommandSystem;
import com.phoenix.systems.entitySystems.VelocitySystem;
import com.phoenix.systems.entitySystems.ResourceEntitySystem;
import com.phoenix.systems.entitySystems.SelectedEntityCircleRenderSystem;
import com.phoenix.systems.entitySystems.TextureRenderSystem;
import com.phoenix.ui.InGameUI;
import com.phoenix.utility.MathUtility;

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
	public HashMap<String, BlueprintData> blueprintData;

	private float timeDilation = 1;

	public GameScreen(Phoenix game)
	{
		this.game = game;

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);

		playerList = new HashMap<String, Player>();
		playerList.put(ACTIVE_PLAYER_NAME, new Player(ACTIVE_PLAYER_NAME));

		blueprintData = new HashMap<String, BlueprintData>();
		loadBlueprintData();

		shapeRendererLine = new ShapeRenderer();
		shapeRendererFilled = new ShapeRenderer();

		guiStage = new InGameUI(new ScreenViewport(), this);

		inputs = new InputMultiplexer(new BlueprintInputManager(this, shapeRendererLine), guiStage,
				new InputManager(this, shapeRendererLine));

		Gdx.input.setInputProcessor(inputs);

		// TODO put this somewhere else
		MapLoader.convertTiledMapToGameMap("maze_alt_2_compatible.tmx", "test_map_write.json");

		engine = new Engine();
		loadGameMap("test_map_write.json");

		// add the systems that manages entities
		engine.addSystem(new TextureRenderSystem(this));
		engine.addSystem(new SelectedEntityCircleRenderSystem(this));
		engine.addSystem(new VelocitySystem(shapeRendererLine));
		engine.addSystem(new MoveCommandSystem(shapeRendererLine));
		engine.addSystem(new CollisionSystem(shapeRendererLine));
		engine.addSystem(new BlueprintCollectionSystem(this));
		engine.addSystem(new EntityBuildingSystem());
		engine.addSystem(new ResourceEntitySystem());

		// add the systems that does not manage entities
		engine.addSystem(new BlueprintValidationIndicatorRenderSystem(this));
		engine.addSystem(new SelectionBoxRenderSystem(this));

		// pauseSystems();
	}

	private void loadBlueprintData()
	{
		BlueprintDataLoader.loadAllDataBlueprint(blueprintData);
	}

	private void loadGameMap(String gameMapFileName)
	{
		MapLoader.addEntitiesToEngine(this.engine, MapLoader.getGameMap(gameMapFileName));
	}

	private void saveGameMap(String gameMapFileName)
	{
		JsonUtility.writeJsonGameMapFile(gameMapFileName, MapLoader.createGameMapFromEngine(this.engine));
	}

	public float getTimeDilation()
	{
		return timeDilation;
	}

	public void setTimeDilation(float timeDilation)
	{
		if (timeDilation < 2.0 && timeDilation > -0.05)
		{
			this.timeDilation = MathUtility.roundFloat(timeDilation);
		}

	}

	public void toggleSystems()
	{
		boolean paused = true;
		for (EntitySystem s : engine.getSystems())
		{
			if (s.checkProcessing())
			{
				paused = false;
				break;
			}
		}

		if (paused)
		{
			resumeSystems();
		}
		else
		{
			pauseSystems();
		}
	}

	public void pauseSystems()
	{
		for (EntitySystem s : engine.getSystems())
		{
			s.setProcessing(false);
		}

		// engine.getSystem(MovementSystem.class).setProcessing(false);
	}

	public void resumeSystems()
	{
		for (EntitySystem s : engine.getSystems())
		{
			s.setProcessing(true);
		}
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		delta = delta * timeDilation;
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

		if (!guiStage.isSetup)
		{
			guiStage.setupUI();
		}

		guiStage.act(delta);
		guiStage.draw();
	}

	@Override
	public void resize(int width, int height)
	{

		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = Gdx.graphics.getWidth();
		guiStage.getViewport().update(width, height, true);
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
