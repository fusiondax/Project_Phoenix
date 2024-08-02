package com.phoenix.entityAction;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.ResourceComponent;
import com.phoenix.io.EntityLoader;
import com.phoenix.resource.Resource;
import com.phoenix.utility.GameWorldUtility;

/* TODO 3 although maybe 'picking up' and 'dropping off' should be separated into 2 
distinct actions for clarity's sake, I think it would only clog the code to have them 
Separated, and I cannot imagine a case where you would want an entity to be able to pick up 
but not be able to drop off (except for making a practical joke) */

/**
 * 
 * This entity action allows an entity to pick up a bundle of resource found in
 * the game world and drop it off somewhere else. Depending on contextual
 * elements (such as what the player has targeted when issuing the command and
 * whether or not the entity is carrying something), the entity will be
 * commanded to pick up or drop off, changing the behavior of the action
 * 
 * @author David Janelle
 *
 */
public class CarryResourceEntityAction extends EntityAction
{
	/**
	 * position component of the entity picking up or dropping off resources
	 */
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<ResourceComponent> rm = ComponentMapper.getFor(ResourceComponent.class);

	private boolean goalReached = false;

	/**
	 * the resources being carried by the entity, to be dropped off
	 */
	public Resource carriedResource;
	private int maxCapacity;
	private float maxPickupRange;

	public CarryResourceEntityAction()
	{

	}

	public CarryResourceEntityAction(int maxCap, float maxPickupRange)
	{
		this.maxCapacity = maxCap;
		this.maxPickupRange = maxPickupRange;
	}

