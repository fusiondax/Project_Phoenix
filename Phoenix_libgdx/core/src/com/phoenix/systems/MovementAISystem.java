package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.components.HitboxComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.VelocityComponent;

public class MovementAISystem extends IteratingSystem
{
	private ComponentMapper<MovementAIComponent> mam = ComponentMapper.getFor(MovementAIComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);
	
	private ShapeRenderer debug;
	
	public MovementAISystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class, MovementAIComponent.class, HitboxComponent.class).get());
		this.debug = debug;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		MovementAIComponent ai = mam.get(entity);
		if(!ai.destinations.isEmpty())
		{
			
			PositionComponent position = pm.get(entity);
			VelocityComponent velocity = vm.get(entity);
			HitboxComponent hitbox = hm.get(entity);
			
			Vector2 position2d = new Vector2(position.pos.x, position.pos.y);
			float unitMaxSpeed = ai.unitMaxSpeed;
			
			Vector2 nextDestination = ai.destinations.get(0);
			
			if(nextDestination.dst(position2d) >= hitbox.radius / 2) 
			{
				Vector2 velocityVector = new Vector2(nextDestination.x - position2d.x, nextDestination.y - position2d.y);
				velocityVector.setLength(unitMaxSpeed);
				
				
				debug.setColor(Color.GREEN);
				debug.line(position2d, nextDestination);
				
				//debug.setColor(Color.RED);
				//debug.line(new Vector2(), velocityVector);
				
				velocity.velocity.set(velocityVector);
				
				//System.out.println(velocityVector.angle());
				
			}
			else //unit's hitbox is at destination, remove current destination point and stops the unit there
			{
				ai.destinations.remove(0);
				velocity.velocity.setZero();
			}
		}
		
		
		
		
		
	}

}
