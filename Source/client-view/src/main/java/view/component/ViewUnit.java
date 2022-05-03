package view.component;

// import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import root.Point2D;
import root.model.component.Unit;
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

		double image_width = hex_side * 1.4;
		double image_height = hex_side * 1.4;

		gc.drawImage(image,
				hex_center.getX() - image_width / 2,
				hex_center.getY() - image_height / 2,
				image_width,
				image_height);
		gc.restore();

	}

}
