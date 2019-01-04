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
import model.Field;
import view.command.DrawFieldCommand;

public class Hexagon implements HexCoordinateConverter, DrawableHexagon {

	// axial representation with two values q & r
	// pointy top hex

	// storage_position.x -> q - column
	// storage_positoin.y -> r - row
	private Point2D storage_position;

	private String BORDER_PATH = "/border.png";

	private Terrain terrain;
	private Unit unit;

	private Point2D hex_center;

	private double side_size;
	private double border_width;

	private double hex_width;
	private double hex_height;

	private List<Point2D> corner_points;

	// constructors

	public Hexagon(Field model) {

		this.storage_position = model.getStorage_position();

		this.terrain = new Terrain(model.getTerrain());
		this.unit = new Unit(model.getUnit());

		this.side_size = DrawFieldCommand.default_hex_size;
		this.border_width = DrawFieldCommand.default_hex_size;

		this.hex_center = this.calcRealPosition(this.storage_position);

		this.hex_width = this.calcHexWidth(this.side_size);
		this.hex_height = this.calcHexHeight(this.side_size);

		this.initCornerPoints();

	}

	public Hexagon(Field model, double side_size) {

		this(model);

		this.side_size = side_size;
		this.hex_center = this.calcRealPosition(this.storage_position);

		this.initCornerPoints();
	}

	public Hexagon(Field model, double side_size, double border_width) {

		this(model);

		this.side_size = side_size;
		this.border_width = border_width;
		this.hex_center = this.calcRealPosition(this.storage_position);

		this.initCornerPoints();
	}

	// may be just for testing ...
	public Hexagon(Point2D storage_position, double side_size) {

		// inverted
		this.storage_position = new Point2D(storage_position.getY(), storage_position.getX());
		// this.storage_position = storage_position;

		this.side_size = side_size;
		this.border_width = DrawFieldCommand.default_border_width;

		this.hex_center = this.calcRealPosition(this.storage_position);

		this.hex_width = this.calcHexWidth(this.side_size);
		this.hex_height = this.calcHexHeight(this.side_size);

		this.unit = new Unit(100, 100, 100);
		this.terrain = new Terrain();

		this.initCornerPoints();
	}

