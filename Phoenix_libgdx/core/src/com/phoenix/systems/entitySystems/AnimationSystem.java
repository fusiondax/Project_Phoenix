package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.phoenix.components.AnimationComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TextureComponent;

public class AnimationSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
	private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
	
	public AnimationSystem()
	{
		super(Family.all(PositionComponent.class, TextureComponent.class, AnimationComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent entityPos = pm.get(entity);
		TextureComponent entityTex = tm.get(entity);
		AnimationComponent entityAnim = am.get(entity);
		
	}

}
