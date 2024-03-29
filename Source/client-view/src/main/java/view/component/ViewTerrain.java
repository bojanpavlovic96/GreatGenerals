package view.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import root.Point2D;
import root.model.component.Terrain;
import view.ResourceManager;

public class ViewTerrain {

	public String terrainName;
	public float intensity;

	public ViewTerrain(Terrain model) {

		// TODO this does not look good
		// enum fields have to be the same as the file names ... 
		// maybe do some mapping in resourceManger
		this.terrainName = model.getType().toString();
		this.intensity = model.getIntensity();

	}

	public void drawTerrain(GraphicsContext gc,
			Point2D hexCenter,
			double hexSide,
			double borderWidth) {

		// TODO intensity hardcoded 
		Image image = ResourceManager.getInstance().getTerrain(this.terrainName, 1);

		if (image == null) {
			System.out.println("Terrain image is null... "
					+ this.terrainName
					+ "\t @ ViewTerrain.drawTerrain");

			return;
		}

		double hex_width = (double) (Math.sqrt(3) * hexSide);
		double hex_height = 2 * hexSide;

		gc.save();

		gc.drawImage(image,
				hexCenter.getX() - hex_width / 2 + borderWidth,
				hexCenter.getY() - hex_height / 2 + borderWidth,
				hex_width - 2 * borderWidth,
				hex_height - 2 * borderWidth);

		gc.restore();

	}

	public void drawHiddenTerrain(GraphicsContext gc,
			Point2D hexCenter,
			double hexSide,
			double borderWidth) {

		String old_terrain_name = this.terrainName;
		this.terrainName = "fog";

		float old_intensity = this.intensity;
		this.intensity = 1;

		this.drawTerrain(gc, hexCenter, hexSide, borderWidth);

		this.terrainName = old_terrain_name;
		this.intensity = old_intensity;

	}

}
