package com.phoenix.input.listener;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.phoenix.blueprint.Blueprint;
import com.phoenix.player.Player;

public class BlueprintIconInputListener extends InputListener
{
	public Blueprint selectedBlueprint;
	public Player player;
	
	public BlueprintIconInputListener(Blueprint b, Player p)
	{
		super();
		this.selectedBlueprint = b;
		this.player = p;
	}
	
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		return true;
	}

	public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	{
		player.selectedBlueprint = this.selectedBlueprint;
		player.selectedEntities.clear();
		player.selectionBox = new Rectangle();
//		System.out.println("touch done at (" + x + ", " + y + ")");
	}
}
