package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.phoenix.input.WindowOpenerButtonInputListener;

public class WindowOpenerBar extends Table
{
	public static final float WINDOW_OPENER_PAD = 5;
	
	public WindowOpenerBar(Skin skin)
	{
		super(skin);
		
		initializeWindowBar();
		//debugAll();
	}
	
	private void initializeWindowBar()
	{
		setBackground(getSkin().get("default_ui_background", TextureRegionDrawable.class));
		//setBackground("default_ui_background");
		
		// open blueprint bar button
		Image blueprintBarButton = new Image(getSkin(), "ui_open_blueprint_bar_button");
		blueprintBarButton.addListener(new WindowOpenerButtonInputListener("blueprint_bar"));
		add(blueprintBarButton).pad(WINDOW_OPENER_PAD);
		
		// open minimap button
		
		// open misc info button
		Image miscInfoButton = new Image(getSkin(), "ui_open_misc_info_button");
		miscInfoButton.addListener(new WindowOpenerButtonInputListener("misc_info"));
		add(miscInfoButton).pad(WINDOW_OPENER_PAD);
		// open game menu button
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		setBounds(0, Gdx.graphics.getHeight() - getPrefHeight(), getPrefWidth(), getPrefHeight());
	}
	
	@Override
	public float getMinHeight()
	{
		return getPadTop() + getCells().first().getActor().getHeight() + getPadBottom();
	}

	@Override
	public float getMinWidth()
	{
		float minContentWidth = 0;

		for (Cell<Actor> c : getCells())
		{
			minContentWidth += c.getActor().getWidth() + c.getPadRight() + c.getPadLeft();
		}

		return minContentWidth;
	}
}
