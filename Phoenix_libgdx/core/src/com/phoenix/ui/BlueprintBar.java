package com.phoenix.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.phoenix.blueprint.Blueprint;
import com.phoenix.input.BlueprintIconInputListener;
import com.phoenix.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BlueprintBar extends Table
{
	private boolean open;
	private Player player;
	private HorizontalGroup hGroup;

	public BlueprintBar(Player owner)
	{
		this(null, owner);
	}

	public BlueprintBar(Skin skin, Player owner)
	{
		super(skin);
		open = false;
		player = owner;
		setTouchable(Touchable.enabled);

		hGroup = new HorizontalGroup();
		hGroup.center();
		hGroup.grow();
		// hGroup.debug();

		addActor(hGroup);

		addListener(new InputListener()
		{
			public boolean keyDown(InputEvent event, int keycode)
			{
				// TODO bind this key to the future keybind map when configurable keys becomes a
				// thing
				boolean handled = false;
				if (Input.Keys.TAB == keycode)
				{
					toggle();
					handled = true;
				}
				return handled;
			}

			public boolean keyUp(InputEvent event, int keycode)
			{
				return true;
			}
		});
	}

	@Override
	public void act(float delta)
	{
		// position the blueprint bar at the right position with the right dimensions
		int xPosition = 10;

		if (open)
		{
			hGroup.clearChildren();

			for (Blueprint b : player.getBlueprintLibrary())
			{
				Table icon = new Table(getSkin());
				
				icon.setSize(40, 40);
				
				icon.setBackground(getSkin().getDrawable("blueprint"));
				
				Label blueprintAmount = new Label(String.valueOf(b.getAmount()), getSkin(), "default_label");
				
				icon.add(blueprintAmount);

				icon.addListener(new BlueprintIconInputListener(b, player));

				//icon.debug();

				hGroup.addActor(icon);
			}
		}
		else
		{
			// when the bar isn't open, it partially "hides" outside of the screen
			xPosition = -40;
		}

		setBounds(xPosition, Gdx.graphics.getHeight() - this.getHeight(), 50, 500);
	}

	public boolean isOpen()
	{
		return open;
	}

	public void toggle()
	{
		open = !open;
	}
}
