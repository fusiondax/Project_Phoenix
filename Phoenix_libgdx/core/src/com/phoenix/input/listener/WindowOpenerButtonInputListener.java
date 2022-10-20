package com.phoenix.input.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.phoenix.ui.window.PhoenixWindowUtility;

public class WindowOpenerButtonInputListener extends InputListener
{
	private String windowName;
	
	public WindowOpenerButtonInputListener(String windowName)
	{
		this.windowName = windowName;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		return true;
	}
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	{
		PhoenixWindowUtility.openWindow(event.getStage(), windowName);
	}
}
