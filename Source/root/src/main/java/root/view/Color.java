package root.view;

public class Color {

	// these constants are taken from the JavaFX Color class
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GRAY = new Color(0.5019608f, 0.5019608f, 0.5019608f);
	public static final Color GREEN = new Color(0.0f, 0.5019608f, 0.0f);
	public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);

	public double red;
	public double green;
	public double blue;

	public double opacity;

	public Color() {

	}

	public Color(double red, double blue, double green) {
		this.red = red;
		this.green = green;
		this.blue = blue;

		this.opacity = 1;
	}

	public Color(double red, double blue, double green, double opacity) {
		this.red = red;
		this.green = green;
		this.blue = blue;

		this.opacity = opacity;
	}

	public javafx.scene.paint.Color asFxColor() {
		return new javafx.scene.paint.Color(this.red,
				this.green,
				this.blue,
				this.opacity);
	}

	public double getRed() {
		return red;
	}

	public double getBlue() {
		return blue;
	}

	public double getGreen() {
		return green;
	}

	public static Color rgb(double red, double green, double blue) {
		return new Color(red, green, blue, 1.0);
	}

	public static Color rgb(double red, double green, double blue, double opacity) {
		return new Color(red, green, blue, opacity);
	}

}
