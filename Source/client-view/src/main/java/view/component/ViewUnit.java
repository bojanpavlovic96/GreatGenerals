package view.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import root.Point2D;
import root.model.component.Unit;
import root.view.Color;
import view.ResourceManager;

public class ViewUnit {

	private String unitName;

	private Color highlightColor;

	private int health;
	private int attack;
	private int defense;

	public ViewUnit(Unit model) {
		this.unitName = model.getUnitType().toString();

		var pColor = model.getOwner().getColor();
		this.highlightColor = new Color(pColor.red,
				pColor.green,
				pColor.blue,
				0.3);
	}

	// attention it wont be used (you can't just create random unit)
	// public ViewUnit(String unit_name, int health, int attack, int defense) {

	// 	this.unitName = unit_name;

	// 	this.health = health;
	// 	this.attack = attack;
	// 	this.defense = defense;
	// }

	public void drawUnit(GraphicsContext gc, Point2D hex_center, double hex_side) {

		Image image = ResourceManager.getInstance().getUnit(this.unitName);

		gc.save();

		double image_width = hex_side * 1.4;
		double image_height = hex_side * 1.4;

		gc.drawImage(image,
				hex_center.getX() - image_width / 2,
				hex_center.getY() - image_height / 2,
				image_width,
				image_height);
		gc.restore();

	}

	public Color getHighlightColor(){
		return this.highlightColor;
	}

}
