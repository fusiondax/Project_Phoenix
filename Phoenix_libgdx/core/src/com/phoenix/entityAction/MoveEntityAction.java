package com.phoenix.entityAction;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.ValidTerrainTypesComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.pathfinding.EntityPathfinding;
import com.phoenix.physics.CollisionDetector;
import com.phoenix.utility.EntityUtility;

/**
 * 
 * @author David Janelle
 *
 */
public class MoveEntityAction extends EntityAction
{
	public static final int START_PATHFINDING_DELAY_MAX = 15;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionHitboxComponent> chm = ComponentMapper.getFor(CollisionHitboxComponent.class);
	private ComponentMapper<ValidTerrainTypesComponent> vttm = ComponentMapper.getFor(ValidTerrainTypesComponent.class);

	public ArrayList<Vector2> destinations = new ArrayList<Vector2>();
	public float unitMaxSpeed;
	public int startPathfindingDelay = START_PATHFINDING_DELAY_MAX;

	public MoveEntityAction()
	{
		this(null, 0.0f);
	}

	public MoveEntityAction(Entity owner, float ums)
	{
		this.unitMaxSpeed = ums;
	}

	@Override
	public void setCommandParameters(EntityActionParameters params)
	{
		super.setCommandParameters(params);
		if (params != null)
		{
			MoveEntityActionParameters meap = (MoveEntityActionParameters) params;
			this.destinations.clear();
			this.destinations.add(meap.targetDestination);
		}
	}

	// TODO 1 implement proper validation. Make sure it is not impossible to reach
	// the destination
	public int validate()
	{
		return 0;
	}

	@Override
	public boolean isErrorCodeExecuteSafe(int errCode)
	{
		boolean safe = false;
		switch (errCode)
		{
			case 0:
			{
				safe = true;
				break;
			}
		}
		return safe;
	}

	// TODO 3 this pathfinding algorithm is the first of many more to come. In the
	// future, each unit will have their own pathfinding algorithm. (some units may
	// share the same algorithm, depending on the design of the unit)

	protected void execute(Engine engine, Entity entity, float deltaTime)
	{
		PositionComponent entityPosition = pm.get(entity); // entity.getComponent(PositionComponent.class);
		VelocityComponent entityMovement = vm.get(entity); // entity.getComponent(VelocityComponent.class);
		CollisionHitboxComponent entityHitbox = chm.get(entity); // entity.getComponent(CollisionHitboxComponent.class);
		ValidTerrainTypesComponent entityValidTerrainTypes = vttm.get(entity); // entity.getComponent(ValidTerrainTypesComponent.class);

		Vector2 entityPosition2d = new Vector2(entityPosition.pos2D);

		Vector2 nextDestination = this.destinations.get(0);

		if (nextDestination.dst(entityPosition2d) >= entityHitbox.size / 2)
		{
			Vector2 nextDestinationVector = new Vector2(nextDestination.x - entityPosition2d.x,
					nextDestination.y - entityPosition2d.y);
			nextDestinationVector.setLength(this.unitMaxSpeed);

			Vector2 nextPathStartPoint = new Vector2().add(entityPosition2d).add(nextDestinationVector);

			Vector2 immediateMovementLocation = new Vector2();
			immediateMovementLocation
					.add(new Vector2(nextDestinationVector.x * deltaTime, nextDestinationVector.y * deltaTime));
			immediateMovementLocation.add(entityPosition2d);

			Circle hitboxCircle = new Circle(immediateMovementLocation, entityHitbox.size);

			/*
			 * TODO 3 add more refined pathfinding start conditions: ignore moving friendly
			 * units, ask stationary friendly units who are blocking to move around
			 */

			// if the unit is colliding with something for more than a second...
			if (CollisionDetector.isCircleCollisionRectangles(hitboxCircle, EntityUtility
					.getRectanglesFromTerrains(EntityUtility.getImpassableTerrains(engine, entityValidTerrainTypes))))
			{
				if (this.startPathfindingDelay <= 0)
				{
					EntityPathfinding.searchNewPath(engine, entityPosition2d, nextPathStartPoint, nextDestination, this,
							entityValidTerrainTypes);
				}
				else
				{
					this.startPathfindingDelay--;
				}
			}
			else // if not, proceed on current vector
			{
				this.startPathfindingDelay = MoveEntityAction.START_PATHFINDING_DELAY_MAX;
			}

			entityMovement.velocity.add(nextDestinationVector);

			// System.out.println("next dest mov: " + nextDestinationVector);
			// System.out.println("current velo: " + entityMovement.velocity.toString());
		}
		else // unit's hitbox is at destination, remove current destination point and stops
				// the unit there
		{
			this.destinations.remove(0);
			entityMovement.velocity.setZero();
		}
	}

	// TODO 1 complete this using existing move command code
	@Override
	public boolean isGoalReached()
	{
		boolean goal = false;

		if (this.destinations.isEmpty())
		{
			goal = true;
		}

		return goal;
	}

	@Override
	public void stopAction(Entity entity)
	{
		this.commandParameters = null;
		this.destinations.clear();
		VelocityComponent entityMovement = entity.getComponent(VelocityComponent.class);
		entityMovement.velocity.setZero();
	}

	@Override
	public void write(Json json)
	{

	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		this.unitMaxSpeed = jsonData.get("unit_max_speed").asFloat();
	}

	@Override
	protected Class getCommandParametersClass()
	{
		return MoveEntityActionParameters.class;
	}
}
