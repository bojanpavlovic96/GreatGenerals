package fields.draw;

import java.util.ArrayList;
import java.util.List;

import fields.region.HexagonRegion;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class Hexagon {

	private Point2D storage_position;

	private String BORDER_PATH = "/border.png";

	private Terrain terrain;
	private Unit unit;

	private Point2D hex_center;
	private double side_size;
	private double hex_width;
	private double hex_height;

	private List<Point2D> corner_points;

	public Hexagon(ModelHexagon model) {

	}

	public Hexagon(Point2D storage_center, Point2D hex_center, double side_size) {

		this.storage_position = storage_center;
		this.hex_center = hex_center;

		this.side_size = side_size;
		// calculate hex width and height based on the hex side size (length)
		this.hex_width = (float) (Math.sqrt(3) * this.side_size);
		this.hex_height = 2 * this.side_size;

		// invalid

		this.unit = new Unit(100, 100, 100);
		this.terrain = new Terrain();

		this.initCornerPoints();

	}

	public Hexagon(Point2D hex_center, double side_size) {

		this.hex_center = hex_center;

		this.side_size = side_size;
		// calculate hex width and height based on the hex side size (length)
		this.hex_width = (float) (Math.sqrt(3) * this.side_size);
		this.hex_height = 2 * this.side_size;

		this.initCornerPoints();

	}

	private void initCornerPoints() {

		this.corner_points = new ArrayList<Point2D>();

		System.out.println("HexCenter: " + this.hex_center);

		// starting point is on top
		float angle = (float) (Math.PI / 2);
		float x = 0;
		float y = 0;
		// calculate coordinates of all 6 corners starting from the one on top
		for (int i = 0; i < 6; i++) {

			// may be + for x an - for y, not sure, but it works with this
			x = (float) (this.hex_center.getX() - this.side_size * Math.cos(angle));
			y = (float) (this.hex_center.getY() - this.side_size * Math.sin(angle));

			this.corner_points.add(new Point2D(x, y));
			System.out.println("Calculated corner: ( " + x + ", " + y + " )");

			// add 60 degree
			angle += Math.PI / 3;

		}

	}

	public void drawOn(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		this.drawBorders(gc);
		this.drawTerrain(gc);
		this.drawUnit(gc);

	}

	private void drawBorders(GraphicsContext gc) {
		gc.save();

		Image border_img = new Image(this.BORDER_PATH);

		double angle = 30;

		for (Point2D corner : this.corner_points) {

			Transform transform = new Rotate(angle, corner.getX(), corner.getY());
			gc.setTransform(transform.getMxx(), transform.getMyx(), transform.getMxy(), transform.getMyy(),
					transform.getTx(), transform.getTy());

			gc.drawImage(border_img, corner.getX(), corner.getY(), this.side_size, border_img.getHeight());

			angle += 60;
		}

		gc.restore();
	}

	private void drawTerrain(GraphicsContext gc) {
		this.terrain.drawTerrain(gc, hex_center, this.side_size);
	}

	private void drawUnit(GraphicsContext gc) {
		this.unit.drawUnit(gc, this.hex_center, this.side_size);
	}

	public void clearHex(GraphicsContext gc) {
		gc.save();

		double[] xs = new double[6];
		double[] ys = new double[6];
		for (int i = 0; i < 6; i++) {
			xs[i] = this.corner_points.get(i).getX();
			ys[i] = this.corner_points.get(i).getY();
		}

		gc.setFill(Color.BLACK);
		gc.fillPolygon(xs, ys, 6);

		gc.restore();
	}

	public Hexagon getNextOnX() {

		int next_x = (int) this.storage_position.getX();
		int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.hex_center.getX() + 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) this.hex_center.getY();

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size);

	}

	public Hexagon getPrevtOnX() {

		int next_x = (int) this.storage_position.getX();
		int next_y = (int) (this.storage_position.getY() - 1);

		int next_cx = (int) (this.hex_center.getX() - 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) this.hex_center.getY();

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size);

	}

	public Hexagon getNextOnY() {

		int next_x = (int) (this.storage_position.getX() + 1);
		int next_y = (int) this.storage_position.getY();

		int next_cx = (int) (this.hex_center.getX() + (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() + this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size);
	}

	public Hexagon getPrevOnY() {

		int next_x = (int) (this.storage_position.getX() - 1);
		int next_y = (int) this.storage_position.getY();

		int next_cx = (int) (this.hex_center.getX() - (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() - this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size);
	}

	public Hexagon getNextOnXY() {

		int next_x = (int) this.storage_position.getX() + 1;
		int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.hex_center.getX() + (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() - this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size);

	}

	public Hexagon getPrevOnXY() {

		int next_x = (int) this.storage_position.getX() + 1;
		int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.hex_center.getX() - (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.hex_center.getY() + this.side_size * 1.5);

		return new Hexagon(new Point2D(next_x, next_y), new Point2D(next_cx, next_cy), this.side_size);

	}

	// todo implement...
	public Point2D calculatePositionFromStorage(Point2D storage_position) {
		return null;
	}

	// todo implement
	public Point2D calculateStoragePositionFromReal(Point2D real_position) {
		return null;
	}

}
