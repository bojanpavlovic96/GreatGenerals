package view.component;

import javafx.geometry.Point2D;
import root.model.component.Field;
import root.view.field.ViewField;

public class HexFieldManager implements ViewFieldManager {

	private double fieldHeight;
	private double fieldWidth;
	private double fieldBorderWidth;

	// constructors

	public HexFieldManager(double field_height,
			double field_width,
			double field_border_width) {

		super();
		this.fieldHeight = field_height;
		this.fieldWidth = field_width;
		this.fieldBorderWidth = field_border_width;
	}

	// methods

	public ViewField getViewField(Field fieldModel) {
		return new HexagonField(fieldModel,
				this.fieldWidth,
				this.fieldHeight,
				this.fieldBorderWidth);
	}

	public Point2D calcStoragePosition(Point2D position) {

		return HexagonField.calcStoragePosition(position, this.fieldWidth, this.fieldHeight);

	}

	@Override
	public Point2D calcRealPosition(Point2D storage_position) {
		return HexagonField.calcRealPosition(storage_position, this.fieldHeight / 2);
	}

	public double getHeight() {
		return this.fieldHeight;
	}

	public double getWidth() {
		return this.fieldWidth;
	}

	public double getBorderWidth() {
		return this.fieldBorderWidth;
	}

	public boolean zoomIn() {
		if (this.fieldHeight < 200) {
			this.fieldHeight += this.fieldHeight * 0.2;
			this.fieldWidth += this.fieldWidth * 0.2;
			// this.field_border_width *= this.field_border_width * 0.2;

			return true;
		}

		return false;
	}

	public boolean zoomOut() {
		if (this.fieldHeight > 50) {

			this.fieldHeight -= this.fieldHeight * 0.2;
			this.fieldWidth -= this.fieldWidth * 0.2;
			// this.field_border_width -= this.field_border_width * 0.2;

			return true;
		}

		return false;
	}

}
