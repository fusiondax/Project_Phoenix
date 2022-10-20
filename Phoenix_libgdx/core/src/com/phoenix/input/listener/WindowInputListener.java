package com.phoenix.input.listener;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.phoenix.ui.PhoenixCursor;
import com.phoenix.ui.window.PhoenixWindow;

public class WindowInputListener extends InputListener
{
	public enum WindowResizeMode
	{
		None, Top, Bottom, Left, Right, Top_Left, Top_Right, Bottom_Left, Bottom_Right, Move;
	}

	private PhoenixWindow ownerWindow;

	private float startX, startY, lastX, lastY;
	private Rectangle window = new Rectangle();
	private WindowInputListener.WindowResizeMode edge;
	private boolean clampPosition;
	private boolean dragging;

	public WindowInputListener(PhoenixWindow window)
	{
		super();
		this.ownerWindow = window;
	}

	private void updateEdge(float x, float y)
	{
		//System.out.println("mouse cursor at x: " + x + " y: " + y);

		edge = WindowInputListener.WindowResizeMode.None;
		
		if(ownerWindow.isResizable())
		{
			// left
			if (x >= 0 && x <= PhoenixWindow.RESIZE_BORDER)
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Horizontal_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Left;
			}
	
			// right
			if (x <= ownerWindow.getWidth() && x >= ownerWindow.getWidth() - PhoenixWindow.RESIZE_BORDER)
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Horizontal_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Right;
			}
	
