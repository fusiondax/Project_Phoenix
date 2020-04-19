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
import com.phoenix.components.HitboxComponent;
import com.phoenix.components.MovementAIComponent;
import com.phoenix.components.PositionComponent;
import com.phoenix.components.TerrainComponent;
import com.phoenix.components.VelocityComponent;
import com.phoenix.game.Phoenix;
import com.phoenix.pathfinding.CollisionDetector;

public class MovementAISystem extends IteratingSystem
{
	private ComponentMapper<MovementAIComponent> mam = ComponentMapper.getFor(MovementAIComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);
	
	private ShapeRenderer debug;
	
	public static final float SEARCH_RAY_MAX_LENGTH = 200.0f;
	public static final int SEARCH_NODES_MAX_AMOUNT = 100;
	
	public MovementAISystem(ShapeRenderer debug)
	{
		super(Family.all(PositionComponent.class, VelocityComponent.class, MovementAIComponent.class, HitboxComponent.class).get());
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
			HitboxComponent entityHitbox = hm.get(entity);
			
			Vector2 entityPosition2d = new Vector2(entityPosition.pos.x, entityPosition.pos.y);
			float unitMaxSpeed = mac.unitMaxSpeed;
			
			Vector2 nextDestination = mac.destinations.get(0);
			
			if(nextDestination.dst(entityPosition2d) >= entityHitbox.radius / 2) 
			{
				Vector2 velocityVector = new Vector2(nextDestination.x - entityPosition2d.x, nextDestination.y - entityPosition2d.y);
				velocityVector.setLength(unitMaxSpeed);
				
				Vector2 immediateMovementLocation = new Vector2();
				immediateMovementLocation.add(entityPosition2d);
				immediateMovementLocation.add(velocityVector);
				
				debug.setColor(Color.GREEN);
				debug.line(entityPosition2d, nextDestination);
				
				// if a collision is imminent
				if(CollisionDetector.isCollision(engine, mac, immediateMovementLocation))
				{
					generateScatterNodes(engine, mac, entityPosition2d);
					
					entityVelocity.velocity.setZero();
					
				}
				else //if not, proceed on current vector
				{
					mac.expandingNodes.clear();
					mac.finalNodes.clear();
					mac.nextNodes.clear();
					
					//debug.setColor(Color.RED);
					//debug.line(new Vector2(), velocityVector);
					
					//debug.rect(terrainRect.x, terrainRect.y, terrainRect.width, terrainRect.height);
					
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
	
	private void searchNewPath(Vector2 startNode, Vector2 endNode)
	{
		
	}
	
	private void generateScatterNodes(Engine engine, MovementAIComponent mac, Vector2 currentNode)
	{
		//ArrayList<Vector2> expandingNodes = new ArrayList<Vector2>();
		
		//ArrayList<Vector2> finalNodes = new ArrayList<Vector2>();
		
		
		
		/*
		if(mac.expandingNodes.isEmpty() && !mac.finalNodes.isEmpty())
		{
			mac.finalNodes.clear();
		}*/
		
		
		System.out.println(mac.expandingNodes.size());
		System.out.println(mac.finalNodes.size());
		System.out.println(mac.nextNodes.size());
		
		if(mac.expandingNodes.isEmpty() && mac.finalNodes.isEmpty())
		{
			int angle = 0;
			
			while(angle < 360)
			{
				Vector2 searchVector = new Vector2(0, 1);
				searchVector.setAngle(angle);
				searchVector.setLength(1);
				searchVector.add(currentNode);
				mac.expandingNodes.add(searchVector);
				angle += 45;
			}
			
		}
		
		for(Vector2 vec: mac.expandingNodes)
		{
			if(CollisionDetector.isCollision(engine, mac, vec) || (Vector2.dst(currentNode.x, currentNode.y, vec.x, vec.y) >= SEARCH_RAY_MAX_LENGTH))
			{
				mac.finalNodes.add(vec);
			}
			else
			{
				vec.sub(currentNode).setLength(vec.len() + 10).add(currentNode);
			}
			debug.setColor(Color.ORANGE);
			
			debug.line(currentNode, vec);
			debug.circle(vec.x, vec.y, 5);
			
			debug.setColor(Color.GREEN);
		}
		ArrayList<Vector2> nodesToRemove = new ArrayList<Vector2>();
		
		for(Vector2 vec: mac.finalNodes)
		{
			if(mac.expandingNodes.contains(vec))
			{
				mac.expandingNodes.remove(vec);
			}
			
			if(Vector2.dst(vec.x, vec.y, currentNode.x, currentNode.y) < 40)
			{
				nodesToRemove.add(vec);
			}
			
			debug.setColor(Color.RED);
			
			debug.line(currentNode, vec);
			debug.circle(vec.x, vec.y, 5);
			
			debug.setColor(Color.GREEN);
		}
		
		for(Vector2 vec: nodesToRemove)
		{
			mac.finalNodes.remove(vec);
		}
		
		
		/*
		System.out.println("------------------------------------------");
		System.out.println("startNode before: " + startNode);
		System.out.println("endNode before: " + endNode);
		System.out.println("origin before: " + origin);
		System.out.println("north before: " + north);
		System.out.println("intersect before: " + intersect);
		
		//boolean isIntersect = Intersector.intersectSegments(startNode, endNode, origin, north, intersect);
		
		System.out.println("is intersected?: " + isIntersect);
		System.out.println("startNode after: " + startNode);
		System.out.println("endNode after: " + endNode);
		System.out.println("origin after: " + origin);
		System.out.println("north after: " + north);
		System.out.println("intersect after: " + intersect);
		System.out.println("------------------------------------------");
		*/
	}
}
