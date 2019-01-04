package view.component;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Terrain {

	private String ICON_PATH = "/mountain.png";

	public Terrain() {

	}

	public Terrain(model.component.Terrain model) {
		
	}

	public void drawTerrain(GraphicsContext gc, Point2D hex_center, double hex_side) {

		Image image = new Image(this.ICON_PATH);
		double hex_width = (float) (Math.sqrt(3) * hex_side);
		double hex_height = 2 * hex_side;

		gc.save();

		gc.drawImage(image, hex_center.getX() - hex_side / 2, hex_center.getY() - hex_side / 2, hex_side, hex_side);

		gc.restore();

	}

}
