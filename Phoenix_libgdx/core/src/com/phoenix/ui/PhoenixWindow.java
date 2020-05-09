package com.phoenix.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.phoenix.screens.GameScreen;

public class PhoenixWindow extends Window
{
	private GameScreen gameScreen;

	public static final int RESIZE_BORDER = 5;

	public PhoenixWindow(String title, Skin skin, String styleName, GameScreen gs)
	{
		super(title, skin, styleName);
		this.gameScreen = gs;

		setMovable(true);
		setModal(false);
		setResizable(true);
		setLayoutEnabled(true);
		setResizeBorder(RESIZE_BORDER * 2);
		padTop(20);
		
		getTitleTable().align(Align.center);
		
		getTitleLabel().setAlignment(Align.center);

		Pixmap titleTableColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		titleTableColor.setColor(Color.BLUE);
		titleTableColor.fill();
		getTitleTable().setBackground(new Image(new Texture(titleTableColor)).getDrawable());

		getTitleTable().setScale(0.2f);

		addListener(new InputListener()
		{
			public boolean mouseMoved(InputEvent event, float x, float y)
			{
				System.out.println("mouse cursor at x: " + x + " y: " + y);

				// left or right
				if ((x >= 0 && x <= RESIZE_BORDER) || (x <= getWidth() && x >= getWidth() - RESIZE_BORDER))
				{
					gameScreen.game.cursor = PhoenixCursor.Horizontal_Resize.getCursor();
				}

				// TODO weird bug where you can't resize windows from the top. I believe it's
				// from the Window class that I inherit
				// bottom
				if ((y >= 0 && y <= RESIZE_BORDER) || (y >= getHeight() - RESIZE_BORDER && y <= getHeight()))
				{
					gameScreen.game.cursor = PhoenixCursor.Vertical_Resize.getCursor();
				}

				// top right doesn't work
				// bottom left
				if (((x >= 0 && x <= 30) && (y >= 0 && y <= 30))
						/*|| ((x <= getWidth() && x >= getWidth() - 30) && (y <= getHeight() && y >= getHeight() - 30))*/)
				{
					gameScreen.game.cursor = PhoenixCursor.Diagonal_Bottom_Left_Resize.getCursor();
				}
				// top left doesn't work
				// bottom right
				if ((x <= getWidth() && x >= getWidth() - 30) && (y >= 0 && y <= 30)
						/*|| ((x >= 0 && x <= 30) && (y <= getHeight() && y >= getHeight() - 30))*/)
				{
					gameScreen.game.cursor = PhoenixCursor.Diagonal_Bottom_Right_Resize.getCursor();
				}

				// center
				if ((x <= getWidth() - RESIZE_BORDER && x >= RESIZE_BORDER)
						&& (y <= getHeight() - RESIZE_BORDER && y >= RESIZE_BORDER))
				{
					gameScreen.game.cursor = PhoenixCursor.Arrow.getCursor();
				}
				return true;
			}

			public void touchDragged(InputEvent event, float x, float y, int pointer)
			{
				if ((y >= getHeight() - RESIZE_BORDER && y <= getHeight()))
				{
					System.out.println("wow");
				}
			}
		});
	}

	@Override
	public void act(float delta)
	{

	}
}
