package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.phoenix.blueprint.Blueprint;
import com.phoenix.input.BlueprintIconInputListener;
import com.phoenix.player.Player;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlueprintBar extends Table
{
	private Player player;
	
	public static final float BLUEPRINT_ICON_PADDING = 5;

	public BlueprintBar(Player owner)
	{
		this(null, owner);
	}

	public BlueprintBar(Skin skin, Player owner)
	{
		super(skin);
		player = owner;
	}

	@Override
	public void act(float delta)
	{
		clearChildren();
		
		for (Blueprint b : player.getBlueprintLibrary())
		{
			Image blueprintIcon = new Image(getSkin(), "blueprint");
			blueprintIcon.addListener(new BlueprintIconInputListener(b, player));
			
			Label blueprintAmountLabel = new Label(String.valueOf(b.getAmount()), getSkin(), "default_label");
			
			add(blueprintIcon)/*.pad(BLUEPRINT_ICON_PADDING)*/;
			add(blueprintAmountLabel);
			
			// the next blueprint type will be placed underneath this icon
			row();
		}
	}
}
