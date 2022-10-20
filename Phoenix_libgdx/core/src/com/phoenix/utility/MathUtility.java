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
