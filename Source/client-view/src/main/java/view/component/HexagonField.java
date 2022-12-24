package view.component;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import root.Point2D;
import root.model.component.Field;
import root.view.Color;
import root.view.field.ViewField;
import view.ResourceManager;

public class HexagonField implements ViewField {

	// axial representation with two values q & r
	// pointy top hex

	// storage_position.x -> q - column
	// storage_position.y -> r - row
	private Point2D storagePosition;

	// canvas coordinates
	private Point2D hexCenter;

	private boolean visibility;

	private Color borderColor;

	private ViewTerrain terrain;
	private ViewUnit unit;

	private List<ViewUnit> unitsInBattle;

	private double sideSize;
	private double borderWidth;

	private double hexWidth;
	private double hexHeight;

	private List<Point2D> cornerPoints;

	public HexagonField(Field model,
			double fieldWidth,
			double fieldHeight,
			double fieldBorder) {

		this.storagePosition = model.getStoragePosition();

		// is visible just returns visibility field from Field
		this.visibility = model.isVisible();

		if (model.getTerrain() != null)
			this.terrain = new ViewTerrain(model.getTerrain());

		if (model.getUnit() != null)
			this.unit = new ViewUnit(model.getUnit());

		this.unitsInBattle = new ArrayList<ViewUnit>();

		this.hexWidth = fieldWidth;
		this.hexHeight = fieldHeight;

		this.borderColor = model.getPlayer().getColor();

		this.sideSize = this.calculateHexSideSize(fieldHeight);

		this.borderWidth = fieldBorder;

		this.hexCenter = HexagonField.calcRealPosition(this.storagePosition, this.sideSize);

		this.initCornerPoints();

	}

	private void initCornerPoints() {

		this.cornerPoints = new ArrayList<Point2D>();

		// starting point is on top
		float angle = (float) (Math.PI / 2);
		float x = 0;
		float y = 0;
		// calculate coordinates of all 6 corners starting from the one on top
		for (int i = 0; i < 6; i++) {

			// may be + for x an - for y, not sure, but it works with this
			x = (float) (this.getFieldCenter().getX() - (this.getSideSize() - 2) * Math.cos(angle));
			y = (float) (this.getFieldCenter().getY() - (this.getSideSize() - 2) * Math.sin(angle));

			this.cornerPoints.add(new Point2D(x, y));

			// add 60 degree
			angle += Math.PI / 3;

		}

	}

	private double calcHexWidth(double sideSize) {
		return Math.sqrt(3) * sideSize;
	}

	private double calcHexHeight(double sideSize) {
		return 2 * sideSize;
	}

	// methods for position calculations
	// HexCoordinateConverter
	private double calculateHexSideSize(double hexHeight) {
		return hexHeight / 2;
	}

	static public Point3D convertToCube(Point2D axial) {

		// axial.q -> x
		// axial.r -> y

		double x = axial.getX();
		double z = axial.getY();
		double y = -x - z;

		return new Point3D(x, y, z);

	}

	static public Point2D convertToAxial(Point3D cube) {

		// axial.q -> x
		// axial.r -> y

		double x = cube.getX();
		double y = cube.getZ();

		return new Point2D(x, y);

	}

	static public Point2D calcRealPosition(Point2D axial, double sideSize) {

		// work with this setup
		// x -> y
		double x = sideSize * (Math.sqrt(3) * axial.getX() + Math.sqrt(3) / 2 * axial.getY());
		// y
		double y = sideSize * (3.0 / 2 * axial.getY());

		return new Point2D(x, y);
	}

	static private Point3D cube_round(Point3D cube) {

		double rx = Math.round(cube.getX());
		double ry = Math.round(cube.getY());
		double rz = Math.round(cube.getZ());

		double x_diff = Math.abs(rx - cube.getX());
		double y_diff = Math.abs(ry - cube.getY());
		double z_diff = Math.abs(rz - cube.getZ());

		if ((x_diff > y_diff) && (x_diff > z_diff)) {
			rx = -ry - rz;
		} else if (y_diff > z_diff) {
			ry = -rx - rz;
		} else {
			rz = -rx - ry;
		}

		return new Point3D(rx, ry, rz);
	}

	static public Point2D calcStoragePosition(Point2D point, double fieldWidth, double fieldHeight) {

		double side_size = fieldHeight / 2;

		// don't ask ...
		// x - y
		// /3 /3
		double x = (Math.sqrt(3.0) / 3 * point.getX() - 1.0 / 3 * point.getY()) / side_size;
		// y
		double y = (2.0 / 3 * point.getY()) / side_size;

		Point2D position = HexagonField
				.convertToAxial(HexagonField.cube_round(HexagonField.convertToCube(new Point2D(x, y))));

		return position;
	}

