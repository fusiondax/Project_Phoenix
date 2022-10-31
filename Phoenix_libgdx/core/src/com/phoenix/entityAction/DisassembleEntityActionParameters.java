package com.phoenix.entityAction;

public class DisassembleEntityActionParameters implements EntityActionParameters
{
	// TODO 3 param value added as example, might be removed later
	public boolean emergencyDisassembly;

	public DisassembleEntityActionParameters(boolean emergencyDisassembly)
	{
		this.emergencyDisassembly = emergencyDisassembly;
	}
}
