package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.CollisionComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.pathfinding.CollisionDetector;

public class MovementSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
	
	private Engine engine;
	
	public MovementSystem(Engine engine) 
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
		this.engine = engine;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		PositionComponent position = pm.get(entity);
		VelocityComponent velocity = vm.get(entity);
		CollisionComponent collision = cm.get(entity);
		
		Vector2 upcomingMovement = new Vector2(velocity.velocity.x * deltaTime, velocity.velocity.y * deltaTime);

		CollisionDetector detector = new CollisionDetector(engine);
		
		if(collision != null)
		{
			detector.isPointCollision(upcomingMovement, rects);
		}
		
		position.pos.x += upcomingMovement.x;
		position.pos.y += upcomingMovement.y;
	}
	
}
