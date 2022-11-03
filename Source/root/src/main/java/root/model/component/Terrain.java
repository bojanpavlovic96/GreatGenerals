package root.model.component;

public class Terrain {

	// these are lowercase because they are mapped to the same-named resource files
	public static enum TerrainType {
		mountains,
		water
	}

	private Terrain.TerrainType type;
	private float intensity;

	public Terrain(Terrain.TerrainType type, float intensity) {
		this.type = type;
		this.intensity = intensity;
	}

	public Terrain.TerrainType getType() {
		return this.type;
	}

	public float getIntensity() {
		return this.intensity;
	}

}
