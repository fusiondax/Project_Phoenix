package com.phoenix.io;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.utils.JsonValue;

import com.phoenix.blueprint.BlueprintData;

public class BlueprintDataLoader
{
	public static final String BLUEPRINT_DATA_CONFIG_PATH = "blueprint_data_config/";

	public static void loadAllDataBlueprint(HashMap<String, BlueprintData> dataMap)
	{
		ArrayList<BlueprintData> datas = JsonUtility.getAllBlueprintDataFromConfig();
		
		for(BlueprintData bd : datas)
		{
			dataMap.put(bd.buildableEntityName, bd);
		}
	}
	
	
}
