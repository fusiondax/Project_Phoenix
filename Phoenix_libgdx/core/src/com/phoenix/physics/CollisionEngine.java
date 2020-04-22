package com.phoenix.physics;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;

public class CollisionEngine
{
	public static Vector2 getCollisionRepulsionVector(Entity collider, Entity collided, Vector2 colliderVelocityVector, ShapeRenderer debug)
	{
		PositionComponent colliderPosition = collider.getComponent(PositionComponent.class);
		CollisionHitboxComponent colliderHitbox = collider.getComponent(CollisionHitboxComponent.class);
		Vector2 colliderPosition2D = new Vector2(colliderPosition.pos.x, colliderPosition.pos.y);

		PositionComponent collidedPosition = collided.getComponent(PositionComponent.class);
		CollisionHitboxComponent collidedHitbox = collided.getComponent(CollisionHitboxComponent.class);
		Vector2 collidedPosition2D = new Vector2(collidedPosition.pos.x, collidedPosition.pos.y);

		Vector2 collisionVector = new Vector2().add(collidedPosition2D).sub(colliderPosition2D);

		float collisionVectorAngle = collisionVector.angle();
		float colliderVelocityVectorAngle = colliderVelocityVector.angle();
		
		if(collisionVectorAngle > 180)
		{
			collisionVectorAngle -= 180;
		}
		if(colliderVelocityVectorAngle > 180)
		{
			colliderVelocityVectorAngle -= 180;
		}
		
		float repulsionValue = (float) Math
				.cos(Math.toRadians(Math.abs(colliderVelocityVectorAngle - collisionVectorAngle))) * colliderVelocityVector.len();

		Vector2 repulsionVector = collisionVector.rotate(180).setLength(repulsionValue);

//		debug.setColor(Color.BLUE);
//		debug.line(new Vector2(), repulsionVector);
//		debug.circle(repulsionVector.x, repulsionVector.y, 10);

		return repulsionVector;
	}

	public static Vector2 collideCircles()
	{
		return null;
	}

}