	// TODO fix borders overwriting (or don't ... )
	private void drawBorders(GraphicsContext gc) {

		gc.save();

		// every player has different border color

		// gc.setStroke(getJColor(borderColor));
		gc.setStroke(borderColor.asFxColor());
		gc.setLineWidth(this.borderWidth);

		// path
		gc.beginPath();

		Point2D prev_point = null;
		for (Point2D corner : this.getCornerPoints()) {

			if (prev_point == null) {
				// first point (corner)
				prev_point = corner;
				gc.moveTo(corner.getX(), corner.getY());

			} else {
				// every other point (corner)

				gc.lineTo(corner.getX(), corner.getY());

				prev_point = corner;

			}

		}

		// attention there should be lintTo->firstPoint, but it actually works with this

		// path
		gc.closePath();

		gc.stroke();

		gc.restore();

	}

	private void drawTerrain(GraphicsContext gc) {
		if (this.terrain != null)
			this.terrain.drawTerrain(gc, this.hexCenter, this.sideSize, this.borderWidth);
	}

	private void drawHiddenTerrain(GraphicsContext gc) {
		if (this.terrain != null) {
			this.terrain.drawHiddenTerrain(gc,
					this.hexCenter,
					this.calculateHexSideSize(hexHeight),
					this.borderWidth);
		}
	}

	private void drawUnit(GraphicsContext gc) {

		if (this.unit != null) {
			if (this.unitsInBattle.isEmpty()) {

				this.unit.drawUnit(gc, this.getFieldCenter(), this.getSideSize());
				this.paintField(gc,this.unit.getHighlightColor());

			} else {
				this.drawBattle(gc);
			}
		}

	}

	// DrawableHexagon

	@Override
	public void drawOn(GraphicsContext gc) {

		// System.out.println("Drawing hex. field ... ");

		// every field has borders
		this.drawBorders(gc);

		// not every field is visible
		// enemy field are hidden with "fog"
		if (this.visibility) {
			// if field is visible draw terrain and unit

			this.drawTerrain(gc);
			this.drawUnit(gc);

		} else {
			// field is still hidden

			this.drawHiddenTerrain(gc);

		}

	}

	@Override
	public void paintField(GraphicsContext gc, Color color) {
		gc.save();

		double[] xs = new double[6];
		double[] ys = new double[6];

		for (int i = 0; i < 6; i++) {
			xs[i] = this.getCornerPoints().get(i).getX();
			ys[i] = this.getCornerPoints().get(i).getY();
		}

		// gc.setFill(getJColor(color));
		gc.setFill(color.asFxColor());
		gc.fillPolygon(xs, ys, 6);

		gc.restore();
	}

	@Override
	public void clearField(GraphicsContext gc) {
		gc.save();

		double[] xs = new double[6];
		double[] ys = new double[6];

		for (int i = 0; i < 6; i++) {
			xs[i] = this.getCornerPoints().get(i).getX();
			ys[i] = this.getCornerPoints().get(i).getY();
		}

		// gc.setFill(getJColor(Color.GRAY));
		gc.setFill(Color.GRAY.asFxColor());
		gc.fillPolygon(xs, ys, 6);

		gc.restore();
	}

	public void drawBattle(GraphicsContext gc) {

		this.clearField(gc);

		// attention wrong way
		Image image = ResourceManager.getInstance().getUnit("battle");

		gc.save();

		gc.drawImage(image,
				getFieldCenter().getX() - this.getSideSize() / 2,
				getFieldCenter().getY() - this.getSideSize() / 2,
				this.getSideSize(),
				this.getSideSize());

		gc.restore();

	}

	// getters and setters
	public List<Point2D> getCornerPoints() {
		return cornerPoints;
	}

	public double getHexWidth() {
		return hexWidth;
	}

	public double getHexHeight() {
		return hexHeight;
	}

	public double getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(double borderWidth) {
		this.borderWidth = borderWidth;
	}

	@Override
	public Point2D getFieldCenter() {
		return hexCenter;
	}

	public void setHexCenter(Point2D hex_center) {
		this.hexCenter = hex_center;
	}

	public double getSideSize() {
		return sideSize;
	}

	public void setSideSize(double side_size) {
		this.sideSize = side_size;

		this.hexWidth = this.calcHexWidth(this.sideSize);
		this.hexHeight = this.calcHexHeight(this.sideSize);

		this.initCornerPoints();

	}

}
