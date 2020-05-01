package com.phoenix.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.components.GraphicComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.utility.MathUtility;

public class InputManager implements InputProcessor
{
	public GameScreen gameScreen;
	
	public InputManager(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
	}

	@Override
	public boolean keyDown(int keycode)
	{
//		System.out.println(Input.Keys.toString(keycode));
		
		boolean handled = false;
		switch(Input.Keys.toString(keycode))
		{
			case "W":
			{
				gameScreen.camera.translate(0, 10);
				handled = true;
				break;
			}
			
			case "A":
			{
				gameScreen.camera.translate(-10, 0);
				handled = true;
				break;
			}
			
			case "S":
			{
				gameScreen.camera.translate(0, -10);
				handled = true;
				break;
			}
			
			case "D":
			{
				gameScreen.camera.translate(10, 0);
				handled = true;
				break;
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

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		OrthographicCamera cam = gameScreen.camera;
		//System.out.println("touchDown X: " + screenX);
		//System.out.println("touchDown Y: " + screenY);
		//System.out.println("button: " + MouseButtonConversion.ConvertButtonIntToString(button));
		
		switch(button)
		{
			// left button for unit selection
			case Input.Buttons.LEFT:
			{
				Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
				
				gameScreen.selectionBox.x = worldPos.x;
				gameScreen.selectionBox.y = worldPos.y;
				
//				System.out.println("world x: " + worldPos.x);
//				System.out.println("world y: " + worldPos.y);
				
				break;
			}
			
			// right button for giving commands
			case Input.Buttons.RIGHT:
			{
				//System.out.println(gameScreen.selectedEntities.size());
				Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
				
				
				for(Entity e : gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities)
				{
					MovementAIComponent mac = e.getComponent(MovementAIComponent.class);
					PositionComponent pc = e.getComponent(PositionComponent.class);
					if(mac != null && pc != null)
					{
						mac.startPathfindingDelay = MovementAIComponent.START_PATHFINDING_DELAY_MAX;
						mac.destinations.clear();
						mac.destinations.add(worldPos);
					}
				}
				break;
			}
			
			// middle mouse for dragging the screen
			case Input.Buttons.MIDDLE:
			{
				gameScreen.prevCamDragPos.set(screenX, screenY);
				break;
			}
		}
		
		
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		OrthographicCamera cam = gameScreen.camera;
		//System.out.println("touchUp X: " + screenX);
		//System.out.println("touchUp Y: " + screenY);
		
		//System.out.println("button: " + MouseButtonConversion.ConvertButtonIntToString(button));
		
		switch(button)
		{
			// left button for unit selection
			case Input.Buttons.LEFT:
			{
				//defines the height and width of the selection box
				Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
				
				gameScreen.selectionBox.width = worldPos.x - gameScreen.selectionBox.x;
				gameScreen.selectionBox.height = worldPos.y - gameScreen.selectionBox.y;
				
				gameScreen.selectionBox = MathUtility.readjustRectangle(gameScreen.selectionBox);
				
//				System.out.println("selectionBox: " + gameScreen.selectionBox.toString());
				
				//clear the selected entities list when trying to select new selctables
				gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities.clear();
				
				//find "selectable" entities that are included in the rectangle
				ImmutableArray<Entity> selectableEntities = gameScreen.engine.getEntitiesFor(
						Family.all(SelectionComponent.class, PositionComponent.class).get());
				
				for(Entity e : selectableEntities)
				{
					SelectionComponent select = e.getComponent(SelectionComponent.class);
					PositionComponent position = e.getComponent(PositionComponent.class);
					
					//TODO change the way units are selected/being controlled depending on ownership and selction mode
					OwnershipComponent owner = e.getComponent(OwnershipComponent.class);
					
					Vector2 position2d = new Vector2(position.pos.x, position.pos.y);
					
//					System.out.println("selectable entity position: " + position2d.toString());
					
					// if the entity's position is contained within the selection rectangle, add it to the selectedEntities list
					if(gameScreen.selectionBox.contains(position2d))
					{
						select.selected = true;
						GraphicComponent graph = e.getComponent(GraphicComponent.class); 
						System.out.println(graph.textureName);
						gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities.add(e);
					}
					else
					{
						select.selected = false;
					}
				}
				
				gameScreen.selectionBox = new Rectangle();
				
				break;
			}
			
			// right button for giving commands
			case Input.Buttons.RIGHT:
			{
				
				
				break;
			}
			
			// middle mouse for dragging the screen
			case Input.Buttons.MIDDLE:
			{
				break;
			}
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		//System.out.println("X: " + screenX);
		//System.out.println("Y: " + screenY);
		OrthographicCamera cam = gameScreen.camera;
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
			Vector2 worldPos = MathUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
			
			gameScreen.selectionBox.width = worldPos.x - gameScreen.selectionBox.x;
			gameScreen.selectionBox.height = worldPos.y - gameScreen.selectionBox.y;
			
			//System.out.println("selectionBox: " + gameScreen.selectionBox.toString());
			
			//gameScreen.selectionBox = MathUtility.readjustRectangle(gameScreen.selectionBox);
		}
		
		if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE))
		{
			gameScreen.camera.translate(gameScreen.prevCamDragPos.x - screenX, screenY - gameScreen.prevCamDragPos.y);
			gameScreen.prevCamDragPos.set(screenX, screenY);
		}
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		
		float prevViewportHeight = gameScreen.camera.viewportHeight;
		float prevViewportWidth = gameScreen.camera.viewportWidth;
		
		if(amount > 0)
		{
			gameScreen.camera.viewportWidth = gameScreen.camera.viewportWidth * 1.1f;
			gameScreen.camera.viewportHeight = gameScreen.camera.viewportHeight * 1.1f;
		}
		else
		{
			gameScreen.camera.viewportWidth = gameScreen.camera.viewportWidth * 0.9f;
			gameScreen.camera.viewportHeight = gameScreen.camera.viewportHeight * 0.9f;
		}
			
		
		if(gameScreen.camera.viewportHeight < Gdx.graphics.getHeight() * 0.3 || gameScreen.camera.viewportHeight > Gdx.graphics.getHeight() * 2)
		{
			gameScreen.camera.viewportWidth = prevViewportWidth;
			gameScreen.camera.viewportHeight = prevViewportHeight;
		}
		
		return false;
	}
}
