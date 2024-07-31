package com.phoenix.systems.entitySystems;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phoenix.components.ParticleComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.screens.GameScreen;

public class ParticleRenderSystem extends IteratingSystem
{
	private ComponentMapper<ParticleComponent> pam = ComponentMapper.getFor(ParticleComponent.class);
	private ComponentMapper<PositionComponent> pom = ComponentMapper.getFor(PositionComponent.class);
	
	private SpriteBatch batch;
	
	private ArrayList<ParticleEffect> particleEffects = new ArrayList<ParticleEffect>();
	
	//TODO 4 test particle stuff, remove later
	private boolean TEST = false;
	
	public ParticleRenderSystem(GameScreen screen)
	{
		super(Family.all(ParticleComponent.class, PositionComponent.class).get());
		this.batch = screen.game.gameBatcher;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		if (batch.isDrawing())
		{
			ParticleComponent particle = pam.get(entity);
			PositionComponent position = pom.get(entity);
			
			// check for new particle effect to add
			if(TEST)
			{
				initializeParticle(particle.particleName);
				TEST = false;
			}
			
			// check for 'flagged' particle effects to remove
			
			
			
			// update existing particle effects
			for(ParticleEffect pe : this.particleEffects)
			{
				pe.getEmitters().first().setPosition(position.pos2D.x,position.pos2D.y);
				pe.update(Gdx.graphics.getDeltaTime());
				pe.draw(batch);
				
				if (pe.isComplete())
				{
					pe.reset();
				}
			}
		}
	}
	
	// TODO 3 testing method for particles, revamp or destroy
	/**
	 * this is testing code to get a feel of how Particles work, this is not meant to last and should be revamped, if not completely removed.
	 */
	private void initializeParticle(String particleFileName)
	{
		 ParticleEffect pe = new ParticleEffect();
		 pe.load(Gdx.files.internal("particles/" + particleFileName + ".particle"),Gdx.files.internal(""));
		 
		 pe.start();
		 
		 this.particleEffects.add(pe);
	}
}
