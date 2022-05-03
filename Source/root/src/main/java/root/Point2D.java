package root;

public class Point2D {

	public static Point2D ZERO = new Point2D(0, 0);

	public double x;
	public double y;

	// saw it in javafx implementation ... I guess I am gonna keep it 
	private int hash = 0;

	public Point2D() {
	}

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof Point2D) {
			Point2D other = (Point2D) obj;
			return getX() == other.getX() && getY() == other.getY();
		} else {
			return false;
		}
	}

	// taken from javafx implementation as well ... :)
	@Override
	public int hashCode() {
		if (hash == 0) {
			long bits = 7L;
			bits = 31L * bits + Double.doubleToLongBits(getX());
			bits = 31L * bits + Double.doubleToLongBits(getY());
			hash = (int) (bits ^ (bits >> 32));
		}
		return hash;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
