package com.phoenix.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.phoenix.components.CollisionHitboxComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.game.Phoenix;
import com.phoenix.pathfinding.SearchNode;
import com.phoenix.physics.CollisionDetector;

public class MovementAISystem extends IteratingSystem
{
	private ComponentMapper<MovementAIComponent> mam = ComponentMapper.getFor(MovementAIComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionHitboxComponent> chm = ComponentMapper.getFor(CollisionHitboxComponent.class);
	
	private ShapeRenderer debug;
	
	public MovementAISystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class, MovementAIComponent.class, CollisionHitboxComponent.class).get());
		this.debug = debug;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		MovementAIComponent mac = mam.get(entity);
		
		//TODO new pathfinding system
		if(!mac.destinations.isEmpty())
		{
			Engine engine = getEngine();
			PositionComponent entityPosition = pm.get(entity);
			VelocityComponent entityVelocity = vm.get(entity);
			CollisionHitboxComponent entityHitbox = chm.get(entity);
			
			Vector2 entityPosition2d = new Vector2(entityPosition.pos.x, entityPosition.pos.y);
			float unitMaxSpeed = mac.unitMaxSpeed;
			
			Vector2 nextDestination = mac.destinations.get(0);
			
			if(nextDestination.dst(entityPosition2d) >= entityHitbox.size / 2) 
			{
				CollisionDetector detector = new CollisionDetector(engine);
				
				Vector2 velocityVector = new Vector2(nextDestination.x - entityPosition2d.x, nextDestination.y - entityPosition2d.y);
				velocityVector.setLength(unitMaxSpeed);
				
				Vector2 nexPathStartPoint = new Vector2().add(entityPosition2d).add(velocityVector);
				
				Vector2 immediateMovementLocation = new Vector2();
				immediateMovementLocation.add(new Vector2(velocityVector.x * deltaTime, velocityVector.y * deltaTime));
				immediateMovementLocation.add(entityPosition2d);
				
				
				Circle hitboxCircle = new Circle(immediateMovementLocation, entityHitbox.size);
				
				debug.setColor(Color.GREEN);
				debug.line(nexPathStartPoint, nextDestination);
				debug.setColor(Color.YELLOW);
				debug.circle(hitboxCircle.x, hitboxCircle.y, hitboxCircle.radius);
				
				detector.debugRectanglesHitbox(debug, CollisionDetector.getRectanglesFromTerrains(detector.getImpassableTerrains(mac)));
				
				//if the unit's movements speed is near zero
				
				// if a collision is imminent
				if(detector.isCircleCollisionRectangles(hitboxCircle, CollisionDetector.getRectanglesFromTerrains(detector.getImpassableTerrains(mac))))
				{
					searchNewPath(detector, entityPosition2d, nexPathStartPoint, nextDestination, mac);
					
					entityVelocity.velocity.setZero();
				}
				else //if not, proceed on current vector
				{
					mac.initialNode = null;
					
					//debug.setColor(Color.RED);
					//debug.line(new Vector2(), velocityVector);
					
					entityVelocity.velocity.set(velocityVector);
				}
				
			}
			else //unit's hitbox is at destination, remove current destination point and stops the unit there
			{
				mac.destinations.remove(0);
				entityVelocity.velocity.setZero();
			}
		}
	}
	
	private void searchNewPath(CollisionDetector detector, Vector2 initialPoint, Vector2 startNode, Vector2 endNode, MovementAIComponent mac)
	{
		if(mac.initialNode == null)
		{
			mac.initialNode = new SearchNode(initialPoint);
			mac.initialNode.isSearching = false;
		}
		else
		{
			
			SearchNode validPath = SearchNode.getValidPath(mac.initialNode); 
			if(validPath != null)
			{
				System.out.println("new path found!");
				SearchNode.debugLegacy(validPath, debug);
			}
			else
			{
				mac.initialNode.startRecursiveSearch(detector, mac, startNode, endNode, debug);
			}
		}
	}
}
