package com.phoenix.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.components.GraphicComponent;
import com.phoenix.components.HitboxComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
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
			Engine engine = getEngine();
			PositionComponent position = pm.get(entity);
			VelocityComponent velocity = vm.get(entity);
			HitboxComponent hitbox = hm.get(entity);
			
			Vector2 entityPosition = new Vector2(position.pos.x, position.pos.y);
			float unitMaxSpeed = ai.unitMaxSpeed;
			
			Vector2 nextDestination = ai.destinations.get(0);
			
			if(nextDestination.dst(entityPosition) >= hitbox.radius / 2) 
			{
				Vector2 velocityVector = new Vector2(nextDestination.x - entityPosition.x, nextDestination.y - entityPosition.y);
				velocityVector.setLength(unitMaxSpeed);
				AssetManager manager = PhoenixAssetManager.getInstance().manager;
				
				debug.setColor(Color.GREEN);
				debug.line(entityPosition, nextDestination);
				
				//debug.setColor(Color.RED);
				//debug.line(new Vector2(), velocityVector);
				
				velocity.velocity.set(velocityVector);
				
				//get terrain types of terrain currently on
				
				Circle entityHitbox = new Circle(entityPosition, hitbox.radius);
				//TODO potentially very poorly optimized
				ImmutableArray<Entity> terrainEntities = engine.getEntitiesFor(Family.all(TerrainComponent.class).get());
				
				for(Entity e : terrainEntities)
				{
					PositionComponent terrainPos = e.getComponent(PositionComponent.class);
					GraphicComponent graph = e.getComponent(GraphicComponent.class);
					
					//TODO Weird, shitty way to get width/height for terrains
					
					TextureAtlas texAtlas = manager.get("graphics/atlas/entities.atlas");
					TextureRegion region = texAtlas.createSprite(graph.textureName);
					
					int width = region.getRegionWidth(); 
					int height = region.getRegionHeight();
					
					Rectangle terrainRect = new Rectangle(terrainPos.pos.x - width / 2, terrainPos.pos.y - height / 2, width, height);
					
					//debug.rect(terrainRect.x, terrainRect.y, terrainRect.width, terrainRect.height);
					
					if(terrainRect.contains(entityPosition))
					{
						TerrainComponent t = e.getComponent(TerrainComponent.class);
						
						//System.out.println(t.types.toString());
					}
					
				}
			}
			else //unit's hitbox is at destination, remove current destination point and stops the unit there
			{
				ai.destinations.remove(0);
				velocity.velocity.setZero();
			}
		}
	}

}
