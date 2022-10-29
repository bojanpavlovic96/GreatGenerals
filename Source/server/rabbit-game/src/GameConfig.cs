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

		public List<string> Colors { get; set; }
		public int DefaultPoints { get; set; }
		public int DefaultMapSizeX { get; set; }
		public int DefaultMapSizeY { get; set; }
		public List<Point> DefaultPositions { get; set; }

		public int MinimumPlayers { get; set; }
		public int MaximumPlayers { get; set; }

	}
}