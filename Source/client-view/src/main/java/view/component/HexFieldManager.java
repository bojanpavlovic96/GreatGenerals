package view.component;

import root.Point2D;
import root.model.component.Field;
import root.view.field.ViewField;

public class HexFieldManager implements ViewFieldManager {

	private double fieldHeight;
	private double fieldWidth;
	private double fieldBorderWidth;

	public HexFieldManager(double fieldHeight,
			double fieldWidth,
			double fieldBorderWidth) {

		this.fieldHeight = fieldHeight;
		this.fieldWidth = fieldWidth;
		this.fieldBorderWidth = fieldBorderWidth;
	}

	public ViewField getViewField(Field fieldModel) {
		return new HexagonField(fieldModel,
				this.fieldWidth,
				this.fieldHeight,
				this.fieldBorderWidth);
	}

	// TODO logic for the next two methods should be inside this class ... ? 
	// ViewField/View should use this class to determine 
	@Override
	public Point2D calcStoragePosition(Point2D position) {
		return HexagonField.calcStoragePosition(position, this.fieldWidth, this.fieldHeight);
	}

	@Override
	public Point2D calcRealPosition(Point2D storagePosition) {
		return HexagonField.calcRealPosition(storagePosition, this.fieldHeight / 2);
	}

	@Override
	public double getHeight() {
		return this.fieldHeight;
	}

	@Override
	public double getWidth() {
		return this.fieldWidth;
	}

	@Override
	public double getBorderWidth() {
		return this.fieldBorderWidth;
	}

	@Override
	public boolean zoomIn() {
		if (this.fieldHeight < 200) {
			this.fieldHeight += this.fieldHeight * 0.2;
			this.fieldWidth += this.fieldWidth * 0.2;
			// this.field_border_width *= this.field_border_width * 0.2;

			return true;
		}

		return false;
	}

	@Override
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
