package com.phoenix.input;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.blueprint.Blueprint.BlueprintBuildStatus;
import com.phoenix.components.BuildableComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.ResourceComponent;
import com.phoenix.io.EntityLoader;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.player.Player;
import com.phoenix.resource.Resource;
import com.phoenix.screens.GameScreen;
import com.phoenix.utility.GameWorldUtility;
import com.phoenix.utility.MathUtility;

public class BlueprintInputManager implements InputProcessor
{
	private GameScreen gameScreen;
	private Player player;

	public BlueprintInputManager(GameScreen gameScreen, ShapeRenderer renderer)
	{
		this.gameScreen = gameScreen;
		this.player = this.gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		boolean handled = false;

		if (player.selectedBlueprint != null)
		{
			// TODO bind this key to the future keybind map when configurable keys becomes a
			// thing

			// this key press cancels the "build mode"
			if (Input.Keys.ESCAPE == keycode)
			{
				player.selectedBlueprint = null;
			}
		}
		return handled;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	/**
	 * used when we are trying to build a buildable entitiy using the player's
	 * selected blueprint at the clicked location
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		boolean handled = false;

		switch (button)
		{
			case Input.Buttons.LEFT:
			{
				if (player.selectedBlueprint != null)
				{
					OrthographicCamera cam = gameScreen.camera;
					Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
					Entity buildingEntity = EntityLoader
							.getInitializedEntity(player.selectedBlueprint.data.buildableEntityName);

					player.selectedBlueprint.validate(gameScreen.engine, worldPos, buildingEntity);

					if (player.selectedBlueprint.getValidity() == BlueprintBuildStatus.Valid)
					{
						// remove the resources that were consumed during the building process

						ArrayList<Resource> proxyResources = new ArrayList<Resource>();

						for (Entity e : GameWorldUtility.getProxyEntities(gameScreen.engine, worldPos, player.selectedBlueprint.data.buildRange,
								Family.all(ResourceComponent.class).get()))
						{
							proxyResources.add(e.getComponent(ResourceComponent.class).resource);
						}

						// ArrayList<Entity> proxyResourceEntities = detector.getProxyEntities(worldPos,
						// player.selectedBlueprint.data.buildRange,
						// Family.all(ResourceComponent.class).get());

						// we do not want to modify the selected blueprint's resource list, so we will
						// create a copy of that list instead
						ArrayList<Resource> usedResourceList = new ArrayList<Resource>();
						for (Resource requiredRes : player.selectedBlueprint.data.resourceList)
						{
							usedResourceList.add(new Resource(requiredRes.type, requiredRes.amount));
						}

						for (Resource requiredRes : usedResourceList)
						{
							// find a corresponding resource in the proxy resource list
							for (Resource proxyRes : proxyResources)
							{
								// is the proxy resource of the right type?
								if (proxyRes.type.equals(requiredRes.type))
								{
									if (proxyRes.amount >= requiredRes.amount)
									{
										proxyRes.amount -= requiredRes.amount;
										requiredRes.amount = 0;
									}
									else
									{
										requiredRes.amount -= proxyRes.amount;
										proxyRes.amount = 0;
									}
								}
							}
						}

						// update the position to be where the mouse was clicked
						PositionComponent position = buildingEntity.getComponent(PositionComponent.class);
						position.pos2D.set(worldPos);

						// update the ownership to be the active player
						OwnershipComponent owner = buildingEntity.getComponent(OwnershipComponent.class);
						owner.owner = player.name;

						// update the buildable component to have a positive buildRate
						BuildableComponent buildable = buildingEntity.getComponent(BuildableComponent.class);
						// TODO find a more suitable way to get this value
						buildable.setBuildProgress(BuildableComponent.NEW_BUILD_BUILD_PROGRESS_START_VALUE);
						buildable.setBuildRate(player.selectedBlueprint.data.buildRate);

						// add the newly built entity into the engine
						gameScreen.engine.addEntity(buildingEntity);

						// dispense a charge of the blueprint used by the player's
						player.dispenseBlueprintFromCollection(player.selectedBlueprint);

						handled = true;
					}
				}
				break;
			}
			case Input.Buttons.RIGHT:
			{
				player.selectedBlueprint = null;
				break;
			}
		}

		return handled;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	/**
	 * updates the validBuildIndication's position and color based on the validity
	 * of the location where the cursor currently is
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		boolean handled = false;

		if (player.selectedBlueprint != null)
		{
			OrthographicCamera cam = gameScreen.camera;
			Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);

			Circle validBuildIndicator = player.selectedBlueprint.validBuildIndicator;

			validBuildIndicator.setPosition(worldPos);
			validBuildIndicator.radius = player.selectedBlueprint.data.buildRange;

			Entity buildingEntity = EntityLoader
					.getInitializedEntity(player.selectedBlueprint.data.buildableEntityName);

			player.selectedBlueprint.validate(gameScreen.engine, worldPos, buildingEntity);
		}
		return handled;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
