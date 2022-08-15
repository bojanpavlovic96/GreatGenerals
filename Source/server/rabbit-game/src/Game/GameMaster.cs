using RabbitGameServer.Client;
using RabbitGameServer.Database;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Game
{
	public delegate void GameDoneHandler(GameMaster gameMaster);

	public class GameMaster
	{

		public string masterPlayer { get; set; }

		public string RoomName { get; set; }
		public string Password { get; set; }
		public List<string> Players { get; set; }
		private IDatabase Database;

		private int recEventsCnt;
		private int sendEventsCnt;

		public event GameDoneHandler onGameDone;

		public GameMaster(string roomName,
					string password,
					string masterPlayer,
					IPlayerProxy playerProxy,
					IDatabase db,
					GameDoneHandler onGameDone)
		{
			this.RoomName = roomName;
			this.Password = password;
			this.masterPlayer = masterPlayer;
			this.Players = new List<string>();
			this.Players.Add(masterPlayer);

			this.recEventsCnt = 0;
			this.sendEventsCnt = 0;

			this.Database = db;

			this.onGameDone += onGameDone;
		}

		public void AddModelEvent(ModelEvent newEvent)
		{
			recEventsCnt++;
			// based on the newEven.type/name
			// run predifined set of 'validators'
			// which are (based on the current state of the game)
			// gonna generate adequate command as a result

			// pass thiss command to the client proxy

		}

		public bool hasPlayer(string name)
		{
			return this.Players.Contains(name);
		}

		public void addPlayer(string name)
		{
			Players.Add(name);
		}

		public GameSummary getSummary()
		{
			return new GameSummary(Players.Count,
				recEventsCnt,
				sendEventsCnt);
		}

	}
}