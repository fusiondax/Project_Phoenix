package com.phoenix.input;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.BuildableComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.game.Player;
import com.phoenix.io.EntityLoader;
import com.phoenix.screens.GameScreen;
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
		
		if(player.selectedBlueprint != null)
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
	 * used when we are trying to build a buildable entitiy using the player's selected blueprint at the clicked location
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		boolean handled = false;
		
		if(player.selectedBlueprint != null)
		{
			if(player.selectedBlueprint.isValid())
			{
				OrthographicCamera cam = gameScreen.camera;
				Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
				
				Entity newEntity = EntityLoader.getInitializedEntity(player.selectedBlueprint.data.buildableEntityName);
				
				// update the position to be where the mouse was clicked
				PositionComponent position = newEntity.getComponent(PositionComponent.class);
				position.pos.x = worldPos.x;
				position.pos.y = worldPos.y;
				
				// update the ownership to be the active player
				OwnershipComponent owner = newEntity.getComponent(OwnershipComponent.class);
				owner.owner = player.name;
				
				// update the buildable component to have a positive buildRate
				BuildableComponent buildable = newEntity.getComponent(BuildableComponent.class);
				buildable.setBuildRate(player.selectedBlueprint.data.buildRate);
				
				gameScreen.engine.addEntity(newEntity);
			}
		}
		return handled;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	/**
	 * updates the validBuildIndication's position and color based on the validity of the location where the cursor currently is
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		boolean handled = false;
		
		if(player.selectedBlueprint != null)
		{
			OrthographicCamera cam = gameScreen.camera;
			Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
			
			Circle validBuildIndicator = player.selectedBlueprint.validBuildIndicator;
			
			validBuildIndicator.setPosition(worldPos);
			validBuildIndicator.radius = player.selectedBlueprint.data.buildRange;
		}
		
		return handled;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
