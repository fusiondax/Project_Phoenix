package com.phoenix.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.systems.entitySystems.VelocitySystem;

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
		// colliderPosition2D.y + colliderVelocityVector.y,
		// colliderVelocityVector.len() / 10);
		// }

		switch (collidedHitbox.hitboxShape)
		{
			case "Circle":
			{
				repulsionVector = collideCircles(collider, collided, debug);
				break;
			}

			case "Rectangle":
			{
				repulsionVector = collideRectangles(collider, collided, debug);
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

//		debug.setColor(Color.RED);
//		debug.line(collidedPosition2D, collidedPosition2D.cpy().add(repulsionVector));
//		debug.circle(collidedPosition2D.x + repulsionVector.x, collidedPosition2D.y + repulsionVector.y,
//				repulsionVector.len() / 10);

		return repulsionVector;
	}

	private static Vector2 collideRectangles(Entity collider, Entity collided, ShapeRenderer debug)
	{
		PositionComponent colliderPosition = collider.getComponent(PositionComponent.class);
		Vector2 colliderPosition2D = new Vector2(colliderPosition.pos2D);
		Vector2 colliderVelocityVector = collider.getComponent(VelocityComponent.class).velocity;

		PositionComponent collidedPosition = collided.getComponent(PositionComponent.class);
		Vector2 collidedPosition2D = new Vector2(collidedPosition.pos2D);

		Vector2 repulsionVector = new Vector2();
		
		Vector2 collisionVector = new Vector2().add(colliderPosition2D).sub(collidedPosition2D);

//		float angle = collisionVector.angle();
//
//		// collider coming from the right
//		if (angle > 315 || angle < 45)
//		{
//			if (colliderVelocityVector.x < 0)
//			{
//				repulsionVector.x = -colliderVelocityVector.x;
//			}
//		}
//		// collider coming from the left
//		else if (angle > 135 && angle < 225)
//		{
//			if (colliderVelocityVector.x > 0)
//			{
//				repulsionVector.x = -colliderVelocityVector.x;
//			}
//		}
//		// collider coming from the bottom
//		else if (angle > 225 && angle < 315)
//		{
//			if (colliderVelocityVector.y > 0)
//			{
//				repulsionVector.y = -colliderVelocityVector.y;
//			}
//		}
//		// collider coming from the top
//		else if ((angle > 45 && angle < 135))
//		{
//			if (colliderVelocityVector.y < 0)
//			{
//				repulsionVector.y = -colliderVelocityVector.y;
//			}
//		}
		float repulsionValue = colliderVelocityVector.len() * 1.25f;

		repulsionVector.set(collisionVector);

		repulsionVector.setLength(repulsionValue);

//		debug.setColor(Color.CYAN);
//		debug.line(collidedPosition2D, collidedPosition2D.cpy().add(repulsionVector));
//
//		debug.circle(collidedPosition2D.x + repulsionVector.x, collidedPosition2D.y + repulsionVector.y,
//				repulsionVector.len() / 10);

//		debug.setColor(Color.BLUE);
//		debug.line(collidedPosition2D, collidedPosition2D.cpy().add(collisionVector));
//
//		debug.circle(collidedPosition2D.x + collisionVector.x, collidedPosition2D.y + collisionVector.y,
//				collisionVector.len() / 10);

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

			case "Rectangle":
			{
				shape = new Rectangle(pc.pos2D.x - chc.size / 2, pc.pos2D.y - chc.size / 2, chc.size, chc.size);

				// if (debug != null)
				// {
				// debug.setColor(Color.ORANGE);
				// debug.rect(pc.pos2D.x - chc.size / 2, pc.pos2D.y - chc.size / 2, chc.size,
				// chc.size);
				// }
				break;
			}
		}
		return shape;
	}

}
