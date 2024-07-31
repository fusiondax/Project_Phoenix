package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.ResourceComponent;
import com.phoenix.screens.GameScreen;

public class ResourceAmountCounterRenderSystem extends IteratingSystem
{
	private ComponentMapper<ResourceComponent> rm = ComponentMapper.getFor(ResourceComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	
	private SpriteBatch batch;
	private BitmapFont resourceCountFont;
	
	public ResourceAmountCounterRenderSystem(GameScreen screen)
	{
		super(Family.all(ResourceComponent.class, PositionComponent.class).get());
		
		this.batch = screen.game.gameBatcher;
		this.resourceCountFont = new BitmapFont();
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		if (batch.isDrawing())
		{
			PositionComponent pos = pm.get(entity);
			ResourceComponent res = rm.get(entity);
			String countLabel = String.valueOf(res.resource.amount);
			
			this.resourceCountFont.draw(batch, countLabel, pos.pos2D.x, pos.pos2D.y);
		}
	}
}
