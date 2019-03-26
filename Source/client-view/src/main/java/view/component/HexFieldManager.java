package view.component;

import javafx.geometry.Point2D;
import root.model.component.Field;
import root.view.field.ViewField;

public class HexFieldManager implements ViewFieldManager {

	private double field_height;
	private double field_width;
	private double field_border_width;

	// constructors

	public HexFieldManager(double field_height, double field_width, double field_border_width) {
		super();
		this.field_height = field_height;
		this.field_width = field_width;
		this.field_border_width = field_border_width;
	}

	// methods

	public ViewField getViewField(Field model) {
		return new HexagonField(model, this.field_width, this.field_height, this.field_border_width);
	}

	public Point2D calcStoragePosition(Point2D position) {

		return HexagonField.calcStoragePosition(position, this.field_width, this.field_height);

	}

	@Override
	public Point2D calcRealPosition(Point2D storage_position) {
		return HexagonField.calcRealPosition(storage_position, this.field_height / 2);
	}

	public double getHeight() {
		return this.field_height;
	}

	public double getWidth() {
		return this.field_width;
	}

	public double getBorderWidth() {
		return this.field_border_width;
	}

	public boolean zoomIn() {
		if (this.field_height < 200) {
			this.field_height += this.field_height * 0.2;
			this.field_width += this.field_width * 0.2;
			// this.field_border_width *= this.field_border_width * 0.2;

			return true;
		}

		return false;
	}

	public boolean zoomOut() {
		if (this.field_height > 50) {

			this.field_height -= this.field_height * 0.2;
			this.field_width -= this.field_width * 0.2;
			// this.field_border_width -= this.field_border_width * 0.2;

			return true;
		}

		return false;
	}

}
