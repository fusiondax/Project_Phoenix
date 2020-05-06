package com.phoenix.physics;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.ValidTerrainTypesComponent;

public class CollisionDetector
{
	public static boolean isShapeCollisionShape(Shape2D shape1, Shape2D shape2)
	{
		// this is super ugly, and I don't know how to make it cleaner...
		boolean intersected = false;
		
		if(shape1 instanceof Circle)
		{
			if(shape2 instanceof Circle)
			{
				intersected = Intersector.overlaps((Circle)shape1, (Circle)shape2);
			}
			else if(shape2 instanceof Rectangle)
			{
				intersected = Intersector.overlaps((Circle)shape1, (Rectangle)shape2);
			}
		}
		else if (shape1 instanceof Rectangle)
		{
			if(shape2 instanceof Circle)
			{
				intersected = Intersector.overlaps((Circle)shape2, (Rectangle)shape1);
			}
			else if(shape2 instanceof Rectangle)
			{
				intersected = Intersector.overlaps((Rectangle)shape1, (Rectangle)shape2);
			}
		}
		return intersected;
	}
	
	public static boolean isCircleCollisionRectangles(Circle entityHitbox, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;

		for (Rectangle rect : rects)
		{
			if (Intersector.overlaps(entityHitbox, rect))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}

	public static boolean isPointCollisionRectangles(Vector2 targetLocation, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;

		for (Rectangle rect : rects)
		{
			if (rect.contains(targetLocation))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}
	
	public static boolean isSegmentCollisionRectangles(Vector2 segmentStart, Vector2 segmentEnd, ArrayList<Rectangle> rects)
	{
		boolean isCollision = false;

		for (Rectangle rect : rects)
		{
			if (Intersector.intersectSegmentRectangle(segmentStart, segmentEnd, rect))
			{
				isCollision = true;
				break;
			}
		}
		return isCollision;
	}
}
