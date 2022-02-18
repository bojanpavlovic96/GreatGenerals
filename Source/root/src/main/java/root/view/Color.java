package root.view;

public class Color {

	public double red;
	public double green;
	public double blue;

	public double opacity;

	public Color() {

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
}
