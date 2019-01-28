package model.component;

public class Terrain {

	private String terrain_name;
	// attention looks stupid
	private int intensity;

	// methods

	public Terrain(String terrain_name, int intensity) {
		super();

		this.terrain_name = terrain_name;
		this.intensity = intensity;
	}

	public String getTerrainName() {
		return terrain_name;
	}

	public void setTerrainName(String terrain_type) {
		this.terrain_name = terrain_type;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

}
