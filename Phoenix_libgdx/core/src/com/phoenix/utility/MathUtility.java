package com.phoenix.utility;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MathUtility
{

	public static float roundFloat(float originalFloat)
	{
		DecimalFormat format = new DecimalFormat("0.00");
		
		return Float.parseFloat(format.format(originalFloat).replace(',', '.'));
	}
	
	public static Vector2 getWorldPositionFromScreenLocation(int screenX, int screenY, OrthographicCamera cam)
	{
		Vector2 worldPos = new Vector2();
		
		Vector2 adjustedScreenPos = getCenterScreenOriginScreenPos(screenX, screenY);
		
		worldPos.x = cam.position.x + ((cam.viewportWidth / 2) * adjustedScreenPos.x);
		worldPos.y = cam.position.y + ((cam.viewportHeight / 2) * adjustedScreenPos.y);
		
		return worldPos;
	}
	
	public static Vector2 getCenterScreenOriginScreenPos(int screenX, int screenY)
	{
		Vector2 adjustedScreenPos = new Vector2();
		
		adjustedScreenPos.x = screenX - (Gdx.graphics.getWidth() / 2);
		adjustedScreenPos.y = (screenY - Gdx.graphics.getHeight() / 2) * -1.0f;
		
		adjustedScreenPos.x /= (float) (Gdx.graphics.getWidth() / 2);
		adjustedScreenPos.y /= (float) (Gdx.graphics.getHeight() / 2);
		
		return adjustedScreenPos;
	}
	
	/**
	 * found some bug/limitation with the Rectangle class
	 * @return
	 */
	
	public static Rectangle readjustRectangle(Rectangle rect)
	{
		Rectangle newRect = new Rectangle(rect);
		
		if(rect.height < 0)
		{
			newRect.y = rect.y - Math.abs(rect.height);
			newRect.height = Math.abs(rect.height);
		}
		
		if(rect.width < 0)
		{
			newRect.x = rect.x - Math.abs(rect.width);
			newRect.width = Math.abs(rect.width);
		}
		
		return newRect;
	}
}
