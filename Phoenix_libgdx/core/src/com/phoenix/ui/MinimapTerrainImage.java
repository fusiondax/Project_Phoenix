package com.phoenix.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MinimapTerrainImage extends Image
{
	public MinimapTerrainImage()
	{
		this(new TextureRegion());
	}
	
	public MinimapTerrainImage(TextureRegion mtt)
	{
		super(mtt);
		setSize(getImageWidth(), getImageWidth());
		setFillParent(true);
	}
	
	@Override
	public void act(float delta)
	{
		//setDrawable(drawable);
	}
	
	
}
