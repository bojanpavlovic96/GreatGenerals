namespace RabbitGameServer
{
	public class Point
	{
		public int x { get; set; }
		public int y { get; set; }
	}

	public class GameConfig
	{

		public static string ConfigSection = "GameConfig";

		public List<string> Colors;
		public int DefaultPoints;
		public int DefaultMapSizeX;
		public int DefaultMapSizeY;
		public List<Point> DefaultPositions;

	}
}