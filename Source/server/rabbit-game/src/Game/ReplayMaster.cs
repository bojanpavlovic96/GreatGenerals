
using RabbitGameServer.Client;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.ClientIntentions;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Game
{
	public class ReplayMaster : GameMaster
	{
		private string RoomId;

		private Database.IDatabase Db;
		private List<Message> Messages;

		private Database.DbGame details;

		private IPlayerProxy playerProxy;

		public ReplayMaster(string roomId, Database.IDatabase db, IPlayerProxy playerProxy)
		{
			this.RoomId = roomId;
			this.Db = db;
			this.playerProxy = playerProxy;

			this.Messages = new List<Message>();

			this.details = Db.getGame(roomId);
		}


		private List<Message> LoadMessages(string roomId)
		{
			Console.WriteLine("Loading messages ... ");
			try
			{
				return Db.getMessages(roomId)
					.Select<Database.Message, SharedModel.Messages.Message>(DbMessageMapper.map)
					.ToList();
			}
			catch (Exception e)
			{
				Console.WriteLine(e.Message);
				Console.WriteLine(e.StackTrace);
				return null;
			}

		}

		public async void AddIntention(ClientIntention intention)
		{
			Console.WriteLine("ReplayMaster handling intention: " + intention.type.ToString());
			if (intention.type == ClientIntentionType.ReadyForReplay)
			{
				Console.WriteLine("Handling readyForReplay ... ");

				Messages = LoadMessages(((ReadyForReplayIntention)intention).roomId);
				var initTime = details.startTime;

				var prevTime = initTime;
				foreach (var msg in Messages)
				{
					Console.WriteLine($"Delaying: {(msg.timestamp - prevTime).TotalMilliseconds}");
					await Task.Delay((int)(msg.timestamp - prevTime).TotalMilliseconds);

					playerProxy.sendMessage(RoomId, intention.playerName, msg);

					prevTime = msg.timestamp;
				}

			}
			else
			{
				Console.WriteLine("Unknown intention received in replay master ... ");
			}

		}

		public PlayerData AddPlayer(PlayerData player)
		{
			throw new NotImplementedException();
		}

		public void EndGame()
		{
			throw new NotImplementedException();
		}

		public PlayerData GetMasterPlayer()
		{
			return new PlayerData()
			{
				username = details.masterPlayer
			};
		}

		public string GetPassword()
		{
			throw new NotImplementedException();
		}

		public List<PlayerData> GetPlayers()
		{
			return details.players.Select((name) => new PlayerData() { username = name }).ToList();
		}

		public string GetRoomId()
		{
			return this.RoomId;
		}

		public string GetRoomName()
		{
			return details.roomName;
		}

		public GameSummary GetSummary()
		{
			return new GameSummary(RoomId,
				details.masterPlayer,
				details.players.Count,
				details.players,
				1,
				Messages.Count);

		}

		public bool HasPlayer(string name)
		{
			return details.players.Contains(name);
		}

		public bool InitGame()
		{
			throw new NotImplementedException();
		}

		public bool IsMaster(string player)
		{
			return details.masterPlayer == player;
		}

		public bool IsReady()
		{
			return Db != null && details != null;
		}

		public PlayerData RemovePlayer(string name)
		{
			throw new NotImplementedException();
		}
	}
}