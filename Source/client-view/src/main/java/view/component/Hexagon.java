package view.component;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import model.component.field.Field;
import model.component.unit.Unit;
import view.command.DrawFieldCommand;

public class Hexagon implements DrawableHexagon {

	// axial representation with two values q & r
	// pointy top hex

	// storage_position.x -> q - column
	// storage_positoin.y -> r - row
	private Point2D storage_position;

	private String BORDER_PATH = "/border.png";
	private String BATTLE_IMG_PATH = "/battle.png";

	private ViewTerrain terrain;
	private ViewUnit unit;

	private List<ViewUnit> units_in_battle;

	private Point2D hex_center;

	private double side_size;
	private double border_width;

	private double hex_width;
	private double hex_height;

	private List<Point2D> corner_points;

	// constructors

	public Hexagon(Field model) {

		this.storage_position = model.getStoragePosition();

		this.terrain = new ViewTerrain(model.getTerrain());

		if (model.getUnit() != null)
			this.unit = new ViewUnit(model.getUnit());

		this.units_in_battle = new ArrayList<ViewUnit>();
		if (model.isInBattle()) {
			for (Unit model_unit : model.getBattle()) {
				this.units_in_battle.add(new ViewUnit(model_unit));
			}
		}

		this.side_size = DrawFieldCommand.default_hex_size;
		this.border_width = DrawFieldCommand.default_hex_size;

		this.hex_center = Hexagon.calcRealPosition(this.storage_position, this.getSideSize());

		this.hex_width = this.calcHexWidth(this.getSideSize());
		this.hex_height = this.calcHexHeight(this.getSideSize());

		this.initCornerPoints();

	}

	public Hexagon(Field model, double side_size, double border_width) {

		this(model);

		this.side_size = side_size;
		this.border_width = border_width;
		this.hex_center = Hexagon.calcRealPosition(this.storage_position, this.getSideSize());

		this.initCornerPoints();
	}

	private void initCornerPoints() {

		this.corner_points = new ArrayList<Point2D>();

		// starting point is on top
		float angle = (float) (Math.PI / 2);
		float x = 0;
		float y = 0;
		// calculate coordinates of all 6 corners starting from the one on top
		for (int i = 0; i < 6; i++) {

			// may be + for x an - for y, not sure, but it works with this
			x = (float) (this.getHexCenter().getX() - (this.getSideSize() - 1) * Math.cos(angle));
			y = (float) (this.getHexCenter().getY() - (this.getSideSize() - 1) * Math.sin(angle));

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
	public static Point3D convertToCube(Point2D axial) {

		// axial.q -> x
		// axial.r -> y

		double x = axial.getX();
		double z = axial.getY();
		double y = -x - z;

		return new Point3D(x, y, z);

	}

	public static Point2D convertToAxial(Point3D cube) {

		// axial.q -> x
		// axial.r -> y

		double x = cube.getX();
		double y = cube.getZ();

		return new Point2D(x, y);

	}

	public static Point2D calcRealPosition(Point2D axial, double side_size) {

		// work with this setup
		// x -> y
		double x = side_size * (Math.sqrt(3) * axial.getX() + Math.sqrt(3) / 2 * axial.getY());
		// y
		double y = side_size * (3.0 / 2 * axial.getY());

		return new Point2D(x, y);
	}

	private static Point3D cube_round(Point3D cube) {

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

	public static Point2D calcStoragePosition(Point2D point, double side_size) {

		// x - y
		// /3 /3
		double x = (Math.sqrt(3.0) / 3 * point.getX() - 1.0 / 3 * point.getY()) / side_size;
		// y
		double y = (2.0 / 3 * point.getY()) / side_size;

		Point2D position = Hexagon.convertToAxial(Hexagon.cube_round(Hexagon.convertToCube(new Point2D(x, y))));

		return position;
	}

	// drawing methods

	private void drawBorders(GraphicsContext gc) {
		gc.save();

		Image border_img = new Image(this.BORDER_PATH);

		double angle = 30;

		for (Point2D corner : this.getCornerPoints()) {

			Transform transform = new Rotate(angle, corner.getX(), corner.getY());
			gc.setTransform(transform.getMxx(), transform.getMyx(), transform.getMxy(), transform.getMyy(),
					transform.getTx(), transform.getTy());

			gc.drawImage(border_img, corner.getX(), corner.getY(), this.getSideSize(), this.border_width);

			angle += 60;
		}

		gc.restore();
	}

	// TODO remove sleep
	private void drawLineBorders(GraphicsContext gc) {

		gc.save();

		Point2D prev_point = null;

		gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));

		// path
		gc.beginPath();

		for (Point2D corner : this.getCornerPoints()) {

			if (prev_point == null) {

				prev_point = corner;
				gc.moveTo(corner.getX(), corner.getY());

			} else {

				gc.lineTo(corner.getX(), corner.getY());

				prev_point = corner;

			}

		}

		// path
		gc.closePath();

		gc.stroke();

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		gc.restore();

	}
	// TODO uncomment or remove
	private void drawTerrain(GraphicsContext gc) {
		// if (this.terrain != null)
		// this.terrain.drawTerrain(gc, this.getHexCenter(), this.getSideSize());
	}

	private void drawUnit(GraphicsContext gc) {

		if (this.unit != null) {
			if (this.units_in_battle.isEmpty()) {
				this.unit.drawUnit(gc, this.getHexCenter(), this.getSideSize());
			} else {
				this.drawBattle(gc);
			}
		}

	}

	// DrawableHexagon

	public void drawOn(Canvas canvas) {

		GraphicsContext gc = canvas.getGraphicsContext2D();

		this.drawLineBorders(gc);
		this.drawTerrain(gc);
		this.drawUnit(gc);

	}

	public void paintHex(GraphicsContext gc, Color color) {
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

	public void clearHex(GraphicsContext gc) {
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

		this.clearHex(gc);

		// draw battle image
		Image image = new Image(this.BATTLE_IMG_PATH);

		gc.save();

		gc.drawImage(image, getHexCenter().getX() - this.getSideSize() / 2,
				getHexCenter().getY() - this.getSideSize() / 2, this.getSideSize(), this.getSideSize());

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

	public Point2D getHexCenter() {
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
