package com.phoenix.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.cursor.PhoenixCursor;

/**
 * Determines what the mouse cursor should display as depending on a number of 
 * @author David Janelle
 *
 */
public class CursorDisplaySystem extends EntitySystem
{
	private GameScreen gameScreen;
	
	public CursorDisplaySystem(GameScreen gs)
	{
		this.gameScreen = gs;
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		Player p = gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);
		
		for (int i = 0; i <= p.getCursorDisplayPriorityListSize(); i++)
		{
			PhoenixCursor newCursor = p.getCursorDisplay(i);
			
			if(newCursor != null)
			{
				gameScreen.game.cursor = newCursor.getCursor();
				break;
			}
		}
	}
}
