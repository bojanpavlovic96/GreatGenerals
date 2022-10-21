namespace RabbitGameServer.SharedModel
{
	public class Point2D
	{
		public double x { get; set; }

		public double y { get; set; }

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