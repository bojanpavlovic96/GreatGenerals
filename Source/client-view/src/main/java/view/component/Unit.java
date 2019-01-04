package view.component;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Unit {

	private String ICON_PATH = "/soldier.png";

	private int health;
	private int attack;
	private int defense;
	
	public Unit(model.component.Unit model) {
		
	}
	
	public Unit(int health, int attack, int defense) {

		this.health = health;
		this.attack = attack;
		this.defense = defense;
	}

	public void drawUnit(GraphicsContext gc, Point2D hex_center, double hex_side) {

		Image image = new Image(this.ICON_PATH);
		double hex_width = (float) (Math.sqrt(3) * hex_side);
		double hex_height = 2 * hex_side;

		gc.save();

		gc.drawImage(image, hex_center.getX() - hex_side / 2, hex_center.getY() - hex_side / 2, hex_side, hex_side);

		gc.restore();

	}

}
