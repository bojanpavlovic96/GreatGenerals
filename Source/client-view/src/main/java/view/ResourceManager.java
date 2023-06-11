package view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.image.Image;

// attention this thing is a mess ... do something 
// should be populated with resources "downloaded" from the server ... maybe

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

	static private final String DEFAULT_ARROW = "arrow";
	static private final String DEFAULT_ARROW_PATH = "assets/default/arrow.png";
	static private final String DEFAULT_BATTLE_ICON_PATH = "assets/default/battle.png";

	static private final String COINS_PATH = "assets/default/coins.png";

	// #region singletonStuff
	// instance
	static private ResourceManager instance;

	static public ResourceManager getInstance() {

		if (ResourceManager.instance == null) {
			ResourceManager.instance = new ResourceManager();
		}

		return ResourceManager.instance;
	}

	// #endregion singletonStuff

	// type included
	private String concreteAssetsPath;

	// [unit|terrain]-name-range <- key format
	private Map<String, Image> assets;

	private Map<String, Image> attackArrows;

	private Image coins;

	private Image battleIcon;

	private JSONArray unitsList;
	private JSONArray terrainsList;

	// methods

	private ResourceManager() {

		this.loadResources();

	}

	private void loadResources() {

		this.concreteAssetsPath = ResourceManager.ASSETS_PATH
				+ "/"
				+ ResourceManager.ASSETS_TYPE;

		attackArrows = new HashMap<>();
		var arrow = new Image(DEFAULT_ARROW_PATH);
		attackArrows.put(DEFAULT_ARROW, arrow);

		battleIcon = new Image(DEFAULT_BATTLE_ICON_PATH);

		coins = new Image(COINS_PATH);

		this.loadTerrainsList();
		this.loadUnitsList();

		// load available units from units list

		JSONObject unit = null;

		this.assets = new HashMap<String, Image>();

		final String UNITS_PATH = ResourceManager.ASSETS_PATH
				+ "/"
				+ ResourceManager.ASSETS_TYPE
				+ "/unit";

		for (int index = 0; index < this.unitsList.length(); index++) {

			unit = this.unitsList.getJSONObject(index);

			for (int range = unit.getInt("range_start"); range <= unit.getInt("range_end"); range++) {

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
		for (int index = 0; index < this.terrainsList.length(); index++) {

			terrain = this.terrainsList.getJSONObject(index);

			var start = terrain.getInt("range_start");
			var end = terrain.getInt("range_end");

			for (int range = start; range <= end; range++) {

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
					+ "/terrain"
					+ "/terrain_list"
					+ ResourceManager.CONFIG_SUFFIX;

			// String PHYSICAL_LIST_PATH =
			// this.getClass().getClassLoader().getResource(LOGICAL_LIST_PATH).getPath();

			InputStream input_stream = this.getClass()
					.getClassLoader()
					.getResourceAsStream(LOGICAL_LIST_PATH);
			InputStreamReader stream_reader = new InputStreamReader(input_stream);
			BufferedReader buff_reader = new BufferedReader(stream_reader);

			StringBuilder buffer = new StringBuilder();

			String line = null;
			while ((line = buff_reader.readLine()) != null) {
				buffer.append(line);
			}

			buff_reader.close();
			stream_reader.close();
			input_stream.close();

			this.terrainsList = new JSONArray(buffer.toString());

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

			// String PHYSICAL_LIST_PATH =
			// this.getClass().getClassLoader().getResource(LOGICAL_LIST_PATH).getPath();

			InputStream input_stream = this.getClass().getClassLoader().getResourceAsStream(LOGICAL_LIST_PATH);
			InputStreamReader stream_reader = new InputStreamReader(input_stream);
			BufferedReader buff_reader = new BufferedReader(stream_reader);

			StringBuilder buffer = new StringBuilder();

			String line = null;
			while ((line = buff_reader.readLine()) != null) {
				buffer.append(line);
			}

			buff_reader.close();

			this.unitsList = new JSONArray(buffer.toString());

		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public String getAssetsType() {
		return ResourceManager.ASSETS_TYPE;
	}

	public Image getTerrain(String terrainName, float intensity) {

		return this.assets.get(this.constructTerrainKey(terrainName, (int) intensity));

	}

	public Image getUnit(String unitName) {

		return this.assets.get(this.constructUnitKey(unitName));

	}

	public String constructTerrainKey(String terrainName, int intensity) {

		return ResourceManager.TERRAIN_KEY_PREFIX + terrainName + "-" + this.scaleIntensity(intensity);

	}

	// implement
	private int scaleIntensity(int intensity) {

		return intensity;

	}

	public String constructUnitKey(String unitName) {

		return ResourceManager.UNIT_KEY_PREFIX + unitName;

	}

	public Image getByKey(String key) {
		return assets.get(key);
	}

	// TODO add attack stuff as well ... 

	public Image getBattleArrow(String attackType) {
		// attackType IS ignored 
		return attackArrows.get(DEFAULT_ARROW);
	}

	public Image getBattleIcon() {
		return battleIcon;
	}

	public Image getCoins() {
		return this.coins;
	}

}
