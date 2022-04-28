using RabbitGameServer.Client;
using RabbitGameServer.Database;

namespace RabbitGameServer.Game
{
	public class GamePool : IGamePool
	{
		public int GamesCount { get; set; }

		// TODO this should probably be concurrent dict. 
		public Dictionary<string, GameMaster> games;

		private IDatabase database;

		private IPlayerProxy playerProxy;

		public GamePool(IDatabase database, IPlayerProxy playerProxy)
		{
			Console.WriteLine("Creating gamePool ... ");
			this.database = database;
			this.playerProxy = playerProxy;

			GamesCount = 0;

			games = new Dictionary<string, GameMaster>();

		}

		public GameMaster CreateGame(string roomName, string masterPlayer)
		{
			var newGame = new GameMaster(
				roomName,
				masterPlayer,
				playerProxy,
				database);

			games.Add(roomName, newGame);

			newGame.onGameDone += GameDoneHandler;

			GamesCount++;

			return newGame;
		}

		private void GameDoneHandler(GameMaster gameMaster)
		{

			Console.WriteLine($"Game in a room {gameMaster.RoomName} is finally done ... ");
			games.Remove(gameMaster.RoomName);

			GamesCount--;
		}

		public GameMaster GetGame(string roomName)
		{
			GameMaster game;
			games.TryGetValue(roomName, out game);
			return game;
		}

		public BasicStats GetBasicPoolStats()
		{
			BasicStats stats = new BasicStats();

			foreach (var game in games.Values)
			{
				stats.Add(game.Stats);
			}

			return stats;
		}

	}
}