package root.model.component;

public class Terrain {

	// these are lowercase beacuse they are mapped to the sampe-named res files
	public static enum TerrainType {
		mountains,
		watter
	}

	private Terrain.TerrainType type;
	private int intensity;

	public Terrain(Terrain.TerrainType type, int intensity) {
		this.type = type;
		this.intensity = intensity;
	}

	public Terrain.TerrainType getType() {
		return this.type;
	}

	public int getIntensity() {
		return this.intensity;
	}

}
