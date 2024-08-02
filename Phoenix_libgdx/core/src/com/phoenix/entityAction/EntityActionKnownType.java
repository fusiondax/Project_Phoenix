package com.phoenix.entityAction;

public enum EntityActionKnownType
{
	MoveSelf("move"), CarryResources("carry"), DisassembleSelf("disassemble");
	
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
