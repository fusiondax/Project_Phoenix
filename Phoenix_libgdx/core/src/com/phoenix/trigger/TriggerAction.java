package com.phoenix.trigger;

import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.screens.GameScreen;

public interface TriggerAction extends Serializable
{
	public void execute(GameScreen map);
}
