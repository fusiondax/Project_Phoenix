package com.phoenix.io;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.phoenix.blueprint.BlueprintData;

public class JsonUtility
{
	public static JsonValue readJson(String path)
	{
		FileHandle handle = Gdx.files.internal(path);

		JsonReader reader = new JsonReader();
		
		return reader.parse(handle);
	}
	
	public static ArrayList<BlueprintData> getAllBlueprintDataFromConfig()
	{
		ArrayList<BlueprintData> datas = new ArrayList<BlueprintData>();
		
		FileHandle blueprintDataConfigDirectory = Gdx.files.local(BlueprintDataLoader.BLUEPRINT_DATA_CONFIG_PATH);
		
		JsonReader reader = new JsonReader();
		
		Json json = new Json();
		
		for(FileHandle blueprintDataConfigFile : blueprintDataConfigDirectory.list())
		{
			datas.add(json.fromJson(BlueprintData.class, reader.parse(blueprintDataConfigFile).toString()));
		}
		
		return datas;
	}
		
	public static GameMap readJsonGameMapFile(String fileName)
	{
		GameMap gameMap = new GameMap();
		
		FileHandle handle = Gdx.files.local("gamemaps/" + fileName);
		
		JsonReader reader = new JsonReader();
		
		Json json = new Json();
		
		String test = reader.parse(handle).toString();

		gameMap = json.fromJson(GameMap.class, reader.parse(handle).toString());
		
		return gameMap;
	}
	
	public static ArrayList<String> getAllGameMapNames()
	{
		ArrayList<String> levelNameList = new ArrayList<String>();
		
		FileHandle levelDirectory = Gdx.files.internal("gamemaps");
		
		for(FileHandle levelFile : levelDirectory.list())
		{
			levelNameList.add(levelFile.name());
		}
		
		return levelNameList;
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
