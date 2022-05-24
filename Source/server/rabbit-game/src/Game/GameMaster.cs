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
		public List<string> Players { get; set; }
		private IDatabase Database;

		public BasicStats Stats { get; set; }

		public event GameDoneHandler onGameDone;

		// public GameMaster() { }

		public GameMaster(string roomName,
					string masterPlayer,
					IPlayerProxy playerProxy,
					IDatabase db)
		{
			this.RoomName = roomName;
			this.masterPlayer = masterPlayer;
			this.Players = new List<string>();
			this.Players.Add(masterPlayer);

			this.Database = db;

			this.Stats = new BasicStats();
		}

		public void AddModelEvent(ModelEvent newEvent)
		{
			// based on the newEven.type/name
			// run predifined set of 'validators'
			// which are (based on the current state of the game)
			// gonna generate adequate command as a result

			// pass thiss command to the client proxy

		}



	}
}