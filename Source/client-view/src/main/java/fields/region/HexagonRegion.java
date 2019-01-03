package fields.region;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class HexagonRegion extends Region {

	private Point2D storage_position;

	private static String BORDER_PATH = "/border.png";
	private float BORDER_WIDTH = (float) 5;

	private float side_size;
	private float hex_width;
	private float hex_height;
	private Point2D hex_center;

	// still not implemented
	private Terrain terrain;

	// still not implemented
	private Unit unit;

	private List<Point2D> corner_points;

	private List<ImageView> border_views;

	public HexagonRegion(float x_coordinate, float y_coordinate, float side_size) {

		// HexagonRegion position in parent container
		this.setLayoutX(x_coordinate);
		this.setLayoutY(y_coordinate);

		this.side_size = side_size;
		// calculate hex width and height based on the hex side size (length)
		this.hex_width = (float) (Math.sqrt(3) * this.side_size);
		this.hex_height = 2 * this.side_size;

		// center of the region
		this.hex_center = new Point2D(this.hex_width / 2, this.hex_height / 2);

		this.initCornerPoints();
		this.drawBorders();

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

			x = (float) (this.hex_center.getX() - this.side_size * Math.cos(angle));
			y = (float) (this.hex_center.getY() - this.side_size * Math.sin(angle));

			this.corner_points.add(new Point2D(x, y));
			System.out.println("Calculated corner: ( " + x + ", " + y + " )");

			// add 60 degrees
			angle += Math.PI / 3;

		}

	}

	private void drawBorders() {
		this.border_views = new ArrayList<ImageView>();

		Image border_image = new Image(HexagonRegion.BORDER_PATH);
		ImageView border_view = null;

		Transform rotation = null;

		// top-right border angle (clockwise rotation)
		float angle = 30;

		for (int i = 0; i < 6; i++) {

			// create border view from border_image
			border_view = new ImageView(border_image);

			// set position of top left corner relative to HexagonRegion
			border_view.setLayoutX(this.corner_points.get(i).getX());
			border_view.setLayoutY(this.corner_points.get(i).getY());

			// set dimension
			border_view.setFitWidth(this.side_size);
			border_view.setFitHeight(this.BORDER_WIDTH);

			// rotate for 'angle' around ( border_view.getFitHeight()/2 , 0 ) point
			// rotation point relative to imageView
			// |-------------------|
			// x <- rotation point | < BORDER
			// |-------------------|
			rotation = new Rotate(angle, border_view.getFitHeight() / 2, 0);
			angle += 60; // set angle value for next border

			// add rotation transformation to border_view
			border_view.getTransforms().add(rotation);

			this.border_views.add(border_view);
			this.getChildren().add(border_view);

		}

	}

	// to do: implement after terrain implementation
	private void drawTerrain() {

	}

	// to do: implement after unit implementation
	private void drawUnit() {

	}

	public HexagonRegion getNextOnX() {

		// todo uncomment

		// int next_x = (int) this.storage_position.getX();
		// int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.getLayoutX() + 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) this.getLayoutY();

		return new HexagonRegion(next_cx, next_cy, this.side_size);

	}

	public HexagonRegion getPrevOnX() {

		// todo uncomment

		// int next_x = (int) this.storage_position.getX();
		// int next_y = (int) (this.storage_position.getY() - 1);

		int next_cx = (int) (this.getLayoutX() - 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) this.getLayoutY();

		return new HexagonRegion(next_cx, next_cy, this.side_size);

	}

	public HexagonRegion getNextOnY() {

		// todo uncomment

		// int next_x = (int) (this.storage_position.getX() + 1);
		// int next_y = (int) this.storage_position.getY();

		int next_cx = (int) (this.getLayoutX() + (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.getLayoutY() + this.side_size * 1.5);

		return new HexagonRegion(next_cx, next_cy, this.side_size);
	}

	public HexagonRegion getPrevOnY() {

		// todo uncomment

		// int next_x = (int) (this.storage_position.getX() - 1);
		// int next_y = (int) this.storage_position.getY();

		int next_cx = (int) (this.getLayoutX() - (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.getLayoutY() - this.side_size * 1.5);

		return new HexagonRegion(next_cx, next_cy, this.side_size);
	}

	public HexagonRegion getNextOnXY() {

		// todo uncomment

		// int next_x = (int) this.storage_position.getX()+1;
		// int next_y = (int) (this.storage_position.getY() + 1);

		int next_cx = (int) (this.getLayoutX() + 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.getLayoutY() - this.side_size * 1.5);

		return new HexagonRegion(next_cx, next_cy, this.side_size);

	}

	public HexagonRegion getPrevOnXY() {

		// todo uncomment

		// int next_x = (int) this.storage_position.getX()-1;
		// int next_y = (int) (this.storage_position.getY() - 1);

		int next_cx = (int) (this.getLayoutX() - 2 * (this.side_size * Math.sin(Math.PI / 3)));
		int next_cy = (int) (this.getLayoutY() + this.side_size * 1.5);

		return new HexagonRegion(next_cx, next_cy, this.side_size);

	}

}
