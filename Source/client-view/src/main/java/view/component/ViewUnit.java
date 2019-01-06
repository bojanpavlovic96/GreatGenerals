package view.component;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.component.unit.Unit;

public class ViewUnit {

	private String ICON_PATH = "/soldier.png";

	private int health;
	private int attack;
	private int defense;
	
	public ViewUnit(Unit model) {
		
	}
	
	public ViewUnit(int health, int attack, int defense) {

		this.health = health;
		this.attack = attack;
		this.defense = defense;
	}

	public void drawUnit(GraphicsContext gc, Point2D hex_center, double hex_side) {

		Image image = new Image(this.ICON_PATH);

		gc.save();

		gc.drawImage(image, hex_center.getX() - hex_side / 2, hex_center.getY() - hex_side / 2, hex_side, hex_side);

		gc.restore();

	}

}
