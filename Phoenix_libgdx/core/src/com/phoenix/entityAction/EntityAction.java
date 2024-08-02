package com.phoenix.entityAction;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
	/**
	 * May be null.
	 * 
	 * The command parameter for the action. When null, the action is dormant,
	 * 'awaiting commands'. When this gets defined, the entity action system
	 * determines the validity of the command (via the validate() method) and asks
	 * the entity to perform the action (via the execute() method). The action stops
	 * when the command parameters is set to null once again
	 */
	protected EntityActionParameters commandParameters;

	// TODO 2 define some sort of map to get an error message from an error code
	/**
	 * Method that checks that the current task is possible.
	 * <p>
	 * 
	 * Every actions must implement this method internally due to the varying nature
	 * of actions.
	 * <p>
	 * 
	 * Here is a good place to check if the action has received the proper command
	 * parameters.
	 * <p>
	 * 
	 * The standard for return codes goes as follow:
	 * <p>
	 * - 0: the command is valid.
	 * <p>
	 * - 1+: the command is invalid.
	 * <p>
	 * 
	 * Although implementing class could deviate from this standard, it is generally
	 * discouraged.
	 * 
	 * @return an error code based on the cause of the action's impossibility. See
	 *         standard return codes above.
	 */
	public abstract EntityActionGenericReturnCodes validate(Engine engine, Entity entity);

	/**
	 * calls the {@link validate()} method to check if the {@link execute()} method
	 * can be called and then calls {@link execute()}
	 * 
	 * @return the error code that {@link validate()} returned
	 */
	public EntityActionGenericReturnCodes attemptExecute(Engine engine, Entity entity, float deltaTime)
	{
		EntityActionGenericReturnCodes errCode = EntityActionGenericReturnCodes.DefaultCode;
		
		if (isGoalReached())
		{
			stopAction(entity);
		}else
		{
			errCode = validate(engine, entity);
		
			if (!isErrorCodeExecuteSafe(errCode))
			{
				stopAction(entity);
			}
			else
			{
				execute(engine, entity, errCode, deltaTime);
			}
		}
		
		return errCode;
	}

	/**
	 * Performs its specific action to the commanded entity. As the
	 * 
	 * Every actions must implement this method internally due to the varying nature
	 * of actions
	 */
	protected abstract void execute(Engine engine, Entity entity, EntityActionGenericReturnCodes errorCode, float deltaTime);

	/**
	 * @return true if the goal has been reached and the action should be stopped,
	 *         false otherwise.
	 */
	public abstract boolean isGoalReached();

	/**
	 * This should be called to make the entity cease to execute its current
	 * command, typically by making the command parameter null. this is typically
	 * called either when {@link validate()} returns an error code that
	 * {@link isErrorCodeExecuteSafe()} considers unsuitable or when
	 * {@link isGoalReached()} returns true.
	 */
	public abstract void stopAction(Entity entity);

	/**
	 * Determines whether or not the error code returned by {@link validate()}
	 * indicates that the command given to the entity is safe to execute. An
	 * execution safe state is a state that will not, for example, result in an
	 * infinite loop where the entity will never be able to complete its task.
	 *
	 * @param errCode
	 *            the error code returned by {@link validate()}
	 * @return true if the error code is considered 'execution safe'
	 */
	public abstract boolean isErrorCodeExecuteSafe(EntityActionGenericReturnCodes errCode);

	/**
	 * 
	 * @return may be null
	 */
	public EntityActionParameters getCommandParameters()
	{
		return this.commandParameters;
	}

	/**
	 * Classes overriding this method should make sure that the received parameters
	 * is of the right type for its action type (maybe even throw an exception)
	 * 
	 * @param params
	 *            the command parameters to perform the action. This should be of a
	 *            type inheriting {@link EntityActionParameters} specific to the
	 *            specific action.
	 */
	public void setCommandParameters(EntityActionParameters params)
	{
		if(params != null)
		{
			if (params.getClass().equals(getCommandParametersClass()))
			{
				this.commandParameters = params;
			}
			else
			{
				// TODO 2 make a new Exception for assigning the wrong type of command parameter
				System.out.println("The recieved parameter of Type: " + params.getClass().toString()
						+ " does not match expected Type: " + getCommandParametersClass().toString());
			}
		}
		else
		{
			this.commandParameters = params;
		}
	}

	// TODO 3 technically should not use 'Class' raw, but not sure how to implement
	// it otherwise...
	/**
	 * Typical implementation:
	 * 
	 * {@code return SpecificEntityActionParameters.class;}
	 * 
	 * @return the class of the {@link EntityActionParameters} implemented and used
	 *         by the Entity Action
	 */
	protected abstract Class getCommandParametersClass();
	
	public enum EntityActionGenericReturnCodes
	{
		DefaultCode("this is the default code"),GoalReached("the goal has been reached"),
		PickUp("unit will pick up resource bundle"), DropOffNew("unit will drop off a new resource bundle"),
		DropOffExisting("unit will drop off resource into an existing resouce bundle"),
		ErrResNotFound("Error: resource was not found at location"),
		ErrResOutRange("Error: resource is out of unit's range"),
		ErrLocationOutRange("error: location is out of unit's range");;
		
		private String message;

		private EntityActionGenericReturnCodes(String message)
		{
			this.message = message;
		}

		public String getMessage()
		{
			return this.message;
		}
	}
}
