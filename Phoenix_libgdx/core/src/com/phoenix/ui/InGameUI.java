package com.phoenix.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.payne.games.piemenu.PieMenu;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.EntityActionsComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.entityAction.EntityAction;
import com.phoenix.entityAction.EntityActionKnownType;
import com.phoenix.entityAction.MoveEntityAction;
import com.phoenix.entityAction.MoveEntityActionParameters;
import com.phoenix.player.CursorMode;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.cursor.PhoenixCursor;
import com.phoenix.ui.window.PhoenixWindowBuilder;
import com.phoenix.ui.window.PhoenixWindowUtility;
import com.phoenix.ui.window.WindowOpenerBar;

public class InGameUI extends Stage
{
	public GameScreen gameScreen;

	public PhoenixWindowBuilder builder;

	private boolean isSetup = false;

	public InGameUI(Viewport port, GameScreen gs)
	{
		super(port);
		this.gameScreen = gs;
	}

	public void setupUI()
	{
		Skin skin = PhoenixAssetManager.getInstance().activeSkin;

		// initialize primary actors

		builder = new PhoenixWindowBuilder(skin, "default_label", "default_window", gameScreen);

		WindowOpenerBar wob = new WindowOpenerBar(skin);

		addActor(wob);

		this.isSetup = true;
		// addActor(builder.getWindow("misc_info"));
		// addActor(builder.getWindow("blueprint_bar"));
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	// TODO 2 find a better place for this method, some sort of Radial Menu utility class or something...
	/**
	 * Note: the 'name' attribute of the menu's children is used to define what the
	 * button's action is suppose to be.
	 */
	public void executeRadialMenuAction()
	{
		// 1- we know that this radial menu is always contextually used with an entity
		// because the entity needs to have a 'radial menu' component to be 'eligible'
		// for a radial menu.

		// 2- the radial menu component has a list of 'radial menu button' used to
		// create
		// every button in the radial menu: it has information on the graphical icon to
		// load
		// and the 'button type'

		// 3- then it is clear that we must have access to those Radial Menu Buttons

		// 4- the action the selected button is suppose to perform is stored as a String
		// in the selected button's 'name' attribute (maybe unconventional, but
		// practical)

		// 5- with that name, we can associate it with a behavior

		PieMenu menu = getActiveRadialMenu();

		// TODO 3 I got a null pointer exception here, and I was not able to re-create it...
		String selectedButtonAction = menu.getChildren().get(menu.getSelectedIndex()).getName();
		
		Player p = gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);

		System.out.println("Selected button type: " + selectedButtonAction);

		switch (selectedButtonAction)
		{
			case "move":
			{
				// this requires a new UI elements that prompts the player to 'select' a
				// destination for the unit to move to.
				p.setCursorMode(CursorMode.MoveCoordinateSelection);
				p.setCursorDisplay(PhoenixCursor.Target, Player.CURSOR_MODE_PRIORITY_NAME);
				break;
			}
			case "stop":
			{
				for(Entity e : p.selectedEntities)
				{
					EntityAction ea = e.getComponent(EntityActionsComponent.class).actions.get(EntityActionKnownType.MoveSelf.getName());
					if(ea instanceof MoveEntityAction)
					{
						ea.setCommandParameters(null);
					}
				}
				break;
			}

			case "carry":
			{
				p.setCursorMode(CursorMode.CarryResourceSelection);
				p.setCursorDisplay(PhoenixCursor.Target, Player.CURSOR_MODE_PRIORITY_NAME);
				
				// System.out.println("A Behavior has not yet been implemented for this button type");
				break;
			}

			case "attack":
			{
				System.out.println("A Behavior has not yet been implemented for this button type");
				break;
			}

			case "disassemble":
			{
				System.out.println("A Behavior has not yet been implemented for this button type");
				break;
			}

			case "information":
			{
				PhoenixWindowUtility.openWindow(this, "misc_info");
				// TODO 3 debug behavior, remove this eventually
				for(Entity e : p.selectedEntities)
				{
					e.getComponents().toString();
				}
				break;
			}
			default:
			{
				System.out.println("this button type is not handled.");
				break;
			}
		}
		// TODO 2 Once the button on the menu is selected, does the radial menu needs to
		// disappear?
		
		getActors().removeValue(menu, false);
	}

	// TODO 2 this is pretty gross way to check if a radial menu is already being
	// display. recommend finding a cleverer way of determining
	/**
	 * @return true if this stage has at least one actor of type {@link PieMenu} in
	 *         his actors list
	 */
	public boolean hasRadialMenu()
	{
		boolean hasRadialMenu = false;

		for (Actor a : getActors())
		{
			if (a instanceof PieMenu)
			{
				hasRadialMenu = true;
				break;
			}
		}
		return hasRadialMenu;
	}

	// TODO 2 this assumes that there is only one PieMenu in the stage at any given
	// time, which is true as of the writing of this, but might not be true in the
	// future
	/**
	 * 
	 * @return may return null if no {@link PieMenu} was foind in the stage's actors
	 *         list
	 */
	public PieMenu getActiveRadialMenu()
	{
		PieMenu menu = null;

		for (Actor a : getActors())
		{
			if (a instanceof PieMenu)
			{
				menu = (PieMenu) a;
				break;
			}
		}

		return menu;
	}

	public boolean isSetup()
	{
		return isSetup;
	}
}
