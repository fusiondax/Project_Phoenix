package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.phoenix.assets.PhoenixAssetManager;
import com.phoenix.screens.GameScreen;

public class InGameUI extends Stage
{
	private GameScreen gameScreen;
	private Skin skin;

	private Label framerateCounter;
	private Label timeDilationCounter;
	private BlueprintBar blueprintBar;
	private Window testWindow;

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
		
		ImageTextButtonStyle imgTextBtnStyle = new ImageTextButtonStyle();
		imgTextBtnStyle.font = new BitmapFont();
		imgTextBtnStyle.fontColor = Color.WHITE;
		
		LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.WHITE);
		
		Pixmap windowBackgroundColor = new Pixmap(10, 10, Pixmap.Format.RGB888);
		windowBackgroundColor.setColor(Color.NAVY);
		windowBackgroundColor.fill();
		WindowStyle windowStyle = new WindowStyle(new BitmapFont(), Color.WHITE, new Image(new Texture(windowBackgroundColor)).getDrawable());
		
		skin.add("default_image_text_button", imgTextBtnStyle);
		skin.add("default_label", labelStyle);
		skin.add("default_window", windowStyle);
		
		// initialize primary actors
		
		framerateCounter = new FramerateCounterLabel("", skin);
		timeDilationCounter = new Label("", skin, "default_label");
		
		blueprintBar = new BlueprintBar(skin, gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME));
		blueprintBar.debug();
		
		testWindow = new PhoenixWindow("This is a test", skin, "default_window", gameScreen);
		
		
		
		
		//testWindow.setColor(Color.GREEN);
		
		// add all primary actors
		addActor(blueprintBar);
		addActor(framerateCounter);
		addActor(timeDilationCounter);
		addActor(testWindow);
		
		// give the blueprint bar the keyboard focus
		// TODO I don't like that only one actor has the keyboard focus... might want to think of something else here...
		setKeyboardFocus(blueprintBar);
		addTouchFocus(testWindow.getListeners().first(), testWindow, testWindow, 0, 0);
		
		isSetup = true;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		timeDilationCounter.setBounds(Gdx.graphics.getWidth() - 125, (Gdx.graphics.getHeight() / 2) + 100, 0, 0);
		timeDilationCounter.setText("game speed: " + gameScreen.getTimeDilation());
	}
}
