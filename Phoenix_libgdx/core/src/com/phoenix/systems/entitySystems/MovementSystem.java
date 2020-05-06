package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.MovementComponent;

public class MovementSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<MovementComponent> vm = ComponentMapper.getFor(MovementComponent.class);

	private ShapeRenderer debug;
	public MovementSystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, MovementComponent.class).get());
		this.debug = debug;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent position = pm.get(entity);
		MovementComponent movement = vm.get(entity);
		
		Vector2 upcomingMovement = new Vector2().add(movement.velocity).scl(deltaTime);
		
		//the position is modified by the velocity
		position.pos2D.add(upcomingMovement);
	}
}
