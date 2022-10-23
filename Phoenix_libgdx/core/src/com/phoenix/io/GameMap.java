package com.phoenix.io;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;

/**
 * Object used to load all the components of all the entities in a game world
 * file. The components are then transferes into entities that are added to the
 * {@link Engine}
 * 
 * @author David Janelle
 */
public class GameMap
{
	public ArrayList<ArrayList<Component>> entities;

	public GameMap()
	{
		entities = new ArrayList<ArrayList<Component>>();
	}
}
