package com.phoenix.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.phoenix.input.WindowCloseButtonInputListener;
import com.phoenix.input.WindowInputListener;
import com.phoenix.screens.GameScreen;

public class PhoenixWindow extends Window
{
	public GameScreen gameScreen;
	
	private boolean keepWithinStage = true;
	private Rectangle defaultDisposition;

	private Image lockWindowButton;

	private Image resetWindowButton;

	private Image minMaxWindowButton;
	private boolean isMinimized;
	private Rectangle previousDisposition;

	private Image closeWindowButton;

	public static final int RESIZE_BORDER = 5;
	public static final int TITLE_TABLE_HEIGHT = 30;
	public static final float MINIMIZED_WINDOW_SPACING = 20;

	public PhoenixWindow(String name, String title, Skin skin, String styleName, GameScreen gs)
	{
		this(name, title, null, skin, styleName, gs);
	}

	public PhoenixWindow(String name, String title, float x, float y, float width, float height, Skin skin, String styleName,
			GameScreen gs)
	{
		this(name, title, new Rectangle(x, y, width, height), skin, styleName, gs);
	}

	public PhoenixWindow(String name, String title, Rectangle defaultDisposition, Skin skin, String styleName, GameScreen gs)
	{
		
		super(title, skin, styleName);
		this.setName(name);
		this.gameScreen = gs;
		this.isMinimized = false;
		this.previousDisposition = new Rectangle();
		this.defaultDisposition = defaultDisposition;

		setMovable(true);
		setModal(false);
		setResizable(true);
		setLayoutEnabled(true);
		setKeepWithinStage(true);
		// pad top defines the height of the title bar
		padTop(TITLE_TABLE_HEIGHT);
		padBottom(RESIZE_BORDER);

		Pixmap titleTableColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		titleTableColor.setColor(Color.BLUE);
		titleTableColor.fill();
		getTitleTable().setBackground(new Image(new Texture(titleTableColor)).getDrawable());

		initializeTitleBarButtons(skin);

		resetDisposition();

		// clear the listeners from the parent class. the listener will be replaced by a
		// similar listener with updated features
		clearListeners();

		addCaptureListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				getStage().setKeyboardFocus(event.getTarget());
				toFront();
				return false;
			}
		});

		addListener(new WindowInputListener(this));
	}

	private void initializeTitleBarButtons(Skin skin)
	{
		// the order in which we add the buttons to the title bar is important

		// title label
		getTitleTable().getCell(getTitleLabel()).align(Align.left).padLeft(RESIZE_BORDER).padRight(RESIZE_BORDER)
				.minWidth(getTitleLabel().getWidth());

		// lock window button
		lockWindowButton = new Image(skin, "ui_unlock_window_button");
		lockWindowButton.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				if (isResizable())
				{
					setResizable(false);
					setMovable(false);
					lockWindowButton.setDrawable(getSkin(), "ui_lock_window_button");
				}
				else
				{
					setResizable(true);
					setMovable(true);
					lockWindowButton.setDrawable(getSkin(), "ui_unlock_window_button");
				}
			}
		});
		getTitleTable().add(lockWindowButton).align(Align.right).pad(RESIZE_BORDER)
				.minWidth(lockWindowButton.getWidth());

		// reset disposition button
		resetWindowButton = new Image(skin, "ui_reset_window_button");
		resetWindowButton.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				resetDisposition();
			}
		});
		getTitleTable().add(resetWindowButton).align(Align.right).pad(RESIZE_BORDER)
				.minWidth(resetWindowButton.getWidth());

		// minimize/maximize button
		minMaxWindowButton = new Image(skin, "ui_minimize_window_button");
		minMaxWindowButton.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				toggleMinimized();
			}
		});
		getTitleTable().add(minMaxWindowButton).align(Align.right).pad(RESIZE_BORDER)
				.minWidth(minMaxWindowButton.getWidth());

		// close button
		closeWindowButton = new Image(skin, "ui_close_window_button");
		closeWindowButton.addListener(new WindowCloseButtonInputListener());
		getTitleTable().add(closeWindowButton).align(Align.right).pad(RESIZE_BORDER)
				.minWidth(closeWindowButton.getWidth());
	}

	@Override
	public float getMinHeight()
	{
		float minHeight = getPadTop() + getPadBottom();

		// the content of the window itself
		// for (Cell<Actor> c : getCells())
		// {
		// minHeight += c.getMinHeight() + c.getPadTop() + c.getPadBottom();
		// }

		return minHeight;
	}

	@Override
	public float getMinWidth()
	{
		float minTitleBarWidth = 0;

		float minWindowContentWidth = 0;

		// the content of the title bar
		for (Cell<Actor> c : getTitleTable().getCells())
		{
			minTitleBarWidth += c.getMinWidth() + c.getPadRight() + c.getPadLeft();
			// TODO should be able to ellipse the title bar label, but it doesn't work as
			// intended
			// if(c.getActor() != getTitleLabel())
			// {
			// minTitleBarWidth += c.getMinWidth() + c.getPadRight() + c.getPadLeft();
			// }
		}

		// the content of the window itself
		for (Cell<Actor> c : getCells())
		{
			minWindowContentWidth += c.getMinWidth() + c.getPadRight() + c.getPadLeft();
		}

		return Math.max(minTitleBarWidth, minWindowContentWidth);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		// if(get)
	}

	public void resetDisposition()
	{
		// if the default disposition is null
		if (defaultDisposition == null)
		{
			defaultDisposition = new Rectangle(0, 0, getMinWidth(), getMinHeight());
		}
		// if not, does it respects the minimum width and height?
		else
		{
			if (defaultDisposition.width < getMinWidth())
			{
				defaultDisposition.setWidth(getMinWidth());
			}

			if (defaultDisposition.height < getMinHeight())
			{
				defaultDisposition.setHeight(getMinHeight());
			}
		}

		setBounds(defaultDisposition.x, defaultDisposition.y, defaultDisposition.width, defaultDisposition.height);

		// make sure the window isn't in "minimized" mode
		if (isMinimized)
		{
			setMinimized(false, false);
		}
	}

	public boolean isMinimized()
	{
		return isMinimized;
	}

	public void setMinimized(boolean minimized)
	{
		this.setMinimized(minimized, true);
	}

	public void setMinimized(boolean minimized, boolean updateDisposition)
	{
		this.isMinimized = minimized;
		updateMinMaxIcon();
		if (updateDisposition)
		{
			updateMinMaxDisposition();
		}
	}

	public void toggleMinimized()
	{
		this.setMinimized(!this.isMinimized, true);
	}

	public void toggleMinimized(boolean update)
	{
		this.setMinimized(!this.isMinimized, update);
	}

	private void updateMinMaxDisposition()
	{
		if (isMinimized)
		{
			float minimizedX = 0;

			// place the minimized window next to other minimized windows
			for (Actor a : getStage().getRoot().getChildren())
			{
				if (a instanceof PhoenixWindow)
				{
					if (a != this)
					{
						if (((PhoenixWindow) a).isMinimized())
						{
							minimizedX += a.getWidth() + MINIMIZED_WINDOW_SPACING;
						}
					}
				}
			}

			previousDisposition.set(getX(), getY(), getWidth(), getHeight());
			setBounds(minimizedX, 0, getMinWidth(), getMinHeight());
		}
		else
		{
			setBounds(previousDisposition.x, previousDisposition.y, previousDisposition.width,
					previousDisposition.height);
		}
	}

	private void updateMinMaxIcon()
	{
		if (isMinimized)
		{
			this.minMaxWindowButton.setDrawable(getSkin(), "ui_maximize_window_button");
		}
		else
		{
			this.minMaxWindowButton.setDrawable(getSkin(), "ui_minimize_window_button");
		}
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

	public Rectangle getPreviousDisposition()
	{
		return previousDisposition;
	}

	public void setPreviousDisposition(Rectangle previousDisposition)
	{
		this.previousDisposition = previousDisposition;
	}
}