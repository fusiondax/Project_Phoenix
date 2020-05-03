package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.phoenix.components.BuildableComponent;
import com.phoenix.components.NameComponent;
import com.phoenix.screens.GameScreen;

public class EntityBuildingSystem extends IteratingSystem
{
	private ComponentMapper<BuildableComponent> bcm = ComponentMapper.getFor(BuildableComponent.class);

	private GameScreen gameScreen;

	public EntityBuildingSystem(GameScreen gs)
	{
		super(Family.all(BuildableComponent.class).get());
		this.gameScreen = gs;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		BuildableComponent bc = bcm.get(entity);

		// TODO add mechanisms that prevents non-fully built entities to not update
		// their AI components and Command components
		// TODO think about how the health of an entity

		// if the entity is being built or being salvaged
		if (bc.getBuildRate() != 0.0f)
		{
			NameComponent nameComp = entity.getComponent(NameComponent.class);
			// the buildRate should be affected by the deltaTime... I think
			bc.setBuildProgress(bc.getBuildProgress() + (bc.getBuildRate() * deltaTime));

			System.out.println("entity " + nameComp.name + "is built at: " + bc.getBuildProgress() * 100
					+ "%\n at a rate of: " + bc.getBuildRate() * 100 + "% per updates");

			// if the unit is done being built
			if (bc.getBuildProgress() <= 0.0)
			{
				bc.setBuildProgress(0.0f);
				bc.setBuildRate(0.0f);

			}

			// if the unit is done being salvaged
			if (bc.getBuildProgress() >= 1.0)
			{
				bc.setBuildProgress(0.0f);
				bc.setBuildRate(0.0f);
			}
		}
	}
}