	// used for getNext
	public Hexagon(Point2D storage_center, Point2D hex_center, double side_size, Unit unit, Terrain terrain) {

		this.storage_position = storage_center;
		this.hex_center = hex_center;
		this.side_size = side_size;
		this.border_width = DrawFieldCommand.default_border_width;

		// calculate hex width and height based on the hex side size (length)
		this.hex_width = this.calcHexWidth(this.side_size);
		this.hex_height = this.calcHexHeight(this.side_size);

		this.unit = unit;
		this.terrain = terrain;

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
			x = (float) (this.hex_center.getX() - this.side_size * Math.cos(angle));
			y = (float) (this.hex_center.getY() - this.side_size * Math.sin(angle));

			this.getCorner_points().add(new Point2D(x, y));
			// System.out.println("Calculated corner: ( " + x + ", " + y + " )");

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
	public Point3D convertToCube(Point2D axial) {

		// axial.q -> x
		// axial.r -> y

		double x = axial.getX();
		double z = axial.getY();
		double y = -x - z;

		return new Point3D(x, y, z);

	}

	public Point2D convertToAxial(Point3D cube) {

		// axial.q -> x
		// axial.r -> y

		double x = cube.getX();
		double y = cube.getZ();

		return new Point2D(x, y);

	}

	public Point2D calcRealPosition(Point2D axial) {

		// work with this setup
		// x -> y
		double x = this.side_size * (Math.sqrt(3) * axial.getX() + Math.sqrt(3) / 2 * axial.getY());
		// y
		double y = this.side_size * (3.0 / 2 * axial.getY());

		return new Point2D(x, y);
	}

	private Point3D cube_round(Point3D cube) {

		System.out.println("got in round: " + cube);

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

	public Point2D calcStoragePosition(Point2D point) {

		// Point2D point = new Point2D(ppoint.getX() + this.side_size, ppoint.getY());

		// x - y
		// /3 /3
		double x = (Math.sqrt(3.0) / 3 * point.getX() - 1.0 / 3 * point.getY()) / this.side_size;
		// y
		double y = (2.0 / 3 * point.getY()) / this.side_size;

		System.out.println("\n--------");
		System.out.println("click on: " + point);
		System.out.println("in matrix: " + new Point2D((int) x, (int) y));

		Point2D position = this.convertToAxial(this.cube_round(this.convertToCube(new Point2D(x, y))));
		System.out.println("Position: " + position);

		System.out.println("--------");

		// return new Point2D(position.getX(), y);
		return position;
	}

	// drawing methods

	private void drawBorders(GraphicsContext gc) {
		gc.save();

		Image border_img = new Image(this.BORDER_PATH);

		double angle = 30;

		for (Point2D corner : this.getCorner_points()) {

			Transform transform = new Rotate(angle, corner.getX(), corner.getY());
			gc.setTransform(transform.getMxx(), transform.getMyx(), transform.getMxy(), transform.getMyy(),
					transform.getTx(), transform.getTy());

			gc.drawImage(border_img, corner.getX(), corner.getY(), this.side_size, this.border_width);

			angle += 60;
		}

		gc.restore();
	}

	private void drawTerrain(GraphicsContext gc) {

		this.terrain.drawTerrain(gc, this.hex_center, this.side_size);
	}

	private void drawUnit(GraphicsContext gc) {

		this.unit.drawUnit(gc, this.hex_center, this.side_size);
	}

	// DrawableHexagon

	public void drawOn(Canvas canvas) {

		GraphicsContext gc = canvas.getGraphicsContext2D();

		this.drawBorders(gc);
		this.drawTerrain(gc);
		this.drawUnit(gc);

	}

	public void paintHex(GraphicsContext gc, Color color) {
		gc.save();

		double[] xs = new double[6];
		double[] ys = new double[6];

		for (int i = 0; i < 6; i++) {
			xs[i] = this.getCorner_points().get(i).getX();
			ys[i] = this.getCorner_points().get(i).getY();
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
			xs[i] = this.getCorner_points().get(i).getX();
			ys[i] = this.getCorner_points().get(i).getY();
		}

		gc.setFill(Color.GRAY);
		gc.fillPolygon(xs, ys, 6);

		gc.restore();
	}

	// get same hex on different position

	public Hexagon getNextOnX() {

		int next_x = (int) this.storage_position.getX();
		int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.hex_center.getX() + 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) this.hex_center.getY();

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size, this.unit,
				this.terrain);

	}

	public Hexagon getPrevtOnX() {

		int next_x = (int) this.storage_position.getX();
		int next_y = (int) (this.storage_position.getY() - 1);

		int next_cx = (int) (this.hex_center.getX() - 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) this.hex_center.getY();

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size, this.unit,
				this.terrain);

	}

	public Hexagon getNextOnY() {

		int next_x = (int) (this.storage_position.getX() + 1);
		int next_y = (int) this.storage_position.getY();

		int next_cx = (int) (this.hex_center.getX() + (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() + this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size, this.unit,
				this.terrain);
	}

	public Hexagon getPrevOnY() {

		int next_x = (int) (this.storage_position.getX() - 1);
		int next_y = (int) this.storage_position.getY();

		int next_cx = (int) (this.hex_center.getX() - (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() - this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size, this.unit,
				this.terrain);
	}

	public Hexagon getNextOnXY() {

		int next_x = (int) this.storage_position.getX() + 1;
		int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.hex_center.getX() + (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() - this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size, this.unit,
				this.terrain);

	}

	public Hexagon getPrevOnXY() {

		int next_x = (int) this.storage_position.getX() + 1;
		int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.hex_center.getX() - (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() + this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size, this.unit,
				this.terrain);

	}

	// getters and setters

	public List<Point2D> getCorner_points() {
		return corner_points;
	}

	public double getHex_width() {
		return hex_width;
	}

	public double getHex_height() {
		return hex_height;
	}

	public double getBorder_width() {
		return border_width;
	}

	public void setBorder_width(double border_width) {
		this.border_width = border_width;
	}
}
