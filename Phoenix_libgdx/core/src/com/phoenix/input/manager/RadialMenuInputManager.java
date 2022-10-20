package com.phoenix.input.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.payne.games.piemenu.PieMenu;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.RadialMenuComponent;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.radialMenu.RadialMenuButton;
import com.phoenix.utility.GameWorldUtility;

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
			case Input.Buttons.RIGHT:
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
	private boolean handleRadialMenuButtonInteractions(int screenX, int screenY, int pointer, int button)
	{
		boolean handled = false;
		// does the player has an active Radial Menu?
		
		
		if (!gameScreen.guiStage.hasRadialMenu())
		{
			OrthographicCamera cam = gameScreen.camera;
			Vector2 worldPos = GameWorldUtility.getWorldPositionFromScreenLocation(screenX, screenY, cam);
			
			
			// activate radial menus on entities with such feature
			Entity entity = GameWorldUtility.getEntityAtLocation(gameScreen.engine, worldPos, 20.0f,
					Family.all(RadialMenuComponent.class).get());

			if (entity != null)
			{
				float entitySize = 20.0f;
				// TODO 4 can there be a unit with a "size" but no "collision hitbox" ?
				CollisionHitboxComponent chc = entity.getComponent(CollisionHitboxComponent.class);
				
				if(chc != null)
				{
					entitySize = chc.size;
				}
				Skin skin = PhoenixAssetManager.getInstance().activeSkin;
				
				// TODO 3 maybe use an anonymous class to fine-tune behaviours (see documentation)
				PieMenu menu = new PieMenu(skin.getRegion("white_pixel"), skin, "default_radial_menu", entitySize * 5);
				menu.setPosition(screenX - (menu.getWidth() / 2), Gdx.graphics.getHeight() - screenY - (menu.getHeight() / 2));
				menu.setInnerRadiusPercent(0.5f);
				
				// menu.setHitThroughInnerRadius(true);
				
				
				// add behaviour to close radial menu on right-click
				menu.addListener(new InputListener() 
				{
					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
					{
						PieMenu radialMenu = (PieMenu)event.getListenerActor();
						Vector2 cursorPos = new Vector2(x - (radialMenu.getWidth() / 2), y - (radialMenu.getHeight() / 2));
						
						float cursorDistance = cursorPos.dst(0, 0);
						
						if(cursorDistance > radialMenu.getWidth() / 2)
						{
							event.getStage().getActors().removeValue(radialMenu, false);
						}
					}
				});
				
				RadialMenuComponent radialMenuComp = entity.getComponent(RadialMenuComponent.class);
				
				for(RadialMenuButton b : radialMenuComp.buttons)
				{
					Image icon;
					
					if(!b.iconName.equals(""))
					{
						icon = new Image(skin, b.iconName);
						menu.addActor(icon);
					}
					else
					{
						icon = new Image();
					}
					
					menu.addActor(icon);
				}
				
				// TODO 1 define listener to execute action on sector click
				menu.addListener(new ChangeListener() 
				{
				    @Override
				    public void changed(ChangeEvent event, Actor actor) {
				    	//System.out.println("actor: " + actor.toString());
				        System.out.println("The selected index is: " + ((PieMenu)actor).getSelectedIndex());
				    }
				});
				
				
				gameScreen.guiStage.addActor(menu);
				
				handled = true;
			}
		}
		return handled;
	}
}
