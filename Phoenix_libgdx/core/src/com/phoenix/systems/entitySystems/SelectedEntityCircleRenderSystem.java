package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.screens.GameScreen;

public class SelectedEntityCircleRenderSystem extends IteratingSystem
{
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<SelectionComponent> sm = ComponentMapper.getFor(SelectionComponent.class);
	private ComponentMapper<CollisionHitboxComponent> chm = ComponentMapper.getFor(CollisionHitboxComponent.class);

	private GameScreen gameScreen;

	public SelectedEntityCircleRenderSystem(GameScreen screen)
	{
		super(Family.all(PositionComponent.class, SelectionComponent.class, CollisionHitboxComponent.class).get());
		this.gameScreen = screen;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent pos = pm.get(entity);
		SelectionComponent select = sm.get(entity);
		CollisionHitboxComponent hitbox = chm.get(entity);

		if (select.selected)
		{
			gameScreen.shapeRendererLine.setColor(Color.GREEN);
			gameScreen.shapeRendererLine.circle(pos.pos.x, pos.pos.y, hitbox.size + 1);
		}
	}
}
