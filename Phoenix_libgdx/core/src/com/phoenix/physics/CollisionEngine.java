package com.phoenix.physics;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.systems.entitySystems.VelocitySystem;
import com.phoenix.utility.EntityUtility;

public class CollisionEngine
{
	public static Vector2 getCollisionRepulsionVector(Entity collider, Entity collided)
	{
		return CollisionEngine.getCollisionRepulsionVector(collider, collided, null);
	}

	public static Vector2 getCollisionRepulsionVector(Entity collider, Entity collided, ShapeRenderer debug)
	{
		CollisionHitboxComponent collidedHitbox = collided.getComponent(CollisionHitboxComponent.class);

		Vector2 repulsionVector = new Vector2();

		// VelocityComponent colliderVC =
		// collider.getComponent(VelocityComponent.class);
		// if (colliderVC != null)
		// {
		// Vector2 colliderPosition2D =
		// collider.getComponent(PositionComponent.class).pos2D;
		// Vector2 colliderVelocityVector =
		// collider.getComponent(VelocityComponent.class).velocity;
		// debug.setColor(Color.BLUE);
		// debug.line(colliderPosition2D,
		// colliderPosition2D.cpy().add(colliderVelocityVector));
		// debug.circle(colliderPosition2D.x + colliderVelocityVector.x,
		// colliderPosition2D.y + colliderVelocityVector.y, colliderVelocityVector.len()
		// / 10);
		// }

		switch (collidedHitbox.hitboxShape)
		{
			case "Circle":
			{
				repulsionVector = collideCircles(collider, collided, debug);
				break;
			}

			case "Polygon":
			{
				repulsionVector = collideCirclePolygon(collider, collided, debug);
				break;
			}
		}

		return repulsionVector;
	}

	private static Vector2 collideCircles(Entity collider, Entity collided, ShapeRenderer debug)
	{
		PositionComponent colliderPosition = collider.getComponent(PositionComponent.class);
		Vector2 colliderPosition2D = new Vector2(colliderPosition.pos2D);
		Vector2 colliderVelocityVector = collider.getComponent(VelocityComponent.class).velocity;

		PositionComponent collidedPosition = collided.getComponent(PositionComponent.class);
		Vector2 collidedPosition2D = new Vector2(collidedPosition.pos2D);

		Vector2 collisionVector = new Vector2().add(collidedPosition2D).sub(colliderPosition2D);

		float collisionVectorAngle = collisionVector.angle();
		float colliderVelocityVectorAngle = colliderVelocityVector.angle();

		if (collisionVectorAngle > 180)
		{
			collisionVectorAngle -= 180;
		}
		if (colliderVelocityVectorAngle > 180)
		{
			colliderVelocityVectorAngle -= 180;
		}

		float repulsionValue = (float) Math
				.cos(Math.toRadians(Math.abs(colliderVelocityVectorAngle - collisionVectorAngle)))
				* colliderVelocityVector.len();

		Vector2 repulsionVector = collisionVector.rotate(180).setLength(repulsionValue);

		debug.setColor(Color.RED);
		debug.line(collidedPosition2D, collidedPosition2D.cpy().add(repulsionVector));
		debug.circle(collidedPosition2D.x + repulsionVector.x, collidedPosition2D.y + repulsionVector.y,
				repulsionVector.len() / 10);

		return repulsionVector;
	}

	/**
	 * 
	 * @param collider assumed to be a Circle
	 * @param collided assumed to be a Polygon
	 * @param debug
	 * @return
	 */
	private static Vector2 collideCirclePolygon(Entity collider, Entity collided, ShapeRenderer debug)
	{
		PositionComponent colliderPosition = collider.getComponent(PositionComponent.class);
		
		float colliderRadius = collider.getComponent(CollisionHitboxComponent.class).size;
		
		ArrayList<Vector2> collidedVertices = collided.getComponent(TerrainComponent.class).vertices;
		
		Vector2 colliderPosition2D = new Vector2(colliderPosition.pos2D);
		Vector2 colliderVelocityVector = collider.getComponent(VelocityComponent.class).velocity;
		
		// determine which side of the polygon is intersecting with the circle
		
		Vector2 startVertex = new Vector2(), endVertex = new Vector2();
		
		for(int i = 0; i < collidedVertices.size(); i++)
		{
			Vector2 activeVertex = collidedVertices.get(i);
			Vector2 nextVertex = collidedVertices.get((i + 1) % collidedVertices.size());
			
			if(Intersector.intersectSegmentCircle(activeVertex, nextVertex, colliderPosition2D, (float)Math.pow(colliderRadius, 2)))
			{
				startVertex = activeVertex;
				endVertex = nextVertex;
				break;
			}
		}
		
		Vector2 repulsionVector = new Vector2();
		
		Intersector.intersectSegmentCircleDisplace(startVertex, endVertex, colliderPosition2D, colliderRadius, repulsionVector);
		
		float repulsionValue = colliderVelocityVector.len() * 1.25f;

		repulsionVector.setLength(repulsionValue);
		
		/*
		System.out.println("collider radius: " + colliderRadius);
		System.out.println("collider velocity: " + colliderVelocityVector.toString());
		System.out.println("repulsion value: " + repulsionValue);
		System.out.println("repulsion vector: " + repulsionVector.toString());
		*/
		
		debug.setColor(Color.CYAN);
		debug.line(colliderPosition2D,
		colliderPosition2D.cpy().add(repulsionVector));
				
		debug.circle(colliderPosition2D.x + repulsionVector.x, colliderPosition2D.y +
		repulsionVector.y,
		repulsionVector.len() / 10);
		
		return repulsionVector;
	}

	public static Shape2D getShapeFromEntity(Entity entity)
	{
		return CollisionEngine.getShapeFromEntity(entity, null);
	}

	public static Shape2D getShapeFromEntity(Entity entity, ShapeRenderer debug)
	{
		PositionComponent pc = entity.getComponent(PositionComponent.class);
		CollisionHitboxComponent chc = entity.getComponent(CollisionHitboxComponent.class);

		Shape2D shape = null;
		switch (chc.hitboxShape)
		{
			case "Circle":
			{
				shape = new Circle(pc.pos2D, chc.size);

				// if (debug != null)
				// {
				// debug.setColor(Color.ORANGE);
				// debug.circle(pc.pos2D.x, pc.pos2D.y, chc.size);
				// }
				break;
			}
			
			case "Polygon":
			{
				shape = EntityUtility.getPolygonFromTerrain(entity);
				
				break;
			}
		}
		return shape;
	}

}
