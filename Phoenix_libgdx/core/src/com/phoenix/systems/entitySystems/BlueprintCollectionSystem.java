package com.phoenix.systems.entitySystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.phoenix.blueprint.Blueprint;
import com.phoenix.components.CollectibleBlueprintComponent;
import com.phoenix.components.OwnershipComponent;
import com.phoenix.player.Player;
import com.phoenix.screens.GameScreen;

public class BlueprintCollectionSystem extends IteratingSystem
{
	private ComponentMapper<CollectibleBlueprintComponent> cbm = ComponentMapper.getFor(CollectibleBlueprintComponent.class);
	
	private GameScreen gameScreen;
	
	public BlueprintCollectionSystem(GameScreen gs)
	{
		super(Family.all(CollectibleBlueprintComponent.class).get());
		this.gameScreen = gs;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		CollectibleBlueprintComponent cbc = cbm.get(entity);
		
		// has this collectible been collected?
		if(cbc.collector != null)
		{
			// does the collector entity has an owner?
			OwnershipComponent owner = cbc.collector.getComponent(OwnershipComponent.class);
			
			if(owner != null)
			{
				Player collectorOwner = gameScreen.playerList.get(owner.owner);
				
				if(collectorOwner != null)
				{
					Blueprint blueprint = new Blueprint(gameScreen.blueprintData.get(cbc.buildableEntityName), cbc.amount);
					collectorOwner.addBlueprintToCollection(blueprint);
					System.out.println("a blueprint of " + cbc.buildableEntityName + " with " + cbc.amount + " charges was added to " + collectorOwner.name + "'s library");
					
					//once successfully collected by an entity that has an existing player, the collectible should disapear so it cannot be collected more than once...
					getEngine().removeEntity(entity);
				}
				else // if the player was not found, reset the collectible's collector variable
				{
					cbc.collector = null;
				}
			}
		}
	}
}
