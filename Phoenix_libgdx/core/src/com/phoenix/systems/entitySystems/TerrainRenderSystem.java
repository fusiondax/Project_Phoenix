package com.phoenix.systems.entitySystems;

import java.util.Iterator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.TextureComponent;
import com.phoenix.screens.GameScreen;
import com.phoenix.utility.EntityUtility;

public class TerrainRenderSystem extends IteratingSystem
{
	private ComponentMapper<TextureComponent> texm = ComponentMapper.getFor(TextureComponent.class);
	private ComponentMapper<TerrainComponent> term = ComponentMapper.getFor(TerrainComponent.class);
	
	private GameScreen gameScreen;
	private SpriteBatch batch;
	
	public TerrainRenderSystem(GameScreen screen)
	{
		super(Family.all(TextureComponent.class, TerrainComponent.class).get());
		this.batch = screen.game.gameBatcher;
		this.gameScreen = screen;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		TextureComponent texture = texm.get(entity);
		TerrainComponent terrain = term.get(entity);
		
		// TODO 1 quick dirty debug stuff, plz remove
		Color c = Color.BLUE;
		
		switch(terrain.heightPriority)
		{
			case 0:
			{
				c = Color.GREEN;
				
				break;
			}
			
			case 1:
			{
				c = Color.GRAY;
				break;
			}
		}
		
		gameScreen.shapeRendererLine.setColor(c);
		
		gameScreen.shapeRendererLine.polygon(EntityUtility.getPolygonFromTerrain(entity).getVertices());
		
		//TODO 2 texture is not being handled for terrains
		/*
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
		*/
	}
}
