package com.phoenix.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.phoenix.player.action.PlayerAction;
import com.phoenix.screens.GameScreen;
import com.phoenix.ui.BlueprintBar;
import com.phoenix.ui.FramerateCounterLabel;
import com.phoenix.ui.KeybindTable;
import com.phoenix.ui.TimeDilationLabel;

public class PhoenixWindowBuilder
{
	private Skin skin;
	
	private String labelStyleName;
	private String windowStyleName;

	private GameScreen gameScreen;

	public PhoenixWindowBuilder(Skin skin, String labelStyleName, String windowStyleName, GameScreen gameScreen)
	{
		this.skin = skin;
		this.labelStyleName = labelStyleName;
		this.windowStyleName = windowStyleName;
		this.gameScreen = gameScreen;
	}

	/**
	 * 
	 * 
	 * @param windowName
	 * @return the window with the given name, or null if the name wasn't found
	 */
	public PhoenixWindow getWindow(String windowName)
	{
		PhoenixWindow window = null;
		
		switch(windowName)
		{
			case "misc_info":
			{
				window = buildMiscInfoWindow();
				break;
			}
			
			case "blueprint_bar":
			{
				window = buildBlueprintBarWindow();
				break;
			}
			
			case "keybind_manager":
			{
				window = buildKeybindManager();
				break;
			}
			
			default:
			{
				System.out.println("the given window name wasn't found");
				break;
			}
		}
		
		return window;
	}
	
	private PhoenixWindow buildKeybindManager()
	{
		KeybindTable table = new KeybindTable(skin, gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME));
		
		Rectangle keybindManagerWindowDisposition = new Rectangle(Gdx.graphics.getWidth() - 200,
				Gdx.graphics.getHeight() / 2, 500, 400);
		
		PhoenixWindow keybindManagerWindow = new PhoenixWindow("keybind_man", "Keybind Manager", keybindManagerWindowDisposition, skin,
				windowStyleName, gameScreen);
		
		keybindManagerWindow.add(table);
		
		return keybindManagerWindow;
	}

	private PhoenixWindow buildMiscInfoWindow()
	{
		FramerateCounterLabel framerateCounter = new FramerateCounterLabel(skin, labelStyleName);
		TimeDilationLabel timeDilationCounter = new TimeDilationLabel(skin, labelStyleName, gameScreen);

		Rectangle miscInformationWindowDisposition = new Rectangle(Gdx.graphics.getWidth() - 200,
				Gdx.graphics.getHeight() / 2, 200, 100);
		
		PhoenixWindow miscInformationWindow = new PhoenixWindow("misc_info", "Misc Information", miscInformationWindowDisposition, skin,
				windowStyleName, gameScreen);
		miscInformationWindow.add(framerateCounter);
		miscInformationWindow.row();
		miscInformationWindow.add(timeDilationCounter);
		
		return miscInformationWindow;
	}
	
	private PhoenixWindow buildBlueprintBarWindow()
	{
		BlueprintBar blueprintBar = new BlueprintBar(skin, gameScreen.playerList.get(GameScreen.ACTIVE_PLAYER_NAME));
		
		Rectangle blueprintBarWindowDisposition = new Rectangle(0, Gdx.graphics.getHeight() - 400 , 50, 400);
		
		PhoenixWindow blueprintBarWindow = new PhoenixWindow("blueprint_bar", "Blueprint Bar", blueprintBarWindowDisposition, skin, "default_window", gameScreen);
		blueprintBarWindow.add(blueprintBar);
		
		return blueprintBarWindow;
	}
}
