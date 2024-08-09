package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.phoenix.player.Player;
import com.phoenix.player.action.PlayerAction;

public class KeybindTable extends Table
{
	
	private Player player;
	
	public KeybindTable(Skin skin, Player player)
	{
		super(skin);
		this.player = player;
	}
	
	
	@Override
	public void act(float delta)
	{
		// TODO 3 should the table rebuild itself every frame?
		
		clearChildren();
		
		this.add(new Label("Actions", getSkin(), "default_label"));
		this.add(new Label("Key", getSkin(), "default_label"));
		row();
		
		for(Entry<String, PlayerAction> entry : this.player.getAllPlayerKeybinds())
		{
			this.add(new Label(entry.value.getActionName(), getSkin(), "default_label"));
			this.add(new Label(entry.key.toString(), getSkin(), "default_label"));
			row();
		}
	}
	
}
