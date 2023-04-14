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

	}
}