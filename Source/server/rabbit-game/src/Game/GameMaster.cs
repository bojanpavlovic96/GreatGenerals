using RabbitGameServer.Client;
using RabbitGameServer.Config;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Game
{
	public delegate void GameDoneHandler(GameMaster gameMaster);

	public delegate Message ModelEventHandler(ModelEvent e);

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

		private List<Color> availableColors;

		private Dictionary<ModelEventType, ModelEventHandler> handlers;

		public GameMaster(string roomName,
					string password,
					PlayerData masterPlayer,
					GameConfig config,
					IPlayerProxy playerProxy,
					Database.IDatabase db,
					GameDoneHandler onGameDone)
		{
			this.config = config;
			parseColors();

			this.RoomName = roomName;
			this.Password = password;

			this.masterPlayer = masterPlayer;
			this.masterPlayer.color = getAvailableColor();
			this.masterPlayer.points = config.DefaultPoints;
			this.Players = new List<PlayerData>();
			this.Players.Add(masterPlayer);

			this.recEventsCnt = 0;
			this.sendEventsCnt = 0;

			this.Database = db;

			this.onGameDone += onGameDone;

			this.Fields = new Dictionary<Point2D, Field>();

			this.initHandlers();
		}

		private void initHandlers()
		{
			handlers = new Dictionary<ModelEventType, ModelEventHandler>();
			handlers.Add(ModelEventType.ReadyForInitEvent, handleInitRequest);
			handlers.Add(ModelEventType.MoveModelEvent, handleMoveEvent);
			handlers.Add(ModelEventType.AttackModelEvent, handleAttackEvent);
		}

		public Message AddModelEvent(ModelEvent newEvent)
		{
			recEventsCnt++;

			ModelEventHandler? handler;
			if (handlers.TryGetValue(newEvent.type, out handler))
			{
				Console.WriteLine($"Handler for {newEvent.type.ToString()} found ...  ");
				return handler.Invoke(newEvent);
			}
			else
			{
				Console.WriteLine($"Handlers for {newEvent.type.ToString()} is missing ... ");

				return null;
			}

		}

		// region separate event handlers

		private Message handleInitRequest(ModelEvent e)
		{
			Console.WriteLine("Handling ready for init request ... ");
			Console.WriteLine($"AvailableUnits: {config.Units.Count}");
			Console.WriteLine($"AvailableMoves: {config.Moves.Count}");

			return new InitializeMessage(RoomName,
				e.playerName,
				Players,
				config.Moves,
				config.Units,
				config.Attacks,
				Fields.Values.ToList());
		}

		private Message handleMoveEvent(ModelEvent e)
		{
			Console.WriteLine("Handling move event ... ");
			var mev = (MoveModelEvent)e;

			var endField = getField(mev.destinationField);
			if (endField != null)
			{
				if (!isOccupied(endField))
				{
					Console.WriteLine("Unit is free to move ... ");

					var srcField = getField(mev.sourceField);
					endField.unit = srcField.unit;
					srcField.unit = null;

					return new MoveMessage(mev.playerName,
						RoomName,
						mev.sourceField,
						mev.destinationField);
				}
				else
				{
					Console.WriteLine("Unit should recalculate path ... ");
					return new RecalculatePathMessage(mev.playerName,
						RoomName,
						mev.sourceField);
				}
			}
			else
			{
				Console.WriteLine("Move should be aborted ... ");
				return new AbortMoveMessage(mev.playerName,
					RoomName,
					mev.sourceField);
			}


			// return new ServerErrorMessage(mev.playerName, RoomName,
			// 	$"Unknown error occured while handling {e.type.ToString()}");
		}

		private Field getField(Point2D point)
		{
			Field? field;
			Fields.TryGetValue(point, out field);
			return field;
		}

		private bool isOccupied(Field field)
		{
			return (field.unit != null);
		}

		private Message handleAttackEvent(ModelEvent e)
		{
			return new ServerErrorMessage(e.playerName, RoomName,
				"Attack event handler still not implemented ... "); ;
		}

		// endregion 

		public bool initGame()
		{

			int left = 3;
			int right = 17;

			int segLen = (right - left) / Players.Count;

			for (int i = 1; i < 16; i++)
			{

				for (int j = left; j < right; j++)
				{

					var position = new Point2D(j, i);
					var terrain = new Terrain(TerrainType.mountains, 1);
					if (i % 2 == 0 && j % 5 == 0)
					{
						terrain = new Terrain(TerrainType.water, 1);
					}

					var playerInd = j / segLen;

					if (playerInd >= Players.Count)
					{
						playerInd = Players.Count - 1;
					}
					else if (playerInd < 0)
					{
						playerInd = 0;
					}

					var playerName = Players[playerInd].username;

					var newField = new Field(position,
						true,
						null,
						terrain,
						playerName,
						false);

					if (config.DefaultPositions.Contains(position))
					{
						newField.unit = UnitType.basicunit;
					}

					Fields.Add(newField.position, newField);

				}

				if (left > -3)
					left--;
			}

			return true;
		}

		public bool hasPlayer(string name)
		{
			return this.Players.Any<PlayerData>((player) => player.username == name);
		}

		public bool isReady()
		{
			return Players.Count >= config.MinimumPlayers;
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

					availableColors.Add(player.color);

					return player;
				}
			}

			return null;
		}

		public GameSummary getSummary()
		{

			return new GameSummary(RoomName, 
				masterPlayer.username,
				Players.Count,
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

		private void parseColors()
		{

			availableColors = new List<Color>();

			foreach (var colorStr in config.Colors)
			{
				availableColors.Add(Color.parse(colorStr));
			}

		}

		private Color getAvailableColor()
		{
			if (availableColors.Count > 0)
			{
				var value = availableColors[0];
				availableColors.RemoveAt(0);
				return value;
			}

			Console.WriteLine("No more available colors, returned default color (RED ... ");
			return Color.RED;
		}

	}
}