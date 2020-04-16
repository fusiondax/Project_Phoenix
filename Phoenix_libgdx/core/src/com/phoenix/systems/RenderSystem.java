package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.SelectionComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.GraphicComponent;
import com.phoenix.components.HitboxComponent;

public class RenderSystem extends IteratingSystem
{
	private ComponentMapper<GraphicComponent> gm = ComponentMapper.getFor(GraphicComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<SelectionComponent> sm = ComponentMapper.getFor(SelectionComponent.class);
	private ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	
	public RenderSystem(GameScreen screen)
	{
		super(Family.all(GraphicComponent.class, PositionComponent.class).get());
		
		this.batch = screen.game.gameBatcher;
		this.shapeRenderer = screen.shapeRenderer;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		AssetManager manager = PhoenixAssetManager.getInstance().manager;
		GraphicComponent graph = gm.get(entity);
		PositionComponent pos = pm.get(entity);
		SelectionComponent select = sm.get(entity);
		HitboxComponent hitbox = hm.get(entity);
		TextureAtlas texAtlas = manager.get("graphics/atlas/entities.atlas");
		Sprite sprite = texAtlas.createSprite(graph.textureName);

		if(select != null && hitbox != null)
		{
			if(select.selected)
			{
				shapeRenderer.circle(pos.pos.x, pos.pos.y, hitbox.radius);
			}
		}
		
		float width = sprite.getRegionWidth();
		float height = sprite.getRegionHeight();
		float originX = width * -0.5f;
		float originY = height * -0.5f;
		
		batch.draw(sprite, 
					pos.pos.x + originX, pos.pos.y + originY, 
					0, 0, 
					width, height, 
					1, 1,
					0);
	}
}
