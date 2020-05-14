package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.screens.GameScreen;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;
	private Skin skin;

	public PhoenixWindowBuilder builder;

	public boolean isSetup = false;

	public InGameUI(Viewport port, GameScreen gs)
	{
		super(port);
		this.gameScreen = gs;
	}

	public void setupUI()
	{
		// initialize the skin
		AssetManager manager = PhoenixAssetManager.getInstance().manager;
		TextureAtlas texAtlas = manager.get("graphics/atlas/ui.atlas");
		skin = new Skin(texAtlas);

		LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.WHITE);

		Pixmap windowBackgroundColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		windowBackgroundColor.setColor(Color.NAVY);
		windowBackgroundColor.fill();
		
		Drawable windowBackgroundDrawable = (Drawable) new Image(new Texture(windowBackgroundColor)).getDrawable();
		
		WindowStyle windowStyle = new WindowStyle(new BitmapFont(), Color.WHITE,
				windowBackgroundDrawable);

		// ButtonStyle closeWindowButtonStyle = new
		// ButtonStyle(skin.getDrawable("ui_close_window_button_up"),
		// skin.getDrawable("ui_close_window_button_down"),
		// skin.getDrawable("ui_close_window_button_over"));
		//
		// skin.add("default_close_window_button", closeWindowButtonStyle);

		skin.add("default_ui_background", windowBackgroundDrawable);
		skin.add("default_label", labelStyle);
		skin.add("default_window", windowStyle);

		// initialize primary actors
		
		builder = new PhoenixWindowBuilder(skin, "default_label", "default_window", gameScreen);
		
		WindowOpenerBar wob = new WindowOpenerBar(skin);
		
		addActor(wob);
		
//		addActor(builder.getWindow("misc_info"));
//		addActor(builder.getWindow("blueprint_bar"));

		isSetup = true;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}
}
