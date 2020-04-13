package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.GraphicComponent;

public class RenderSystem extends IteratingSystem
{
	private ComponentMapper<GraphicComponent> gm = ComponentMapper.getFor(GraphicComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	
	private SpriteBatch batch;
	private OrthographicCamera cam;
	
	public RenderSystem(SpriteBatch batch)
	{
		super(Family.all(GraphicComponent.class, PositionComponent.class).get());
		
		this.batch = batch;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		GraphicComponent graph = gm.get(entity);
		PositionComponent pos = pm.get(entity);
		
		TextureRegion region = new TextureRegion(graph.texture);
		
		float width = graph.texture.getWidth();
		float height = graph.texture.getHeight();
		float originX = width * -0.5f;
		float originY = height * -0.5f;
		
		batch.draw(region, 
					pos.pos.x + originX, pos.pos.y + originY, 
					0, 0, 
					width, height, 
					1, 1,
					0);
	}
}
