namespace RabbitGameServer.SharedModel
{
	public class Point2D
	{
		public double x { get; set; }

		public double y { get; set; }

		public Point2D()
		{
		}

		public Point2D(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

		public override int GetHashCode()
		{
			return int.Parse($"{x}{y}");
		}

		public override bool Equals(object? obj)
		{
			return (obj != null) && (obj is Point2D)
				&& (((Point2D)obj).x == this.x)
				&& (((Point2D)obj).y == this.y);
		}


	}
}