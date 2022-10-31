package com.phoenix.entityAction;

public enum EntityActionKnownType
{
	Move("move"), Carry("carry"), Disassemble("disassemble");
	
	private String actionName;
	
	private EntityActionKnownType(String name)
	{
		this.actionName = name;
	}
	
	public String getName()
	{
		return this.actionName;
	}
	
	
}
