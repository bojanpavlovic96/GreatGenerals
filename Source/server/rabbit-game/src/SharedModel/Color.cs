namespace RabbitGameServer.SharedModel
{
	public class Color
	{
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