package com.phoenix.input.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.MoveCommandComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.components.TextureComponent;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.PhoenixCursor;
import com.phoenix.utility.GameWorldUtility;
import com.phoenix.utility.MathUtility;

public class GameWorldInputManager implements InputProcessor
{
	private GameScreen gameScreen;
	private Player player;
	
	public GameWorldInputManager(GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
		this.player = this.gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);
	}

	@Override
	public boolean keyDown(int keycode)
	{
//		System.out.println(Input.Keys.toString(keycode));
		
		boolean handled = false;
		switch(keycode)
		{
			case Input.Keys.W:
			{
				gameScreen.camera.translate(0, 10);
				handled = true;
				break;
			}
			
			case Input.Keys.A:
			{
				gameScreen.camera.translate(-10, 0);
				handled = true;
				break;
			}
			
			case Input.Keys.S:
			{
				gameScreen.camera.translate(0, -10);
				handled = true;
				break;
			}
			
			case Input.Keys.D:
			{
				gameScreen.camera.translate(10, 0);
				handled = true;
				break;
			}
			
			case Input.Keys.P:
			{
				gameScreen.toggleSystems();
				break;
			}
			
			case Input.Keys.PLUS:
			{
				gameScreen.setTimeDilation(gameScreen.getTimeDilation() + 0.1f);
				break;
			}
			
			case Input.Keys.MINUS:
			{
				gameScreen.setTimeDilation(gameScreen.getTimeDilation() - 0.1f);
				break;
			}
			
			case Input.Keys.L:
			{
				gameScreen.setTimeDilation(1);
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
		Vector2 worldPos = GameWorldUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
		//System.out.println("touchDown X: " + screenX);
		//System.out.println("touchDown Y: " + screenY);
		//System.out.println("button: " + MouseButtonConversion.ConvertButtonIntToString(button));
		
		switch(button)
		{
			// left button for unit selection
			case Input.Buttons.LEFT:
			{
				
				
				player.selectionBox.x = worldPos.x;
				player.selectionBox.y = worldPos.y;
				
//				System.out.println("world x: " + worldPos.x);
//				System.out.println("world y: " + worldPos.y);
				
				break;
			}
			
			
			case Input.Buttons.RIGHT:
			{
				
			}
			
			// middle mouse for dragging the screen
			case Input.Buttons.MIDDLE:
			{
				player.prevCamDragPos.set(screenX, screenY);
				break;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		OrthographicCamera cam = gameScreen.camera;
		Vector2 worldPos = GameWorldUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
		//System.out.println("touchUp X: " + screenX);
		//System.out.println("touchUp Y: " + screenY);
		//System.out.println("button: " + MouseButtonConversion.ConvertButtonIntToString(button));
		switch(button)
		{
			// left button for unit selection
			case Input.Buttons.LEFT:
			{
				//defines the height and width of the selection box
				player.selectionBox.width = worldPos.x - player.selectionBox.x;
				player.selectionBox.height = worldPos.y - player.selectionBox.y;
				
				player.selectionBox = MathUtility.readjustRectangle(player.selectionBox);
				
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
					
					//TODO change the way units are selected/being controlled depending on ownership and selection mode
					OwnershipComponent owner = e.getComponent(OwnershipComponent.class);
					
					Vector2 position2d = new Vector2(position.pos2D);
					
//					System.out.println("selectable entity position: " + position2d.toString());
					
					// if the entity's position is contained within the selection rectangle, add it to the selectedEntities list
					if(player.selectionBox.contains(position2d))
					{
						select.selected = true;
						TextureComponent graph = e.getComponent(TextureComponent.class); 
						//System.out.println(graph.textureName);
						gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities.add(e);
					}
					else
					{
						select.selected = false;
					}
				}
				
				player.selectionBox = new Rectangle();
				break;
			}
			
			case Input.Buttons.RIGHT:
			{
				// if there are selectable entities in the player selected entities list
				for(Entity e : gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities)
				{
					MoveCommandComponent mac = e.getComponent(MoveCommandComponent.class);
					PositionComponent pc = e.getComponent(PositionComponent.class);
					if(mac != null && pc != null)
					{
						mac.startPathfindingDelay = MoveCommandComponent.START_PATHFINDING_DELAY_MAX;
						mac.destinations.clear();
						mac.destinations.add(worldPos);
					}
				}
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
		// TODO 2 weird graphical but with dragging left mouse button while a radial menu is present...
		//System.out.println("X: " + screenX);
		//System.out.println("Y: " + screenY);
		OrthographicCamera cam = gameScreen.camera;
		
		boolean handled = false;
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
			Vector2 worldPos = GameWorldUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
			
			player.selectionBox.width = worldPos.x - player.selectionBox.x;
			player.selectionBox.height = worldPos.y - player.selectionBox.y;
			
			//System.out.println("selectionBox: " + gameScreen.selectionBox.toString());
			//gameScreen.selectionBox = MathUtility.readjustRectangle(gameScreen.selectionBox);
			
			handled = true;
		}
		
		if(Gdx.input.isButtonPressed(Input.Buttons.MIDDLE))
		{
			gameScreen.camera.translate(player.prevCamDragPos.x - screenX, screenY - player.prevCamDragPos.y);
			player.prevCamDragPos.set(screenX, screenY);
			handled = true;
		}
		
		return handled;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		gameScreen.game.cursor = PhoenixCursor.Arrow.getCursor();
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
