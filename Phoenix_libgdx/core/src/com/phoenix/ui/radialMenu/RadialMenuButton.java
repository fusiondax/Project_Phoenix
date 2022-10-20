package com.phoenix.ui.radialMenu;
/**
 * radial button, usually part of a {@link RadialMenu}, allows for
 * contextual control of an entity.
 * 
 * @author David Janelle
 *
 */
public class RadialMenuButton
{
	public String iconName = "";
	
	public String type = "";
	
	public RadialMenuButton(String typeName)
	{
		this(typeName, "");
	}
	
	public RadialMenuButton(String typeName, String iconName)
	{
		this.type = typeName;
		this.iconName = iconName;
	}
}