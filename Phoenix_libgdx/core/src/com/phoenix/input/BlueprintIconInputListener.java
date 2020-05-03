package com.phoenix.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.phoenix.blueprint.Blueprint;
import com.phoenix.game.Player;

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
//		System.out.println("touch done at (" + x + ", " + y + ")");
	}
}
