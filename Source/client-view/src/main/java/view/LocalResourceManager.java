package view;

import javafx.scene.image.Image;

// if ever decided that resource manager should be refactored 
public class LocalResourceManager {
	private static LocalResourceManager instance;

	public static LocalResourceManager getInstance() {
		if (instance == null) {
			instance = new LocalResourceManager();
		}

		return instance;
	}

	private ResourceList terrainList;
	private ResourceList unitsList;

	private LocalResourceManager() {
		loadTerrainsList();
		loadUnitsList();

		loadTerrains();
		loadUnits();
	}

	private void loadTerrainsList() {

	}

	private void loadTerrains() {

	}

	private void loadUnitsList() {

	}

	private void loadUnits() {

	}

	public Image getUnit(String type) {
		return null;
	}

	public Image getTerrain(String type, int intensity) {
		return null;
	}

	public Image getCoins() {
		return null;
	}

}
