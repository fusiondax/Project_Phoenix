package com.phoenix.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.phoenix.entityAction.EntityAction;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;

public class EntityActionSystem extends EntitySystem
{
	private GameScreen gameScreen;
	
	public EntityActionSystem(GameScreen gs)
	{
		this.gameScreen = gs;
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		Player p = gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);
		
		for(EntityAction eAction : p.entityActionQueue)
		{
			int errCode = eAction.attemptExecute();
			
			
			
		}
		
		p.entityActionQueue.clear();
	}

}
