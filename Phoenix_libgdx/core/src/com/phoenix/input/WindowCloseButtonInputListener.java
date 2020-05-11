package com.phoenix.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class WindowCloseButtonInputListener extends InputListener
{
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		return true;
	}
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	{
		event.getTarget().getParent().getParent().remove();
	}
}
