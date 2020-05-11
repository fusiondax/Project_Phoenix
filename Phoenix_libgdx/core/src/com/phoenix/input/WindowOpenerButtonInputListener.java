package com.phoenix.input;

import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.phoenix.ui.PhoenixWindow;
import com.phoenix.ui.PhoenixWindowBuilder;
import com.phoenix.ui.InGameUI;

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
		boolean alreadyOpen = false;
		
		for(Actor a : event.getStage().getActors())
		{
			if(a instanceof PhoenixWindow)
			{
				if(a.getName().equals(windowName))
				{
					alreadyOpen = true;
					break;
				}
			}
		}
		
		if(!alreadyOpen)
		{
			PhoenixWindowBuilder builder = ((InGameUI) event.getStage()).builder;
			PhoenixWindow window = builder.getWindow(windowName);
			event.getStage().addActor(window);
		}
	}
}
