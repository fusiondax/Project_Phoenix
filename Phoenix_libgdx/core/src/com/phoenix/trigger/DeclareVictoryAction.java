package com.phoenix.trigger;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.screens.GameScreen;

public class DeclareVictoryAction implements TriggerAction
{
	public String victoriousPlayer;
	
	@Override
	public void execute(GameScreen map)
	{
		System.out.println("Player " + map.playerList.get(GameScreen.ACTIVE_PLAYER_NAME).name + " is victorious!");
	}

	@Override
	public void write(Json json)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.victoriousPlayer = jsonData.get("victorious_player").asString();
	}
}
