namespace RabbitGameServer.Database
{
	public class Point2D
	{
		public double x;

		public double y;

		public static Point2D fromPoint(SharedModel.Point2D pos)
		{
			return new Point2D()
			{
				x = pos.x,
				y = pos.y
			};
		}

		public SharedModel.Point2D toPoint()
		{
			return new SharedModel.Point2D(x, y);
		}

		override public string ToString()
		{
			return "{x: " + x + " y: " + y + "}";
		}
	}
}