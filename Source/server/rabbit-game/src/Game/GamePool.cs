using Microsoft.Extensions.Options;
using RabbitGameServer.Client;
using RabbitGameServer.Config;
using RabbitGameServer.Database;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;

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

		private GameConfig config;

		public GamePool(IDatabase database,
				IPlayerProxy playerProxy,
				IOptions<GameConfig> config)
		{
			Console.WriteLine("Creating gamePool ... ");

			this.id = (int)(new Random()).NextInt64();
			this.startedDate = DateTime.Now;

			this.database = database;
			this.playerProxy = playerProxy;
			this.config = config.Value;

			games = new Dictionary<string, GameMaster>();

		}

		public GameMaster CreateGame(string roomName,
							string password,
							PlayerData master)
		{
			var newGame = new GameMaster(
				roomName,
				password,
				master,
				config,
				playerProxy,
				database,
				GameDoneHandler,
				IncomeTickHandler);

			games.Add(roomName, newGame);

			return newGame;
		}

		private void GameDoneHandler(GameMaster gameMaster)
		{
			Console.WriteLine($"Rome {gameMaster.RoomName} is done ...");
			games.Remove(gameMaster.RoomName);
		}

		private void IncomeTickHandler(int amount, string room, string player)
		{
			var message = new IncomeTickMessage(player, room, amount);
			playerProxy.sendMessage(room, player, message);
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

		public bool destroyGame(string roomName)
		{
			// maybe force it to write everything to the database
			GameMaster game;
			if (games.TryGetValue(roomName, out game))
			{
				game.endGame();
				games.Remove(roomName);

				return true;
			}
			else
			{
				return false;
			}

		}
	}
}