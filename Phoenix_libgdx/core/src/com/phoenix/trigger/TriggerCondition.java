package com.phoenix.trigger;

import com.badlogic.gdx.utils.Json.Serializable;
import com.phoenix.screens.GameScreen;

public interface TriggerCondition extends Serializable
{
	public boolean isConditionMet(GameScreen map);
}
