package com.phoenix.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class CollisionDetector
{
	public static boolean isShapeCollisionShape(Shape2D shape1, Shape2D shape2)
	{
		// TODO 4 this is super ugly, and I don't know how to make it cleaner...
		boolean intersected = false;
		
		if(shape1 instanceof Circle)
		{
			if(shape2 instanceof Circle)
			{
				intersected = Intersector.overlaps((Circle)shape1, (Circle)shape2);
			}
			else if(shape2 instanceof Polygon)
			{
				intersected = CollisionDetector.isCircleCollisionPolygon((Polygon)shape2, (Circle)shape1);
			}
		}
		else if (shape1 instanceof Polygon)
		{
			if(shape2 instanceof Circle)
			{
				intersected = CollisionDetector.isCircleCollisionPolygon((Polygon)shape2, (Circle)shape1);
			}
			else if(shape2 instanceof Polygon)
			{
				intersected = Intersector.overlapConvexPolygons((Polygon)shape1, (Polygon)shape2);
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

	public static boolean isSegmentCollisionRectangles(Vector2 segmentStart, Vector2 segmentEnd,
			ArrayList<Rectangle> rects)
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

	
	// TODO 4 took this one off the internet
	public static boolean isCircleCollisionPolygon(Polygon p, Circle c)
	{
		float[] vertices = p.getTransformedVertices();
		Vector2 center = new Vector2(c.x, c.y);
		float squareRadius = c.radius * c.radius;
		for (int i = 0; i < vertices.length; i += 2)
		{
			if (i == 0)
			{
				if (Intersector.intersectSegmentCircle(
						new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]),
						new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
					return true;
			}
			else
			{
				if (Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]),
						new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
					return true;
			}
		}
		return false;
	}
}
