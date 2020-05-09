package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;

public class VelocitySystem extends IteratingSystem
{
	public static final float MAX_SPEED = 1000;
	
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

	private ShapeRenderer debug;
	public VelocitySystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class).get(), 2);
		this.debug = debug;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent position = pm.get(entity);
		VelocityComponent movement = vm.get(entity);
		
		Vector2 resultingMovement = new Vector2();
		
		resultingMovement.add(movement.velocity).scl(deltaTime);
		
		//the position is modified by the velocity
		position.pos2D.add(resultingMovement);
		
//		debug.setColor(Color.BLACK);
//		debug.line(position.pos2D, position.pos2D.cpy().add(movement.velocity));
//		debug.circle(position.pos2D.x + movement.velocity.x, position.pos2D.y + movement.velocity.y, movement.velocity.len() / 10);
		
		movement.velocity.setZero();
		
		//friction is applied to the velocity vector
		
//		if(movement.velocity.len() > 0)
//		{
//			movement.velocity.scl(1 - movement.friction);
//			System.out.println("friction: " + movement.friction);
//			System.out.println("velocity: " + movement.velocity);
//		}
	}
}
