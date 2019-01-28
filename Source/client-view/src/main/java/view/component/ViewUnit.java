package view.component;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.component.unit.Unit;
import view.ResourceManager;

public class ViewUnit {

	private String unit_name;

	// implement
	private int health;
	private int attack;
	private int defense;

	// methods

	public ViewUnit(Unit model) {

		this.unit_name = model.getUnitName();

	}

	// attention it wont be used (you can't just create random unit)
	public ViewUnit(String unit_name, int health, int attack, int defense) {

		this.unit_name = unit_name;

		this.health = health;
		this.attack = attack;
		this.defense = defense;
	}

	public void drawUnit(GraphicsContext gc, Point2D hex_center, double hex_side) {

		Image image = ResourceManager.getInstance().getUnit(this.unit_name);

		gc.save();

		gc.drawImage(	image,
						hex_center.getX() - hex_side / 2-7,
						hex_center.getY() - hex_side / 2-5,
						hex_side + 10,
						hex_side + 10);

		gc.restore();

	}

}
