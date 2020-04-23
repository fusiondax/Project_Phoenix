package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;

public class MovementSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

	public MovementSystem()
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent position = pm.get(entity);
		VelocityComponent velocity = vm.get(entity);

		Vector2 upcomingMovement = new Vector2().add(velocity.velocity).scl(deltaTime);
		
		
		position.pos.x += upcomingMovement.x;
		position.pos.y += upcomingMovement.y;
		
	}
}
