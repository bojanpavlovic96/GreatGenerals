using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Client;
using RabbitGameServer.Config;
using RabbitGameServer.Database;
using RabbitGameServer.Mediator;
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

		private IMediator mediator;

		public GamePool(IDatabase database,
			IPlayerProxy playerProxy,
			IOptions<GameConfig> config,
			IMediator mediator)
		{
			Console.WriteLine("Creating gamePool ... ");

			this.id = (int)(new Random()).NextInt64();
			this.startedDate = DateTime.Now;

			this.database = database;
			this.playerProxy = playerProxy;
			this.config = config.Value;
			this.mediator = mediator;

			games = new Dictionary<string, GameMaster>();
		}

		public GameMaster CreateGame(string roomName,
							string password,
							SharedModel.PlayerData master)
		{
			var newGame = new LiveMaster(
				roomName,
				password,
				master,
				config,
				playerProxy,
				database,
				GameDoneHandler
				// IncomeTickHandler
				);

			games.Add(roomName, newGame);

			return newGame;
		}

		private void GameDoneHandler(GameMaster gameMaster)
		{
			Console.WriteLine($"Rome {gameMaster.GetRoomName()} is done ...");
			games.Remove(gameMaster.GetRoomName());

			foreach (var player in gameMaster.GetPlayers())
			{
				var updateReq = new UpdatePlayerRequest(player);

			}

		}

		// private void IncomeTickHandler(int amount, string room, string player)
		// {
		// 	var message = new IncomeTickMessage(DateTime.Now, player, room, amount);
		// 	playerProxy.sendMessage(room, player, message);
		// }

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
				playersCount += game.GetPlayers().Count;
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
				summaries.Add(game.GetSummary());
			}

			return summaries;
		}

		public GameSummary GetGameSummary(string room)
		{

			GameMaster? game;
			games.TryGetValue(room, out game);
			if (game != null)
			{
				return game.GetSummary();
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
				game.EndGame();
				games.Remove(roomName);

				return true;
			}
			else
			{
				return false;
			}

		}

		public GameMaster LoadReplay(string roomId)
		{
			GameMaster master = new ReplayMaster(roomId, database, playerProxy);
			games.Add(roomId, master);
			return master;
		}
	}
}