package com.phoenix.player.action;

import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;

/**
 * 
 * This abstract class defines what a player action should be able to do as well
 * as handle general behaviors
 * 
 * @author David Janelle
 *
 */
public abstract class PlayerAction implements Serializable
{
	protected String[] args;
	
	public abstract void execute(GameScreen gameScreen, Player player);
}
