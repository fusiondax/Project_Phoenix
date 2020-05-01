package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.phoenix.game.Blueprint;
import com.phoenix.game.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlueprintBar extends Table
{
	private boolean open;
	private Player player;
	
	public BlueprintBar(Player owner)
	{
		this(null, owner);
	}
	

	public BlueprintBar(Skin skin, Player owner)
	{
		super(skin);
		open = false;
		player = owner;
		setTouchable(Touchable.enabled);
		addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("touch started at (" + x + ", " + y + ")");
		 		return true;
		 	}
		 
		 	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		 		System.out.println("touch done at (" + x + ", " + y + ")");
		 	}
		 	
		 	public boolean keyDown (InputEvent event, int keycode) {
		 		
		 		// TODO bind this key to the future keybind map when configurable keys becomes a thing
		 		
		 		boolean handled = false;
		 		if(Input.Keys.TAB == keycode)
		 		{
		 			toggle();
		 			handled = true;
		 		}
		 		
				return handled;
			}
		 	
		 	public boolean keyUp (InputEvent event, int keycode) {
		 		
		 		
				return true;
			}
		});
	}
	
	@Override
	public void act(float delta)
	{
		// position the blueprint bar at the right position with the right dimensions
		int xPosition = 0;
		
		if(open)
		{
			for(Blueprint b : player.blueprintLibrary)
			{
				// for every blueprint type in the player's blueprint library, create a smaller image of a blueprint that can fit in the blueprint bar
				
			}
		}
		else
		{
			// when the bar isn't open, it partially "hides" outside of the screen
			xPosition = -40;
		}
		
		setBounds(xPosition, Gdx.graphics.getHeight() - this.getHeight(), 50, 500);
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
	public void toggle()
	{
		open = !open;
	}
}
