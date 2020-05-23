package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.phoenix.components.PositionComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.TextureComponent;

public class TextureRenderSystem extends IteratingSystem
{
	private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

	private SpriteBatch batch;

	public TextureRenderSystem(GameScreen screen)
	{
		super(Family.all(TextureComponent.class, PositionComponent.class).get());

		this.batch = screen.game.gameBatcher;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		if (batch.isDrawing())
		{
			AssetManager manager = PhoenixAssetManager.getInstance().manager;
			TextureComponent graph = tm.get(entity);
			PositionComponent pos = pm.get(entity);

			TextureAtlas texAtlas = manager.get("graphics/atlas/entities.atlas");
			Sprite sprite = texAtlas.createSprite(graph.textureName);

			float width = sprite.getRegionWidth();
			float height = sprite.getRegionHeight();
			float originX = width * -0.5f;
			float originY = height * -0.5f;

			batch.draw(sprite, pos.pos2D.x + originX, pos.pos2D.y + originY, 0, 0, width, height, 1, 1, 0);
		}
	}
}
