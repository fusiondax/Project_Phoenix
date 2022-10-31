package com.phoenix.input.manager;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.payne.games.piemenu.PieMenu;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.RadialMenuComponent;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.InGameUI;
import com.phoenix.ui.radialMenu.RadialMenuButton;

public class RadialMenuInputManager implements InputProcessor
{
	private GameScreen gameScreen;
	private Player player;

	public RadialMenuInputManager(GameScreen gs)
	{
		this.gameScreen = gs;
		this.player = this.gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		return false;
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
		boolean handled = false;

		switch (button)
		{
			case Input.Buttons.LEFT:
			{
				break;
			}
		}
		return handled;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		boolean handled = false;

		switch (button)
		{
			case Input.Buttons.MIDDLE:
			{
				handled = handleRadialMenuButtonInteractions(screenX, screenY, pointer, button);
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

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

	// TODO 3 maybe put this in the GameWorld input manager...
	/**
	 * 
	 * Note: if multiple entities have buttons of the same type but those entities
	 * define different icons for that button type, the first entity to be process
	 * will have its icon used.
	 * 
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @return
	 */
	private boolean handleRadialMenuButtonInteractions(int screenX, int screenY, int pointer, int button)
	{
		boolean handled = false;

		// does the player already has an active Radial Menu in the UI stage?
		if (!gameScreen.guiStage.hasRadialMenu())
		{
			// retrieve all radialMenuComponents from the player's selected entities
			ArrayList<RadialMenuComponent> radMenuComps = new ArrayList<RadialMenuComponent>();

			for (Entity e : player.selectedEntities)
			{
				RadialMenuComponent rmc = e.getComponent(RadialMenuComponent.class);

				if (rmc != null)
				{
					radMenuComps.add(rmc);
				}
			}
			Skin skin = PhoenixAssetManager.getInstance().activeSkin;
			PieMenu menu = null;

			// if there is at least one of those units who had a radial menu, create a
			// radial Menu centered on the mouse pointer.
			if (!radMenuComps.isEmpty())
			{
				// TODO 3 maybe use an anonymous class to fine-tune behaviours (see
				// documentation)
				menu = new PieMenu(skin.getRegion("white_pixel"), skin, "default_radial_menu", 100.0f);

				menu.setPosition(screenX - (menu.getWidth() / 2),
						Gdx.graphics.getHeight() - screenY - (menu.getHeight() / 2));
				menu.setInnerRadiusPercent(0.5f);

				menu.addListener(new InputListener()
				{
					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
					{
						PieMenu radialMenu = (PieMenu) event.getListenerActor();
						Vector2 cursorPos = new Vector2(x - (radialMenu.getWidth() / 2),
								y - (radialMenu.getHeight() / 2));

						float cursorDistance = cursorPos.dst(0, 0);

						if (cursorDistance > radialMenu.getWidth() / 2)
						{
							event.getStage().getActors().removeValue(radialMenu, false);
						}
					}
				});

				menu.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent event, Actor actor)
					{
						((InGameUI) event.getStage()).executeRadialMenuAction();
					}
				});
				
				gameScreen.guiStage.addActor(menu);
			}

			// add radial menu buttons to the menu. There can only be one button of each
			// type.

			ArrayList<String> existingButtonTypes = new ArrayList<String>();
			
			// for each radial menu component in the player entity selection...
			for (RadialMenuComponent rmc : radMenuComps)
			{
				// for each button in that entity's button type list...
				for (RadialMenuButton b : rmc.buttons)
				{
					// add the button only if it wasn't added before
					if(!existingButtonTypes.contains(b.type))
					{
						Image icon;

						if (!b.iconName.equals(""))
						{
							icon = new Image(skin, b.iconName);
						}
						else
						{
							icon = new Image();
						}
						
						icon.setName(b.type);
						menu.addActor(icon);
						existingButtonTypes.add(b.type);
					}
				}
			}

			handled = true;
		}
		return handled;
	}
}
