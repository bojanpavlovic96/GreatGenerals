namespace RabbitGameServer.SharedModel
{
	public class Color
	{
		public static Color RED = new Color(10, 0, 0, 1);
		public static Color GREEN = new Color(0, 10, 0, 1);
		public static Color BLUE = new Color(0, 0, 10, 1);

		public double red { get; set; }
		public double green { get; set; }
		public double blue { get; set; }

		public double opacity { get; set; }

		public Color(double red, double green, double blue, double opacity)
		{
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.opacity = opacity;
		}

	}
}