	@Override
	public EntityActionGenericReturnCodes validate(Engine engine, Entity entity)
	{
		EntityActionGenericReturnCodes returnCode = EntityActionGenericReturnCodes.DefaultCode;
		CarryResourceEntityActionParameters creap = (CarryResourceEntityActionParameters) getCommandParameters();

		// if unit is already carrying resources
		if (carriedResource != null)
		{
			// if entity selected is a resource
			if (validateIsResourceFound(creap))
			{
				// unit must attempt to add carried resources to existing resource bundle

				// if resource is within range
				if (validateResourceWithinRange(entity, creap))
				{
					// if resource bundle type match carried resource
					if (validateResourceTypeMatch(creap))
					{
						// unit is valid to drop off unto existing resource bundle
						// (code 2)
						returnCode = EntityActionGenericReturnCodes.DropOffExisting;
					} else
					{
						// create a new resource bundle next to existing, non-matching resource bundle
						// (code 1)
						returnCode = EntityActionGenericReturnCodes.DropOffNew;
					}

				} else
				{
					// error: unit is too far away from resource
					// (code 4)
					returnCode = EntityActionGenericReturnCodes.ErrResOutRange;
				}
			} else // if entity selected is not a resource
			{
				// unit must drop off carried resources at location

				// if resource is within range of unit
				if (validateLocationWithinRange(entity, creap))
				{
					// unit is valid to drop off as a new bundle (code 1)
					returnCode = EntityActionGenericReturnCodes.DropOffNew;
				} else
				{
					// error: unit is too far away from location
					// (code 5)
					returnCode = EntityActionGenericReturnCodes.ErrLocationOutRange;
				}
			}
		} else // if unit does not carry resources
		{
			// if entity selected is a resource
			if (validateIsResourceFound(creap))
			{
				// unit must attempt to collect resource

				// if location is within range of unit
				if (validateLocationWithinRange(entity, creap))
				{
					// unit is valid to pick up (code 0)
					returnCode = EntityActionGenericReturnCodes.PickUp;
				} else
				{
					// unit is too far away to pick up resource (code 5)
					returnCode = EntityActionGenericReturnCodes.ErrResOutRange;
				}
			} else // if entity selected is not a resource
			{
				// the command is invalid due to lack of resource to collect,
				// error message is displayed (code 3)
				returnCode = EntityActionGenericReturnCodes.ErrResNotFound;
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * @param unit
	 * @param creap
	 * @return true if unit and target location are within the unit's pickup range,
	 *         false otherwise
	 */
	private boolean validateLocationWithinRange(Entity unit, CarryResourceEntityActionParameters creap)
	{
		PositionComponent entityPosition = pm.get(unit);

		return entityPosition.pos2D.dst(creap.targetLocation) <= this.maxPickupRange;
	}

	/**
	 * 
	 * @return true if unit and target resource are within the unit's pickup range,
	 *         false otherwise
	 */
	private boolean validateResourceWithinRange(Entity unit, CarryResourceEntityActionParameters creap)
	{
		PositionComponent entityPosition = pm.get(unit);
		PositionComponent targetResourcePosition = pm.get(creap.targetResourceEntity);

		return entityPosition.pos2D.dst(targetResourcePosition.pos2D) <= this.maxPickupRange;
	}

	/**
	 * 
	 * @param creap
	 * @return true if a resource was found, false if it was not
	 */
	private boolean validateIsResourceFound(CarryResourceEntityActionParameters creap)
	{
		return creap.targetResourceEntity != null;
	}

	/**
	 * 
	 * @return true if target resource type match carried resource type
	 */
	private boolean validateResourceTypeMatch(CarryResourceEntityActionParameters creap)
	{
		ResourceComponent resComp = rm.get(creap.targetResourceEntity);

		return resComp.resource.type.equals(this.carriedResource.type);
	}

	@Override
	public boolean isErrorCodeExecuteSafe(EntityActionGenericReturnCodes errCode)
	{
		boolean safe = false;

		switch (errCode)
		{
			case GoalReached:
				safe = true;
				break;
			case PickUp:
				safe = true;
				break;
			case DropOffExisting:
				safe = true;
				break;
			case DropOffNew:
				safe = true;
				break;
			case ErrLocationOutRange:
				safe = false;
				break;
			case ErrResNotFound:
				safe = false;
				break;
			case ErrResOutRange:
				safe = false;
				break;
			default:
				break;
		}
		return safe;
	}

	@Override
	protected void execute(Engine engine, Entity entity, EntityActionGenericReturnCodes errCode, float deltaTime)
	{
		// TODO 3 should there be an execution time for picking up resources? Or should
		// it be instantaneous?

		// for now, picking up items is instantaneous
		switch (errCode)
		{
			case DropOffExisting:
				executeDropOffExisting(engine, entity, deltaTime);
				break;
			case DropOffNew:
				executeDropOffNew(engine, entity, deltaTime);
				break;
			case PickUp:
				executePickUp(engine, entity, deltaTime);
				break;
		}
		goalReached = true;
	}

	private void executePickUp(Engine engine, Entity unit, float deltaTime)
	{
		CarryResourceEntityActionParameters creap = (CarryResourceEntityActionParameters) getCommandParameters();
		ResourceComponent bundleComp = rm.get(creap.targetResourceEntity);
		
		// create a new resource object based on the bundle's data
		this.carriedResource = new Resource(bundleComp.resource.type, bundleComp.resource.amount);
		
		// if the bundle's size is smaller or equal to the unit's max capacity
		if (bundleComp.resource.amount <= this.maxCapacity)
		{
			// delete the resource bundle
			engine.removeEntity(creap.targetResourceEntity);
		} 
		else
		{
			// substract the max capacity from the resource bundle
			bundleComp.resource.amount = bundleComp.resource.amount - this.maxCapacity;
			
			// cap the amount of carried resource to unit's max capacity
			this.carriedResource.amount = this.maxCapacity;
		}
	}

	private void executeDropOffNew(Engine engine, Entity unit, float deltaTime)
	{
		CarryResourceEntityActionParameters creap = (CarryResourceEntityActionParameters) getCommandParameters();

		// TODO 4 is it a coincidence that a resource type's name is the same as the
		// name of the entity?

		// create new resource entity based on carried resource by unit
		Entity newResourceEntity = EntityLoader.getInitializedEntity(this.carriedResource.type);

		// update the position to be target location
		PositionComponent position = newResourceEntity.getComponent(PositionComponent.class);
		position.pos2D.set(creap.targetLocation);

		ResourceComponent resourceComp = newResourceEntity.getComponent(ResourceComponent.class);
		resourceComp.resource = new Resource(this.carriedResource.type, this.carriedResource.amount);

		engine.addEntity(newResourceEntity);

		// remove carried resource from unit
		this.carriedResource = null;
	}

	private void executeDropOffExisting(Engine engine, Entity unit, float deltaTime)
	{
		CarryResourceEntityActionParameters creap = (CarryResourceEntityActionParameters) getCommandParameters();
		ResourceComponent resComp = rm.get(creap.targetResourceEntity);
		// add the amount of resource carried to existing bundle
		resComp.resource.amount += this.carriedResource.amount;

		// remove carried resource from unit
		this.carriedResource = null;
	}

	@Override
	public boolean isGoalReached()
	{
		return this.goalReached;
	}

	@Override
	public void stopAction(Entity entity)
	{
		this.commandParameters = null;
		goalReached = false;
	}

	@Override
	public void write(Json json)
	{
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.maxCapacity = jsonData.get("max_capacity").asInt();
		this.maxPickupRange = jsonData.get("max_pickup_range").asFloat();
	}

	@Override
	protected Class getCommandParametersClass()
	{
		return CarryResourceEntityActionParameters.class;
	}
}
