package com.phoenix.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.phoenix.input.WindowCloseButtonInputListener;
import com.phoenix.input.WindowInputListener;
import com.phoenix.input.WindowMinMaxButtonInputListener;
import com.phoenix.screens.GameScreen;

public class PhoenixWindow extends Window
{
	public GameScreen gameScreen;
	private boolean keepWithinStage = true;

	public static final int RESIZE_BORDER = 5;
	public static final int TITLE_TABLE_HEIGHT = 20;

	public PhoenixWindow(String title, Skin skin, String styleName, GameScreen gs)
	{
		super(title, skin, styleName);
		this.gameScreen = gs;

		setMovable(true);
		setModal(false);
		setResizable(true);
		setLayoutEnabled(true);
		setKeepWithinStage(true);
		// pad top defines the height of the title bar
		padTop(TITLE_TABLE_HEIGHT);

		Pixmap titleTableColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		titleTableColor.setColor(Color.BLUE);
		titleTableColor.fill();
		getTitleTable().setBackground(new Image(new Texture(titleTableColor)).getDrawable());

		// getTitleLabel().
		getTitleTable().getCell(getTitleLabel()).padLeft(RESIZE_BORDER).minWidth(getTitleLabel().getWidth());

		addTitleBarButtons(skin);

		// clear the listeners from the parent class. the listener will be replaced by a
		// similar listener with updated features
		clearListeners();
		addListener(new WindowInputListener(this));
	}

	private void addTitleBarButtons(Skin skin)
	{
		// minimize/maximize button
		Image minMaxWindowButton = new Image(skin, "ui_minimize_window_button");
		minMaxWindowButton.addListener(new WindowMinMaxButtonInputListener(minMaxWindowButton, this));
		getTitleTable().add(minMaxWindowButton).align(Align.right).padRight(RESIZE_BORDER).padTop(RESIZE_BORDER)
				.minWidth(minMaxWindowButton.getWidth());

		// close button
		Image closeWindowButton = new Image(skin, "ui_close_window_button");
		closeWindowButton.addListener(new WindowCloseButtonInputListener());
		getTitleTable().add(closeWindowButton).align(Align.right).padRight(RESIZE_BORDER).padTop(RESIZE_BORDER)
				.minWidth(closeWindowButton.getWidth());
	}

	@Override
	public float getMinHeight()
	{
		return getPadTop();

	}

	@Override
	public float getMinWidth()
	{
		float minWidth = 0;

		for (Cell<Actor> c : getTitleTable().getCells())
		{
			minWidth += c.getMinWidth();
		}

		System.out.println("min width: " + minWidth);

		return minWidth;
	}

	@Override
	public void act(float delta)
	{

	}

	public boolean getKeepWithinStage()
	{
		return keepWithinStage;
	}

	@Override
	public void setKeepWithinStage(boolean keepWithinStage)
	{
		this.keepWithinStage = keepWithinStage;
		super.setKeepWithinStage(this.keepWithinStage);
	}

	public enum WindowResizeMode
	{
		None, Top, Bottom, Left, Right, Top_Left, Top_Right, Bottom_Left, Bottom_Right, Move;
	}
}
