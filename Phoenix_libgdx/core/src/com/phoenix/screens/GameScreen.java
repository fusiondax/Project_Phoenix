package com.phoenix.screens;

import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.phoenix.systems.entitySystems.ParticleRenderSystem;
import com.phoenix.systems.entitySystems.ResourceAmountCounterRenderSystem;
import com.phoenix.blueprint.BlueprintData;
import com.phoenix.components.TriggerComponent;
import com.phoenix.game.Phoenix;
import com.phoenix.input.manager.BlueprintInputManager;
import com.phoenix.input.manager.GameWorldInputManager;
import com.phoenix.input.manager.RadialMenuInputManager;
import com.phoenix.io.BlueprintDataLoader;
import com.phoenix.io.JsonUtility;
import com.phoenix.io.MapLoader;
import com.phoenix.player.Player;
import com.phoenix.systems.BlueprintValidationIndicatorRenderSystem;
import com.phoenix.systems.CursorDisplaySystem;
import com.phoenix.systems.SelectionBoxRenderSystem;
import com.phoenix.systems.entitySystems.AnimationSystem;
import com.phoenix.systems.entitySystems.BlueprintCollectionSystem;
import com.phoenix.systems.entitySystems.CollisionSystem;
import com.phoenix.systems.entitySystems.EntityActionSystem;
import com.phoenix.systems.entitySystems.EntityBuildingSystem;
import com.phoenix.systems.entitySystems.VelocitySystem;
import com.phoenix.trigger.TriggerCondition;
import com.phoenix.trigger.UnitAtPositionCondition;
import com.phoenix.systems.entitySystems.ResourceEntitySystem;
import com.phoenix.systems.entitySystems.SelectedEntityCircleRenderSystem;
import com.phoenix.systems.entitySystems.TextureRenderSystem;
import com.phoenix.systems.entitySystems.TriggerSystem;
import com.phoenix.ui.InGameUI;
import com.phoenix.utility.MathUtility;

public class GameScreen extends ScreenAdapter
{
	// TODO 3 revamp this
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

		inputs = new InputMultiplexer(new BlueprintInputManager(this), guiStage, new RadialMenuInputManager(this),
				new GameWorldInputManager(this));

		Gdx.input.setInputProcessor(inputs);

		engine = new Engine();

		// add the systems that manages entities
		// engine.addSystem(new AnimationSystem());

		engine.addSystem(new TextureRenderSystem(this));
		engine.addSystem(new SelectedEntityCircleRenderSystem(this));
		engine.addSystem(new VelocitySystem(shapeRendererLine));
		engine.addSystem(new CollisionSystem(shapeRendererLine));
		engine.addSystem(new EntityActionSystem());
		engine.addSystem(new BlueprintCollectionSystem(this));
		engine.addSystem(new EntityBuildingSystem());
		engine.addSystem(new ResourceEntitySystem());
		engine.addSystem(new ResourceAmountCounterRenderSystem(this));
		engine.addSystem(new TriggerSystem(this));
		engine.addSystem(new ParticleRenderSystem(this));
		

		// add the systems that does not manage entities
		engine.addSystem(new BlueprintValidationIndicatorRenderSystem(this));
		engine.addSystem(new SelectionBoxRenderSystem(this));
		engine.addSystem(new CursorDisplaySystem(this));
		
		
		for(EntitySystem s : engine.getSystems())
		{
			System.out.println(s.toString() + " priority: " + s.priority);
		}
	}

	private void loadBlueprintData()
	{
		BlueprintDataLoader.loadAllBlueprintData(blueprintData);
	}

	public void loadGameMap(String gameMapFileName)
	{
		MapLoader.addEntitiesToEngine(this.engine, MapLoader.getGameMap(gameMapFileName));
	}

	public void saveGameMap(String gameMapFileName)
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
		Gdx.input.setInputProcessor(this.inputs);
	}

	@Override
	public void render(float delta)
	{
		delta = delta * timeDilation;
		camera.update();

		game.gameBatcher.setProjectionMatrix(camera.combined);
		
		// TODO 4 is there a better way to do this? Because they technically get called
		// again (but skip most of their work intensive code, as they check if the
		// batcher is active or not before updating
		
		game.gameBatcher.begin();
		engine.getSystem(TextureRenderSystem.class).update(delta);
		engine.getSystem(ParticleRenderSystem.class).update(delta);
		engine.getSystem(ResourceAmountCounterRenderSystem.class).update(delta);
		game.gameBatcher.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRendererLine.setProjectionMatrix(camera.combined);
		shapeRendererFilled.setProjectionMatrix(camera.combined);

		shapeRendererLine.begin(ShapeType.Line);
		shapeRendererFilled.begin(ShapeType.Filled);

		// TODO 3 debugging code, remove ASAP
		shapeRendererLine.setColor(Color.GOLD);

		for (Entity e : engine.getEntitiesFor(Family.all(TriggerComponent.class).get()))
		{
			TriggerComponent tp = e.getComponent(TriggerComponent.class);
			for (TriggerCondition tc : tp.conditions)
			{
				if (tc instanceof UnitAtPositionCondition)
				{
					UnitAtPositionCondition cond = ((UnitAtPositionCondition) tc);
					shapeRendererLine.circle(cond.targetPosition.x, cond.targetPosition.y, cond.targetRadius);
				}
			}
		}

		engine.update(delta);

		shapeRendererLine.end();
		shapeRendererFilled.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (!guiStage.isSetup())
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
