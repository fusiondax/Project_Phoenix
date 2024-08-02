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
import com.phoenix.components.EntityActionsComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.ResourceComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.components.TextureComponent;
import com.phoenix.entityAction.CarryResourceEntityAction;
import com.phoenix.entityAction.CarryResourceEntityActionParameters;
import com.phoenix.entityAction.EntityAction;
import com.phoenix.entityAction.EntityActionKnownType;
import com.phoenix.entityAction.EntityActionParameters;
import com.phoenix.entityAction.MoveEntityAction;
import com.phoenix.entityAction.MoveEntityActionParameters;
import com.phoenix.player.CursorMode;
import com.phoenix.player.Player;
import com.phoenix.resource.Resource;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.cursor.PhoenixCursor;
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
		switch (keycode)
		{
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
		switch (keycode)
		{
			// player's selected entities's stop movement command
			case Input.Keys.S:
			{
				for (Entity e : player.selectedEntities)
				{
					EntityAction ea = e.getComponent(EntityActionsComponent.class).actions
							.get(EntityActionKnownType.MoveSelf.getName());
					if (ea instanceof MoveEntityAction)
					{
						ea.setCommandParameters(null);
					}
				}
				break;
			}
		}
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
		// System.out.println("touchDown X: " + screenX);
		// System.out.println("touchDown Y: " + screenY);
		// System.out.println("button: " +
		// MouseButtonConversion.ConvertButtonIntToString(button));

		switch (button)
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

		switch (player.getCursorMode())
		{
			case MoveCoordinateSelection:
			{
				switch (button)
				{
					case Input.Buttons.LEFT:
					{
						System.out.println("A coordinate has been defined by the player.");
						for (Entity e : player.selectedEntities)
						{
							EntityActionsComponent eac = e.getComponent(EntityActionsComponent.class);
							eac.actions.get(EntityActionKnownType.MoveSelf.getName())
									.setCommandParameters(new MoveEntityActionParameters(worldPos));
						}
						break;
					}
				}
				player.setCursorMode(CursorMode.Normal);
				player.setCursorDisplay(PhoenixCursor.Arrow, Player.CURSOR_MODE_PRIORITY_NAME);
				break;
			}

			case CarryResourceSelection:
			{
				switch (button)
				{
					case Input.Buttons.LEFT:
					{
						// since this action has multiple behaviors, the way we construct the command
						// parameter will determine that action to be taken

						// TODO 3 due to the complexity of the action, when multiple entities are
						// selected by the player, it can only be given to a single
						// unit at a time. So a single unit that has the carry action but no command
						// given will be selected. That means that unit who already have been issued a
						// command will not overwrite their existing command

						// TODO 3 but if only one unit is in the selection list of the player, its
						// command can then be overwritten
						
						// attempt to fetch the resource at selected coordinate
						Entity selectedResourceEntity = GameWorldUtility.getEntityAtLocation(gameScreen.engine,
								worldPos, 50.0f, Family.all(ResourceComponent.class).get());

						for (Entity e : player.selectedEntities)
						{
							// does selected unit has any action?
							EntityActionsComponent eac = e.getComponent(EntityActionsComponent.class);
							if (eac != null)
							{
								// does the selected unit's action list has the carry resource action?
								EntityAction ea = eac.actions.get(EntityActionKnownType.CarryResources.getName());
								if (ea != null)
								{
									EntityActionParameters eap = ea.getCommandParameters();
									
									// does the unit have an active command for this action? 
									// and 
									// is not the only selected unit?
									if(eap != null && player.selectedEntities.size() > 1)
									{
										// TODO 4 if statement with empty true block
										// unit is not given an new order, tries to give order to next unit
									}
									else
									{
										// unit is given a new order, no more units are given orders
										ea.setCommandParameters(new CarryResourceEntityActionParameters(selectedResourceEntity, worldPos));
										break;
									}
								}
							}
						}
						break;
					}
				}
				player.setCursorMode(CursorMode.Normal);
				player.setCursorDisplay(PhoenixCursor.Arrow, Player.CURSOR_MODE_PRIORITY_NAME);
				break;
			}

			case Normal:
			{
				switch (button)
				{
					// left button for unit selection
					case Input.Buttons.LEFT:
					{
						// defines the height and width of the selection box
						player.selectionBox.width = worldPos.x - player.selectionBox.x;
						player.selectionBox.height = worldPos.y - player.selectionBox.y;

						player.selectionBox = MathUtility.readjustRectangle(player.selectionBox);

//						System.out.println("selectionBox: " + gameScreen.selectionBox.toString());

						// clear the selected entities list when trying to select new selctables
						gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities.clear();

						// find "selectable" entities that are included in the rectangle
						ImmutableArray<Entity> selectableEntities = gameScreen.engine
								.getEntitiesFor(Family.all(SelectionComponent.class, PositionComponent.class).get());

						for (Entity e : selectableEntities)
						{
							SelectionComponent select = e.getComponent(SelectionComponent.class);
							PositionComponent position = e.getComponent(PositionComponent.class);

							// TODO change the way units are selected/being controlled depending on
							// ownership and selection mode
							OwnershipComponent owner = e.getComponent(OwnershipComponent.class);

							Vector2 position2d = new Vector2(position.pos2D);

//							System.out.println("selectable entity position: " + position2d.toString());

							// if the entity's position is contained within the selection rectangle, add it
							// to the selectedEntities list
							if (player.selectionBox.contains(position2d))
							{
								select.selected = true;
								TextureComponent graph = e.getComponent(TextureComponent.class);
								// System.out.println(graph.textureName);
								gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities.add(e);
							} else
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
						for (Entity e : gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).selectedEntities)
						{
							EntityActionsComponent eac = e.getComponent(EntityActionsComponent.class);
							eac.actions.get(EntityActionKnownType.MoveSelf.getName())
									.setCommandParameters(new MoveEntityActionParameters(worldPos));
						}
						break;
					}

					// middle mouse for dragging the screen
					case Input.Buttons.MIDDLE:
					{
						break;
					}
				}
				break;
			}
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		// TODO 2 weird graphical but with dragging left mouse button while a radial
		// menu is present...
		// System.out.println("X: " + screenX);
		// System.out.println("Y: " + screenY);
		OrthographicCamera cam = gameScreen.camera;

		boolean handled = false;

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
			Vector2 worldPos = GameWorldUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);

			player.selectionBox.width = worldPos.x - player.selectionBox.x;
			player.selectionBox.height = worldPos.y - player.selectionBox.y;

			// System.out.println("selectionBox: " + gameScreen.selectionBox.toString());
			// gameScreen.selectionBox =
			// MathUtility.readjustRectangle(gameScreen.selectionBox);

			handled = true;
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE))
		{
			gameScreen.camera.translate(player.prevCamDragPos.x - screenX, screenY - player.prevCamDragPos.y);
			player.prevCamDragPos.set(screenX, screenY);
			handled = true;
		}

		return handled;
	}

	@Override
	public boolean scrolled(int amount)
	{

		float prevViewportHeight = gameScreen.camera.viewportHeight;
		float prevViewportWidth = gameScreen.camera.viewportWidth;

		if (amount > 0)
		{
			gameScreen.camera.viewportWidth = gameScreen.camera.viewportWidth * 1.1f;
			gameScreen.camera.viewportHeight = gameScreen.camera.viewportHeight * 1.1f;
		} else
		{
			gameScreen.camera.viewportWidth = gameScreen.camera.viewportWidth * 0.9f;
			gameScreen.camera.viewportHeight = gameScreen.camera.viewportHeight * 0.9f;
		}

		if (gameScreen.camera.viewportHeight < Gdx.graphics.getHeight() * 0.3
				|| gameScreen.camera.viewportHeight > Gdx.graphics.getHeight() * 2)
		{
			gameScreen.camera.viewportWidth = prevViewportWidth;
			gameScreen.camera.viewportHeight = prevViewportHeight;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

}
