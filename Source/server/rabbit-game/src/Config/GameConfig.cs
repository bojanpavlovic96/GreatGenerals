using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Config
{
	public class GameConfig
	{

		public static string ConfigSection = "GameConfig";

		public List<string> Colors { get; set; }
		public int DefaultPoints { get; set; }
		public int DefaultMapSizeX { get; set; }
		public int DefaultMapSizeY { get; set; }
		public List<Point2D> DefaultPositions { get; set; }

		public int MinimumPlayers { get; set; }
		public int MaximumPlayers { get; set; }

		public List<Move> Moves { get; set; }
		public List<Unit> Units { get; set; }
		public List<Attack> Attacks { get; set; }

	}
}