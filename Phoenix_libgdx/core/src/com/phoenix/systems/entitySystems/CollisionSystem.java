package com.phoenix.systems.entitySystems;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollectibleBlueprintComponent;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.NameComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.physics.CollisionEngine;

public class CollisionSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<CollisionHitboxComponent> cm = ComponentMapper.getFor(CollisionHitboxComponent.class);

	private ShapeRenderer debug;
	
	public CollisionSystem()
	{
		this(null);
	}
	
	public CollisionSystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, CollisionHitboxComponent.class).get());
		this.debug = debug;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent position = pm.get(entity);
		CollisionHitboxComponent collision = cm.get(entity);
		
		//TODO this is for debug purposes
		debug.setColor(Color.ORANGE);

		if (collision.hitboxShape.equals("Rectangle"))
		{
			debug.rect(position.pos.x - collision.size / 2, position.pos.y - collision.size / 2, collision.size,
					collision.size);
		}
		else if (collision.hitboxShape.equals("Circle"))
		{
			debug.circle(position.pos.x, position.pos.y, collision.size);
		}
		
		
		Vector2 entityPosition = new Vector2(position.pos.x, position.pos.y);
		
		Engine engine = getEngine();
		CollisionDetector detector = new CollisionDetector(engine);

		// get entities that are relatively close the the current entity and that have
		// collision hitboxes. the range
		// multiplier is guesswork...

		ArrayList<Entity> proximityEntities = detector.getProxyEntities(entityPosition, collision.size * 5,
				Family.all(CollisionHitboxComponent.class).get());
		
		// remove self from list of close by entities...
		proximityEntities.remove(entity);
		
		Shape2D hitbox = CollisionEngine.getShapeFromEntity(entity);

		// TODO collision between more than 3 entities is bugged
		for (Entity e : proximityEntities)
		{
			Shape2D shape = CollisionEngine.getShapeFromEntity(e);

			if (CollisionDetector.isShapeCollisionShape(hitbox, shape))
			{
				
				VelocityComponent proxyEntityVC = e.getComponent(VelocityComponent.class);
				CollectibleBlueprintComponent proxyEntityCBC = e.getComponent(CollectibleBlueprintComponent.class);
				
				if(proxyEntityVC != null)
				{
					Vector2 repulsionForce = CollisionEngine.getCollisionRepulsionVector(e, entity);
					
//					System.out.println(entity.getComponent(NameComponent.class).name + " is pushing " 
//							+ e.getComponent(NameComponent.class).name + " who has velocity " + proxyEntityVC.velocity);
					
					proxyEntityVC.velocity.add(repulsionForce);
					
//					System.out.println(entity.getComponent(NameComponent.class).name + " is pushing on " 
//							+ e.getComponent(NameComponent.class).name + " with force " + repulsionForce.toString());
				}
				
				if(proxyEntityCBC != null)
				{
					proxyEntityCBC.collector = entity;
				}
			}
		}
	}
}