			// top
			if (y >= ownerWindow.getHeight() - PhoenixWindow.RESIZE_BORDER && y <= ownerWindow.getHeight())
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Vertical_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Top;
			}
	
			// bottom
			if (y >= 0 && y <= PhoenixWindow.RESIZE_BORDER)
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Vertical_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Bottom;
			}
			// top right
			if ((x <= ownerWindow.getWidth() && x >= ownerWindow.getWidth() - PhoenixWindow.RESIZE_BORDER)
					&& (y <= ownerWindow.getHeight() && y >= ownerWindow.getHeight() - PhoenixWindow.RESIZE_BORDER))
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Diagonal_Bottom_Left_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Top_Right;
			}
	
			// top left
			if ((x >= 0 && x <= PhoenixWindow.RESIZE_BORDER) && (y <= ownerWindow.getHeight() && y >= ownerWindow.getHeight() - PhoenixWindow.RESIZE_BORDER))
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Diagonal_Bottom_Right_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Top_Left;
			}
	
			// TitleTable
			if ((y >= ownerWindow.getHeight() - ownerWindow.getPadTop() && y <= ownerWindow.getHeight() - PhoenixWindow.RESIZE_BORDER)
					&& (x <= ownerWindow.getWidth() - PhoenixWindow.RESIZE_BORDER && x >= PhoenixWindow.RESIZE_BORDER))
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Arrow.getCursor();
				edge = WindowInputListener.WindowResizeMode.Move;
			}
	
			// bottom left
			if ((x >= 0 && x <= PhoenixWindow.RESIZE_BORDER) && (y >= 0 && y <= PhoenixWindow.RESIZE_BORDER))
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Diagonal_Bottom_Left_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Bottom_Left;
			}
	
			// bottom right
			if ((x <= ownerWindow.getWidth() && x >= ownerWindow.getWidth() - PhoenixWindow.RESIZE_BORDER) && (y >= 0 && y <= PhoenixWindow.RESIZE_BORDER))
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Diagonal_Bottom_Right_Resize.getCursor();
				edge = WindowInputListener.WindowResizeMode.Bottom_Right;
			}
	
			// center
			if ((x <= ownerWindow.getWidth() - PhoenixWindow.RESIZE_BORDER && x >= PhoenixWindow.RESIZE_BORDER)
					&& (y <= ownerWindow.getHeight() - ownerWindow.getPadTop() && y >= PhoenixWindow.RESIZE_BORDER))
			{
				ownerWindow.gameScreen.game.cursor = PhoenixCursor.Arrow.getCursor();
				edge = WindowInputListener.WindowResizeMode.None;
			}
		}
		
	}

	private void resizeLeft(float x, float y)
	{
		float amountX = x - startX;
		if (window.width - amountX < ownerWindow.getMinWidth())
			amountX = -(ownerWindow.getMinWidth() - window.width);
		if (clampPosition && window.x + amountX < 0)
			amountX = -window.x;
		window.width -= amountX;
		window.x += amountX;
	}

	private void resizeRight(float x, float y)
	{
		float amountX = x - lastX - window.width;
		if (window.width + amountX < ownerWindow.getMinWidth())
			amountX = ownerWindow.getMinWidth() - window.width;
		if (clampPosition && window.x + window.width + amountX > ownerWindow.getStage().getWidth())
			amountX = ownerWindow.getStage().getWidth() - window.x - window.width;
		window.width += amountX;
	}

	private void resizeTop(float x, float y)
	{
		float amountY = y - lastY - window.height;
		if (window.height + amountY < ownerWindow.getMinHeight())
			amountY = ownerWindow.getMinHeight() - window.height;
		if (clampPosition && window.y + window.height + amountY > ownerWindow.getStage().getHeight())
			amountY = ownerWindow.getStage().getHeight() - window.y - window.height;
		window.height += amountY;
	}

	private void resizeBottom(float x, float y)
	{
		float amountY = y - startY;
		if (window.height - amountY < ownerWindow.getMinHeight())
			amountY = -(ownerWindow.getMinHeight() - window.height);
		if (clampPosition && window.y + amountY < 0)
			amountY = -window.y;
		window.height -= amountY;
		window.y += amountY;
	}

	private void move(float x, float y)
	{
		float amountX = x - startX, amountY = y - startY;
		window.x += amountX;
		window.y += amountY;
	}

	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	{
		if (button == 0)
		{
			updateEdge(x, y);
			dragging = edge != WindowInputListener.WindowResizeMode.None;
			startX = x;
			startY = y;
			lastX = x - ownerWindow.getWidth();
			lastY = y - ownerWindow.getHeight();
		}
		return edge != WindowInputListener.WindowResizeMode.None || ownerWindow.isModal();
	}

	public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	{
		dragging = false;
	}

	public void touchDragged(InputEvent event, float x, float y, int pointer)
	{
		if (dragging)
		{
			window = new Rectangle(ownerWindow.getX(), ownerWindow.getY(), ownerWindow.getWidth(),
					ownerWindow.getHeight());
			clampPosition = ownerWindow.getKeepWithinStage() && ownerWindow.getStage() != null
					&& ownerWindow.getParent() == ownerWindow.getStage().getRoot();

			// System.out.println(edge);
			
			switch (edge)
			{
				case Move:
				{
					move(x, y);
					break;
				}
				case Left:
				{
					resizeLeft(x, y);
					break;
				}
				case Right:
				{
					resizeRight(x, y);
					break;
				}
				case Top:
				{
					resizeTop(x, y);
					break;
				}
				case Bottom:
				{
					resizeBottom(x, y);
					break;
				}
				case Bottom_Left:
				{
					resizeBottom(x, y);
					resizeLeft(x, y);
					break;
				}

				case Bottom_Right:
				{
					resizeBottom(x, y);
					resizeRight(x, y);
					break;
				}
				case Top_Left:
				{
					resizeTop(x, y);
					resizeLeft(x, y);
					break;
				}
				case Top_Right:
				{
					resizeTop(x, y);
					resizeRight(x, y);
					break;
				}
				case None:
				{
					break;
				}
			}
			ownerWindow.setBounds(Math.round(window.x), Math.round(window.y), Math.round(window.width),
					Math.round(window.height));
			
			// if the window was resized or moved, set "minimized" to false
			if(ownerWindow.isMinimized())
			{
				ownerWindow.setMinimized(false, false);
			}
		}
	}

	public boolean mouseMoved(InputEvent event, float x, float y)
	{
		updateEdge(x, y);
		return true;
	}

	public boolean scrolled(InputEvent event, float x, float y, int amount)
	{
		return false;
	}

	public boolean keyDown(InputEvent event, int keycode)
	{
		return ownerWindow.isModal();
	}

	public boolean keyUp(InputEvent event, int keycode)
	{
		return ownerWindow.isModal();
	}

	public boolean keyTyped(InputEvent event, char character)
	{
		return ownerWindow.isModal();
	}
}
