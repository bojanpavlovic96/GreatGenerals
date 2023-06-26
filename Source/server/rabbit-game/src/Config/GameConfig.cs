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

		public int tickTime { get; set; }
		public int requiredIncomeTicks { get; set; }
		public int incomeAmount { get; set; }
		public int defaultCoins {get;set;}
		public int attackAward { get; set; }
		public int defendAward { get; set; }
		public int defeatCost { get; set; }
		public int defeatAward { get; set; }
		public int winAward { get; set; }

		public int msgQueueSize { get; set; }

		public GameConfig()
		{

		}

	}
}