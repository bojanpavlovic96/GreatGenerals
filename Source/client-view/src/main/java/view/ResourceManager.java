package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.image.Image;

// attention this thing is mess ... to something 

public class ResourceManager {

	// represents game "theme"
	static private String ASSETS_TYPE = "default";

	// paths
	static private final String ASSETS_PATH = "assets";

	static private final String TERRAIN_LIST_PATH = "";
	static private final String UNIT_LIST_PATH = "";

	static private final String CONFIG_SUFFIX = ".json";

	static private final String UNIT_KEY_PREFIX = "unit-";
	static private final String TERRAIN_KEY_PREFIX = "terrain-";

	// instance
	static private ResourceManager instance;

	// type included
	private String concrete_assets_path;

	// unit/terrain-name-range <- key format
	private Map<String, Image> assets;

	private JSONArray units_list;
	private JSONArray terrains_list;

	// methods

	private ResourceManager() {

		this.loadResources();

	}

	private void loadResources() {

		this.concrete_assets_path = ResourceManager.ASSETS_PATH + "/" + ResourceManager.ASSETS_TYPE;

		this.loadTerrainsList();
		this.loadUnitsList();

		// load available units from units list

		JSONObject unit = null;

		this.assets = new HashMap<String, Image>();

		final String UNITS_PATH = ResourceManager.ASSETS_PATH + "/" + ResourceManager.ASSETS_TYPE + "/unit";

		for (int index = 0; index < this.units_list.length(); index++) {

			unit = this.units_list.getJSONObject(index);

			for (int range = unit.getInt("range-start"); range <= unit.getInt("range-end"); range++) {

				this.assets.put(ResourceManager.UNIT_KEY_PREFIX + unit.getString("name"),
								new Image("/" + UNITS_PATH
											+ "/"
											+ unit.getString("name")
											+ unit.getString("extension")));

			}

		}

		// load available terrains from terrains list

		final String TERRAINS_PATH = ResourceManager.ASSETS_PATH + "/"
										+ ResourceManager.ASSETS_TYPE
										+ "/terrain";

		JSONObject terrain = null;
		for (int index = 0; index < this.terrains_list.length(); index++) {

			terrain = this.terrains_list.getJSONObject(index);

			for (int range = terrain.getInt("range-start"); range <= terrain.getInt("range-end"); range++) {

				this.assets.put(this.constructTerrainKey(terrain.getString("name"), range),
								new Image("/" + TERRAINS_PATH
											+ "/"
											+ terrain.getString("name")
											+ "-"
											+ range
											+ terrain.getString("extension")));
			}

		}

	}

	// same as loadUnitsList
	private void loadTerrainsList() {

		try {

			String LOGICAL_LIST_PATH = ResourceManager.ASSETS_PATH + "/"
										+ ResourceManager.ASSETS_TYPE
										+ "/"
										+ "terrain"
										+ "/"
										+ "terrain_list"
										+ ResourceManager.CONFIG_SUFFIX;

			String PHYSICAL_LIST_PATH = this.getClass().getClassLoader().getResource(LOGICAL_LIST_PATH)
					.getPath();

			FileReader reader = new FileReader(new File(PHYSICAL_LIST_PATH));
			BufferedReader buff_reader = new BufferedReader(reader);

			StringBuilder buffer = new StringBuilder();

			String line = null;
			while ((line = buff_reader.readLine()) != null) {
				buffer.append(line);
			}

			buff_reader.close();

			this.terrains_list = new JSONArray(buffer.toString());

		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	// same as loadTerrainsList
	private void loadUnitsList() {

		try {

			String LOGICAL_LIST_PATH = ResourceManager.ASSETS_PATH + "/"
										+ ResourceManager.ASSETS_TYPE
										+ "/"
										+ "unit"
										+ "/"
										+ "unit_list"
										+ ResourceManager.CONFIG_SUFFIX;

			String PHYSICAL_LIST_PATH = this.getClass().getClassLoader().getResource(LOGICAL_LIST_PATH)
					.getPath();

			FileReader reader = new FileReader(new File(PHYSICAL_LIST_PATH));
			BufferedReader buff_reader = new BufferedReader(reader);

			StringBuilder buffer = new StringBuilder();

			String line = null;
			while ((line = buff_reader.readLine()) != null) {
				buffer.append(line);
			}

			buff_reader.close();

			this.units_list = new JSONArray(buffer.toString());

		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public ResourceManager getInstance() {

		if (ResourceManager.instance == null) {
			ResourceManager.instance = new ResourceManager();
		}

		return ResourceManager.instance;
	}

	static public void setAssetsType(String new_type) {
		ResourceManager.ASSETS_TYPE = new_type;
	}

	static public String getAssetsType() {
		return ResourceManager.ASSETS_TYPE;
	}

	public Image getTerrain(String terrain_name, int intensity) {

		return this.assets.get(this.constructTerrainKey(terrain_name, intensity));

	}

	public Image getUnit(String unit_name) {

		return this.assets.get(this.constructUnitKey(unit_name));

	}

	private String constructTerrainKey(String terrain_name, int intensity) {

		return ResourceManager.TERRAIN_KEY_PREFIX + terrain_name + "-" + this.scaleIntensity(intensity);

	}

	// implement
	private int scaleIntensity(int intensity) {

		return intensity;

	}

	private String constructUnitKey(String unit_name) {

		return ResourceManager.UNIT_KEY_PREFIX + unit_name;

	}

}
