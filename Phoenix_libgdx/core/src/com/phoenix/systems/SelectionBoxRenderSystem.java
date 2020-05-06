package com.phoenix.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;

public class SelectionBoxRenderSystem extends EntitySystem
{
	private GameScreen gameScreen;
	
	public SelectionBoxRenderSystem(GameScreen gs)
	{
		super();
		this.gameScreen = gs;
	}
	
	public void update(float deltaTime)
	{
		Player player = gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME);
		
		gameScreen.shapeRendererLine.setColor(Color.GREEN);
		gameScreen.shapeRendererLine.rect(player.selectionBox.x, player.selectionBox.y, player.selectionBox.width, player.selectionBox.height);
	}
}
