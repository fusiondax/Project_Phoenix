package com.phoenix.io;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;

public class GameMap 
{
	public ArrayList<ArrayList<Component>> entities;
	
	public GameMap()
	{
		entities = new ArrayList<ArrayList<Component>>();
	}
}
