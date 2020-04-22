package com.phoenix.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.physics.CollisionEngine;

public class MovementSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionHitboxComponent> cm = ComponentMapper.getFor(CollisionHitboxComponent.class);

	private ShapeRenderer debug;

	public MovementSystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
		this.debug = debug;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent position = pm.get(entity);
		VelocityComponent velocity = vm.get(entity);
		CollisionHitboxComponent collision = cm.get(entity);

		Engine engine = getEngine();

		Vector2 entityPosition = new Vector2(position.pos.x, position.pos.y);
		Vector2 upcomingMovement = new Vector2(velocity.velocity.x * deltaTime, velocity.velocity.y * deltaTime);
		Vector2 upcomingPosition = new Vector2().add(upcomingMovement).setLength(collision.size).add(entityPosition);

		// if the unit is not moving, skip this process entirely
		if (!entityPosition.equals(upcomingPosition))
		{
			// if this moving entity has a collision hitbox...
			if (collision != null)
			{
				CollisionDetector detector = new CollisionDetector(engine);

				// get entities that are relatively close the the current entity and that have
				// collision hitboxes. the range
				// multiplier is guesswork...

				ArrayList<Entity> proximityEntities = detector.getProxyEntities(entityPosition, collision.size * 5,
						Family.all(CollisionHitboxComponent.class).get());

				// remove self from list of close by entities...
				proximityEntities.remove(entity);

				Shape2D hitbox = getShapeFromEntity(entity);

				for (Entity e : proximityEntities)
				{
					Shape2D shape = getShapeFromEntity(e);

					if (CollisionDetector.isShapeCollisionShape(hitbox, shape))
					{
						upcomingMovement.add(CollisionEngine.getCollisionRepulsionVector(entity, e, upcomingMovement, debug)); 
					}
				}
			}
			position.pos.x += upcomingMovement.x;
			position.pos.y += upcomingMovement.y;
		}
	}

	private Shape2D getShapeFromEntity(Entity entity)
	{
		PositionComponent pc = entity.getComponent(PositionComponent.class);
		CollisionHitboxComponent chc = entity.getComponent(CollisionHitboxComponent.class);

		Shape2D shape = null;
		switch (chc.hitboxShape)
		{
			case "Circle":
			{
				shape = new Circle(pc.pos.x, pc.pos.y, chc.size);
				debug.setColor(Color.ORANGE);
				debug.circle(pc.pos.x, pc.pos.y, chc.size);

				break;
			}

			case "Rectangle":
			{
				shape = new Rectangle(pc.pos.x - chc.size / 2, pc.pos.y - chc.size / 2, chc.size, chc.size);

				debug.setColor(Color.ORANGE);
				debug.rect(pc.pos.x - chc.size / 2, pc.pos.y - chc.size / 2, chc.size, chc.size);

				break;
			}
		}

		return shape;
	}

}
