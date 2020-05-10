package com.phoenix.input;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.phoenix.ui.PhoenixWindow;

public class WindowMinMaxButtonInputListener extends InputListener
{
	private PhoenixWindow ownerWindow;
	private Rectangle previousDisposition = new Rectangle();
	
	private Image button;
	private Skin skin;
	private boolean isMinimized;
	
	public WindowMinMaxButtonInputListener(Image button, PhoenixWindow window)
	{
		this.button = button;
		this.ownerWindow = window;
		this.skin = window.getSkin();
		this.button.setDrawable(this.skin, "ui_minimize_window_button");
		this.isMinimized = false;
		this.previousDisposition = new Rectangle();
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		System.out.println("clicking the \"minimize/maximize window\" button");
		return true;
	}
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	{
		if(!isMinimized)
		{
			previousDisposition.set(ownerWindow.getX(), ownerWindow.getY(), ownerWindow.getWidth(), ownerWindow.getHeight());
			ownerWindow.setBounds(0, 0, ownerWindow.getMinWidth(), ownerWindow.getMinHeight());
		}
		else
		{
			ownerWindow.setBounds(previousDisposition.x, previousDisposition.y, previousDisposition.width, previousDisposition.height);
		}
		isMinimized = !isMinimized;
		
		toggleButtonImage();
		System.out.println("min/maxing the window");
	}
	
	private void toggleButtonImage()
	{
		if(isMinimized)
		{
			this.button.setDrawable(skin, "ui_maximize_window_button");
		}
		else
		{
			this.button.setDrawable(skin, "ui_minimize_window_button");
		}
	}
}
