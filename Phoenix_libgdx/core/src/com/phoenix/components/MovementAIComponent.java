package com.phoenix.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementAIComponent implements Component
{
	public ArrayList<String> passableTerrains = new ArrayList<String>();
	public ArrayList<Vector2> destinations = new ArrayList<Vector2>(); 
	public float unitMaxSpeed = 0.0f;
}
