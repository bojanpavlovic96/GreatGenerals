package view.component;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import model.component.field.Field;
import model.component.unit.Unit;
import view.ResourceManager;

public class HexagonField implements ViewField {

	// axial representation with two values q & r
	// pointy top hex

	// storage_position.x -> q - column
	// storage_positoin.y -> r - row
	private Point2D storage_position;

	// canvas coordinates
	private Point2D hex_center;

	private boolean visibility;

	// attention
	// private Color border_color = Color.rgb(200, 100, 100);
	private Color border_color;
	private ViewTerrain terrain;
	private ViewUnit unit;

	private List<ViewUnit> units_in_battle;

	private double side_size;
	private double border_width;

	private double hex_width;
	private double hex_height;

	private List<Point2D> corner_points;

	// constructors

	public HexagonField(Field model, double field_width, double field_height, double border_width) {

		this.storage_position = model.getStoragePosition();

		// is visible just returns visibility field from Field
		this.visibility = model.isVisible();

		if (model.getTerrain() != null)
			this.terrain = new ViewTerrain(model.getTerrain());

		if (model.getUnit() != null)
			this.unit = new ViewUnit(model.getUnit());

		this.units_in_battle = new ArrayList<ViewUnit>();
		if (model.isInBattle()) {
			for (Unit model_unit : model.getBattle()) {
				this.units_in_battle.add(new ViewUnit(model_unit));
			}
		}

		this.hex_width = field_width;
		this.hex_height = field_height;

		this.border_color = model.getPlayer().getPlayerColor();

		this.side_size = this.calculateHexSideSize(field_height);

		this.border_width = border_width;

		this.hex_center = HexagonField.calcRealPosition(this.storage_position, this.side_size);

		this.initCornerPoints();

	}

	// methods

	private void initCornerPoints() {

		this.corner_points = new ArrayList<Point2D>();

		// starting point is on top
		float angle = (float) (Math.PI / 2);
		float x = 0;
		float y = 0;
		// calculate coordinates of all 6 corners starting from the one on top
		for (int i = 0; i < 6; i++) {

			// may be + for x an - for y, not sure, but it works with this
			x = (float) (this.getFieldCenter().getX() - (this.getSideSize() - 2) * Math.cos(angle));
			y = (float) (this.getFieldCenter().getY() - (this.getSideSize() - 2) * Math.sin(angle));

			this.corner_points.add(new Point2D(x, y));

			// add 60 degree
			angle += Math.PI / 3;

		}

	}

	private double calcHexWidth(double side_size) {
		return Math.sqrt(3) * side_size;
	}

	private double calcHexHeight(double side_size) {
		return 2 * side_size;
	}

	// methods for position calculations
	// HexCoordinateConverter
	private double calculateHexSideSize(double hex_height) {
		return hex_height / 2;
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

	static public Point2D calcRealPosition(Point2D axial, double side_size) {

		// work with this setup
		// x -> y
		double x = side_size * (Math.sqrt(3) * axial.getX() + Math.sqrt(3) / 2 * axial.getY());
		// y
		double y = side_size * (3.0 / 2 * axial.getY());

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

	static public Point2D calcStoragePosition(Point2D point, double field_width, double field_height) {

		double side_size = field_height / 2;

		// don't ask ...
		// x - y
		// /3 /3
		double x = (Math.sqrt(3.0) / 3 * point.getX() - 1.0 / 3 * point.getY()) / side_size;
		// y
		double y = (2.0 / 3 * point.getY()) / side_size;

		Point2D position = HexagonField.convertToAxial(HexagonField.cube_round(HexagonField.convertToCube(new Point2D(	x,
																														y))));

		return position;
	}

	// TODO borders are overwriting
	private void drawBorders(GraphicsContext gc) {

		gc.save();

		// every player has different border color
		gc.setStroke(this.border_color);
		gc.setLineWidth(this.border_width);

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
			this.terrain.drawTerrain(gc, this.hex_center, this.side_size, this.border_width);
	}

	private void drawHiddenTerrain(GraphicsContext gc) {
		if (this.terrain != null) {
			this.terrain.drawHiddenTerrain(	gc,
											this.hex_center,
											this.calculateHexSideSize(hex_height),
											this.border_width);
		}
	}

	private void drawUnit(GraphicsContext gc) {

		if (this.unit != null) {
			if (this.units_in_battle.isEmpty()) {
				this.unit.drawUnit(gc, this.getFieldCenter(), this.getSideSize());
			} else {
				this.drawBattle(gc);
			}
		}

	}

	// DrawableHexagon

	public void drawOn(Canvas canvas) {

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// every field has borders
		this.drawBorders(gc);

		// not every field is visible
		// enemy field are hidden with fog
		if (this.visibility) {
			// if field is visible draw terrain and unit

			this.drawTerrain(gc);
			this.drawUnit(gc);

		} else {
			// field is still hidden

			this.drawHiddenTerrain(gc);

		}

	}

	public void paintField(GraphicsContext gc, Color color) {
		gc.save();

		double[] xs = new double[6];
		double[] ys = new double[6];

		for (int i = 0; i < 6; i++) {
			xs[i] = this.getCornerPoints().get(i).getX();
			ys[i] = this.getCornerPoints().get(i).getY();
		}

		gc.setFill(color);
		gc.fillPolygon(xs, ys, 6);

		gc.restore();
	}

	public void clearField(GraphicsContext gc) {
		gc.save();

		double[] xs = new double[6];
		double[] ys = new double[6];

		for (int i = 0; i < 6; i++) {
			xs[i] = this.getCornerPoints().get(i).getX();
			ys[i] = this.getCornerPoints().get(i).getY();
		}

		gc.setFill(Color.GRAY);
		gc.fillPolygon(xs, ys, 6);

		gc.restore();
	}

	public void drawBattle(GraphicsContext gc) {

		this.clearField(gc);

		// attention wrong way
		Image image = ResourceManager.getInstance().getUnit("battle");

		gc.save();

		gc.drawImage(	image,
						getFieldCenter().getX() - this.getSideSize() / 2,
						getFieldCenter().getY() - this.getSideSize() / 2,
						this.getSideSize(),
						this.getSideSize());

		gc.restore();

	}

	// getters and setters
	public List<Point2D> getCornerPoints() {
		return corner_points;
	}

	public double getHexWidth() {
		return hex_width;
	}

	public double getHexHeight() {
		return hex_height;
	}

	public double getBorderWidth() {
		return border_width;
	}

	public void setBorderWidth(double border_width) {
		this.border_width = border_width;
	}

	public Point2D getFieldCenter() {
		return hex_center;
	}

	public void setHexCenter(Point2D hex_center) {
		this.hex_center = hex_center;
	}

	public double getSideSize() {
		return side_size;
	}

	public void setSideSize(double side_size) {
		this.side_size = side_size;

		this.hex_width = this.calcHexWidth(this.side_size);
		this.hex_height = this.calcHexHeight(this.side_size);

		this.initCornerPoints();

	}

}
