package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.GraphicComponent;

public class RenderSystem extends IteratingSystem
{
	private ComponentMapper<GraphicComponent> gm = ComponentMapper.getFor(GraphicComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<SelectionComponent> sm = ComponentMapper.getFor(SelectionComponent.class);
	private ComponentMapper<CollisionHitboxComponent> chm = ComponentMapper.getFor(CollisionHitboxComponent.class);
	
	private SpriteBatch batch;
	private ShapeRenderer debug;
	
	public RenderSystem(GameScreen screen)
	{
		super(Family.all(GraphicComponent.class, PositionComponent.class).get());
		
		this.batch = screen.game.gameBatcher;
		this.debug = screen.shapeRenderer;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		AssetManager manager = PhoenixAssetManager.getInstance().manager;
		GraphicComponent graph = gm.get(entity);
		PositionComponent pos = pm.get(entity);
		SelectionComponent select = sm.get(entity);
		CollisionHitboxComponent hitbox = chm.get(entity);
		
		TextureAtlas texAtlas = manager.get("graphics/atlas/entities.atlas");
		Sprite sprite = texAtlas.createSprite(graph.textureName);

		if(select != null && hitbox != null)
		{
			if(select.selected)
			{
				debug.setColor(Color.GREEN);
				debug.circle(pos.pos.x, pos.pos.y, hitbox.size + 1);
			}
		}
		
		float width = sprite.getRegionWidth();
		float height = sprite.getRegionHeight();
		float originX = width * -0.5f;
		float originY = height * -0.5f;
		
		
		if(hitbox != null)
		{
			debug.setColor(Color.ORANGE);
			if(hitbox.hitboxShape.equals("Rectangle"))
			{
				debug.rect(pos.pos.x + originX, pos.pos.y + originY, hitbox.size, hitbox.size);
			}
			else if(hitbox.hitboxShape.equals("Circle"))
			{
				debug.circle(pos.pos.x, pos.pos.y, hitbox.size);
			}
		}
		batch.draw(sprite, 
					pos.pos.x + originX, pos.pos.y + originY, 
					0, 0, 
					width, height, 
					1, 1,
					0);
	}
}
