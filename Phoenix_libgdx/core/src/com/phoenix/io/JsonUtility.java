package com.phoenix.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class JsonUtility
{
	public static JsonValue readJson(String path)
	{
		FileHandle handle = Gdx.files.internal(path);

		JsonReader reader = new JsonReader();
		
		return reader.parse(handle);
	}
		
	public static GameMap readJsonGameMapFile(String fileName)
	{
		GameMap gameMap = new GameMap();
		
		FileHandle handle = Gdx.files.local("gamemaps/" + fileName);
		
		JsonReader reader = new JsonReader();
		
		Json json = new Json();
		
		//System.out.println(reader.parse(handle).toString());

		gameMap = json.fromJson(GameMap.class, reader.parse(handle).toString());
		
		return gameMap;
	}
	
	public static void writeJsonGameMapFile(String fileName, GameMap gameMap)
	{
		FileHandle handle = Gdx.files.local("gamemaps/" + fileName);
		
		Json json = new Json();
		json.setOutputType(JsonWriter.OutputType.json);

		//TODO write gamemap files with compact json, not prettyprint
		//handle.writeString(json.toJson(gameMap), false);
		handle.writeString(json.prettyPrint(gameMap), false);
	}
}
