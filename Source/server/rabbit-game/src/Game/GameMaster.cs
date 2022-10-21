using RabbitGameServer.Client;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Game
{
	public delegate void GameDoneHandler(GameMaster gameMaster);

	public class GameMaster
	{

		public GameConfig config;

		public PlayerData masterPlayer { get; set; }

		public string RoomName { get; set; }
		public string Password { get; set; }
		public List<PlayerData> Players { get; set; }
		private Database.IDatabase Database;

		private Dictionary<Point2D, Field> Fields;

		private int recEventsCnt;
		private int sendEventsCnt;

		public event GameDoneHandler onGameDone;

		public GameMaster(string roomName,
					string password,
					PlayerData masterPlayer,
					GameConfig config,
					IPlayerProxy playerProxy,
					Database.IDatabase db,
					GameDoneHandler onGameDone)
		{
			this.config = config;
			readColors();

			this.RoomName = roomName;
			this.Password = password;

			this.masterPlayer = masterPlayer;
			this.masterPlayer.color = getMasterColor();
			this.Players = new List<PlayerData>();
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

		public Message initGame()
		{
			// return new InitializeMessage(RoomName, Players, Fields.Values.ToList());
			return null;
		}

		public bool hasPlayer(string name)
		{
			return this.Players.Any<PlayerData>((player) => player.username == name);
		}

		public PlayerData addPlayer(PlayerData player)
		{
			player.color = getAvailableColor();
			Players.Add(player);

			return player;
		}

		public PlayerData removePlayer(string name)
		{
			for (int i = 0; i < Players.Count; i++)
			{
				if (Players[i].username == name)
				{
					var player = Players[i];
					Players.RemoveAt(i);

					return player;
				}
			}

			return null;
		}

		public GameSummary getSummary()
		{

			return new GameSummary(Players.Count,
				Players
					.Select<PlayerData, string>((player) => player.username)
					.ToList(),
				recEventsCnt,
				sendEventsCnt);
		}

		public bool isMaster(string player)
		{
			return masterPlayer.username == player;
		}

		public void endGame()
		{
			Console.WriteLine("Not really implemented but let's pretend it is done ... ");
			Console.WriteLine($"{RoomName} is dead ... ");
		}

		private void readColors()
		{
			// TODO implement
			Console.WriteLine("Reading colors from config in gameMaster NOT IMPLEMENTED ... ");
		}

		// TODO read this from gameConfig
		private Color getMasterColor()
		{
			return Color.RED;
		}

		// TODO as well read from gameConfig
		private Color getAvailableColor()
		{
			return Color.BLUE;
		}

	}
}