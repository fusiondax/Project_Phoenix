package com.phoenix.player;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.phoenix.blueprint.Blueprint;
import com.phoenix.entityAction.EntityAction;
import com.phoenix.entityAction.EntityActionParameters;
import com.phoenix.ui.cursor.PhoenixCursor;

public class Player
{
	public String name = "";

	// TODO 2 set the player's vision/fog of war

	private ArrayList<Blueprint> blueprintLibrary;
	public ArrayList<Entity> selectedEntities;

	public Blueprint selectedBlueprint;

	public Vector2 prevCamDragPos;
	public Rectangle selectionBox;

	private CursorMode cursorMode;

	// TODO 2 this attribute is purely used for graphical purposes, and maybe it
	// would be better if it was somewhere else (to respect MVC... if I even care
	// about that...)

	public static final String WINDOW_RESIZE_PRIORITY_NAME = "window_resize";
	public static final String CURSOR_MODE_PRIORITY_NAME = "cursor_mode";
	/**
	 * The rule with this is that every priority is set in advance in the player's
	 * constructor, so 'outside' code can only define what cursor type is for a
	 * specific priority name (or disable the priority by putting a null value in
	 * the cursor type) but cannot add new priority types, cannot change the name of
	 * an existing priority type, cannot change the priority value of an existing
	 * priority.
	 */
	private OrderedMap<String, PhoenixCursor> cursorDisplayPriorityList;

	public Player(String name)
	{
		this.name = name;
		this.selectedEntities = new ArrayList<Entity>();
		this.blueprintLibrary = new ArrayList<Blueprint>();

		this.cursorDisplayPriorityList = new OrderedMap<String, PhoenixCursor>();
		this.cursorDisplayPriorityList.put(WINDOW_RESIZE_PRIORITY_NAME, null);
		this.cursorDisplayPriorityList.put(CURSOR_MODE_PRIORITY_NAME, PhoenixCursor.Arrow);

		this.selectedBlueprint = null;

		this.prevCamDragPos = new Vector2();
		this.selectionBox = new Rectangle();
		this.setCursorMode(CursorMode.Normal);
	}

	public int getCursorDisplayPriorityListSize()
	{
		return this.cursorDisplayPriorityList.size;
	}

	public String getCursorDisplayPriorityName(int index)
	{
		return this.cursorDisplayPriorityList.orderedKeys().get(index);
	}

	/**
	 * 
	 * @param priorityValue
	 *            the index of the wanted priority, 0 being the top priority
	 * @return may return null if the selected priority is 'dormant'
	 */
	public PhoenixCursor getCursorDisplay(int priorityValue)
	{
		Array<String> keys = this.cursorDisplayPriorityList.orderedKeys();
		String key = keys.get(priorityValue);
		PhoenixCursor cursor = this.cursorDisplayPriorityList.get(key);

		return cursor;
	}

	/**
	 * 
	 * @param priorityName
	 * @return may return null if the selected priority is 'dormant'
	 */
	public PhoenixCursor getCursorDisplay(String priorityName)
	{
		PhoenixCursor cursor = this.cursorDisplayPriorityList.get(priorityName);
		return cursor;
	}

	/**
	 * 
	 * @param newCursor
	 * @param priorityName
	 */
	public void setCursorDisplay(PhoenixCursor newCursor, String priorityName)
	{
		Entries<String, PhoenixCursor> entries = this.cursorDisplayPriorityList.entries();

		while (entries.hasNext())
		{
			Entry<String, PhoenixCursor> entry = entries.next();
			if (entry.key.equals(priorityName))
			{
				this.cursorDisplayPriorityList.put(priorityName, newCursor);
				break;
			}
		}
	}

	public void addBlueprintToCollection(Blueprint blueprint)
	{
		// find an exisiting blueprint that is equivalent to the given blueprint
		Blueprint existingBlueprint = null;
		for (Blueprint b : blueprintLibrary)
		{
			if (b.isEquivalent(blueprint))
			{
				existingBlueprint = b;
				break;
			}
		}

		if (existingBlueprint != null)
		{
			existingBlueprint.addAmount(blueprint.getAmount());
		}
		else
		{
			blueprintLibrary.add(blueprint);
		}
	}

	public void dispenseBlueprintFromCollection(Blueprint blueprint)
	{
		dispenseBlueprintFromCollection(blueprint, 1);
	}

	public void dispenseBlueprintFromCollection(Blueprint blueprint, int amountToRemove)
	{
		// find an exisiting blueprint that is equivalent to the given blueprint
		Blueprint existingBlueprint = null;
		for (Blueprint b : blueprintLibrary)
		{
			if (b.isEquivalent(blueprint))
			{
				existingBlueprint = b;
				break;
			}
		}

		if (existingBlueprint != null)
		{
			existingBlueprint.subAmount(amountToRemove);

			if (existingBlueprint.getAmount() <= 0)
			{
				blueprintLibrary.remove(existingBlueprint);
				selectedBlueprint = null;
			}
		}
		else
		{
			System.out.println("no such blueprint exists in this library");
		}
	}

	public ArrayList<Blueprint> getBlueprintLibrary()
	{
		return blueprintLibrary;
	}

	public CursorMode getCursorMode()
	{
		return cursorMode;
	}

	public void setCursorMode(CursorMode cursorMode)
	{
		this.cursorMode = cursorMode;
	}
}
