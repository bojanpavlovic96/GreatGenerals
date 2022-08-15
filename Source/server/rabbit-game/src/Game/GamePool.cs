using RabbitGameServer.Client;
using RabbitGameServer.Database;

namespace RabbitGameServer.Game
{
	public class GamePool : IGamePool
	{

		// at this point just a random generated number
		public int id;

		public DateTime startedDate;

		// TODO this should probably be concurrent dict. 
		public Dictionary<string, GameMaster> games;

		private IDatabase database;

		private IPlayerProxy playerProxy;

		public GamePool(IDatabase database, IPlayerProxy playerProxy)
		{
			Console.WriteLine("Creating gamePool ... ");

			this.id = (int)(new Random()).NextInt64();
			this.startedDate = DateTime.Now;

			this.database = database;
			this.playerProxy = playerProxy;

			games = new Dictionary<string, GameMaster>();

		}

		public GameMaster CreateGame(string roomName,
							string masterPlayer,
							string password)
		{
			var newGame = new GameMaster(
				roomName,
				password,
				masterPlayer,
				playerProxy,
				database,
				GameDoneHandler);

			games.Add(roomName, newGame);

			return newGame;
		}

		private void GameDoneHandler(GameMaster gameMaster)
		{

			Console.WriteLine($"Game in a room {gameMaster.RoomName} is finally done ... ");
			games.Remove(gameMaster.RoomName);

		}

		public GameMaster GetGame(string roomName)
		{
			GameMaster game;
			games.TryGetValue(roomName, out game);
			return game;
		}

		public PoolSummary GetPoolSummary()
		{
			var playersCount = 0;
			foreach (var game in this.games.Values)
			{
				playersCount += game.Players.Count;
			}

			return new PoolSummary(this.id,
				this.startedDate.ToString(),
				this.games.Values.Count,
				playersCount);
		}

		public List<GameSummary> GetGameSummaries()
		{
			var summaries = new List<GameSummary>();
			foreach (var game in games.Values)
			{
				summaries.Add(game.getSummary());
			}

			return summaries;
		}

		public GameSummary GetGameSummary(string room)
		{

			GameMaster? game;
			games.TryGetValue(room, out game);
			if (game != null)
			{
				return game.getSummary();
			}
			else
			{
				return null;
			}

		}

	}
}