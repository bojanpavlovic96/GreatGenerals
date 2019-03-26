package root.model.component;

public class Terrain {

	private String name;
	private int intensity;

	public Terrain(String name, int intensity) {
		this.name = name;
		this.intensity = intensity;
	}

	public String getName() {
		return this.name;
	}

	public int getIntensity() {
		return this.intensity;
	}

}
