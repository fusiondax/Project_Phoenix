package com.phoenix.player.action;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;

public class PlayerActionTimeDilation extends PlayerAction
{
	@Override
	public void execute(GameScreen gameScreen, Player player)
	{
		switch(this.args[0])
		{
			case "pause_resume":
			{
				gameScreen.toggleSystems();
				break;
			}
			case "speed_up":
			{
				gameScreen.setTimeDilation(gameScreen.getTimeDilation() + 0.1f);
				break;
			}
			case "slow_down":
			{
				gameScreen.setTimeDilation(gameScreen.getTimeDilation() - 0.1f);
				break;
			}
			case "reset_time":
			{
				gameScreen.setTimeDilation(1);
				break;
			}
		}
	}

	@Override
	public void write(Json json)
	{
		
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.args = jsonData.get("arguments").asStringArray();
	}
}
