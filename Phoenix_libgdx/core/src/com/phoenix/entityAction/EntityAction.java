package com.phoenix.entityAction;

import com.badlogic.gdx.utils.Json.Serializable;

/**
 * Abstract class defining actions that an entity can do on itself and to the
 * rest of the game world
 * 
 * Information stored via the action's attributes are unit-specific parameters,
 * the way that this specific unit does a certain action. These can be defined
 * via the constructor.
 * 
 * Actions should declare a nested class that defines the required information
 * the action needs to perform itself, also known as command parameters.
 * 
 * @author David Janelle
 *
 */
public abstract class EntityAction implements Serializable
{
	// TODO 2 define some sort of map to get an error message from an error code
	/**
	 * Method that validate whether or not the action can be performed based on the
	 * action's parameters.
	 * 
	 * Every actions must implement this method internally due to the varying nature
	 * of actions.
	 * 
	 * Here is a good place to check if the action has received the proper command parameters
	 * 
	 * @return an error code based on the cause of the action's impossibility. An
	 *         error code '0' is usually ( and preferably) the code for a valid
	 *         action
	 */
	public abstract int validate();

	/**
	 * calls the {@link validate()} method to check if the {@link execute()} method
	 * can be called and then calls {@link execute()}
	 * 
	 * @return the error code that {@link validate()} returned
	 */
	public int attemptExecute()
	{
		int errCode = validate();
		if (errCode == 0)
		{
			execute();
		}

		return errCode;
	}

	/**
	 * performs its specific action to the commanded entity
	 * 
	 * Every actions must implement this method internally due to the varying nature
	 * of actions
	 */
	protected abstract void execute();
}
