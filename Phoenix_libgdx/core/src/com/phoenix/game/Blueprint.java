package com.phoenix.game;

public class Blueprint
{
	public String buildableEntityName;
	
	private int amount;
	
	public Blueprint(String buildableEntityName, int amount)
	{
		this.buildableEntityName = buildableEntityName;
		this.amount = amount;
	}

	public int getAmount()
	{
		return amount;
	}
	
	public void setAmount(int amount)
	{
		if(amount > 0)
		{
			this.amount = amount;
		}
	}
	
	public void addAmount(int amount)
	{
		this.amount += amount;
	}
	
	public void subAmount(int amount)
	{
		this.amount -= amount;
	}
	
	public boolean isEquivalent(Blueprint blueprint)
	{
		return buildableEntityName.equals(blueprint.buildableEntityName);
	}

	
}
