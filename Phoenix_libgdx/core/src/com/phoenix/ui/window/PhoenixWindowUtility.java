package com.phoenix.ui.window;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.phoenix.ui.InGameUI;

public class PhoenixWindowUtility
{
	public static void openWindow(Stage stage, String windowName)
	{
		boolean alreadyOpen = false;
		
		for(Actor a : stage.getActors())
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
			PhoenixWindowBuilder builder = ((InGameUI) stage).builder;
			PhoenixWindow window = builder.getWindow(windowName);
			stage.addActor(window);
		}
	}
}
