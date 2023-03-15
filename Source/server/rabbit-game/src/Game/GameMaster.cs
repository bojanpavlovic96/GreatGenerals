using System.Timers;
using RabbitGameServer.Client;
using RabbitGameServer.Config;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Game
{
	public delegate void GameDoneHandler(GameMaster gameMaster);

	public delegate void IncomeTickHandler(int amount, string room, string player);

	public delegate Message ModelEventHandler(ClientIntention e);

	class IncomeStats
	{
		public string Name { get; set; }

		public int TickCount { get; set; }
		public int Required { get; set; }

		public int CurrentAmount { get; set; }
		public int Income { get; set; }

		public IncomeStats(string name, int startAmount, int required, int income)
		{
			Name = name;
			Required = required;
			Income = income;

			TickCount = 0;
			CurrentAmount = startAmount;
		}
	}

	public class GameMaster
	{
		public GameConfig config;

		public PlayerData masterPlayer { get; set; }

		public string RoomName { get; set; }
		public string Password { get; set; }
		public List<PlayerData> Players { get; set; }
		private List<IncomeStats> Incomes;
		private List<Unit> ActiveUnits;

		private Database.IDatabase Database;

		private Dictionary<Point2D, Field> Fields;

		private int recEventsCnt;
		private int sendEventsCnt;

		public event GameDoneHandler onGameDone;

		public event IncomeTickHandler onIncomeTick;

		private List<Color> availableColors;

		private System.Timers.Timer tickTimer;

		private Dictionary<ClientIntentionType, ModelEventHandler> handlers;

		public GameMaster(string roomName,
					string password,
					PlayerData masterPlayer,
					GameConfig config,
					IPlayerProxy playerProxy,
					Database.IDatabase db,
					GameDoneHandler onGameDone,
					IncomeTickHandler incomeTickHandler)
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

			this.ActiveUnits = new List<Unit>();

			this.recEventsCnt = 0;
			this.sendEventsCnt = 0;

			this.Database = db;

			this.onGameDone += onGameDone;
			this.onIncomeTick += incomeTickHandler;

			this.Fields = new Dictionary<Point2D, Field>();

			this.initHandlers();
		}

		private void initHandlers()
		{
			handlers = new Dictionary<ClientIntentionType, ModelEventHandler>();
			handlers.Add(ClientIntentionType.ReadyForInit, handleInitRequest);
			handlers.Add(ClientIntentionType.Move, handleMove);
			handlers.Add(ClientIntentionType.Attack, handleAttack);
			handlers.Add(ClientIntentionType.Defend, handleDefend);
			handlers.Add(ClientIntentionType.AbortAttack, handleAbortAttack);
			handlers.Add(ClientIntentionType.BuildUnit, handleBuildUnit);

		}

		public Message? AddIntention(ClientIntention intention)
		{
			Console.WriteLine($"Handling: {intention.GetType().ToString()} ... ");
			recEventsCnt++;

			ModelEventHandler? handler;
			if (handlers.TryGetValue(intention.type, out handler))
			{
				return handler.Invoke(intention);
			}
			else
			{
				Console.WriteLine($"Handlers for {intention.type.ToString()} is missing ... ");

				return null;
			}

		}

		// region separate event handlers

		private Message handleInitRequest(ClientIntention e)
		{
			Console.WriteLine("Handling ready for init request ... ");
			Console.WriteLine($"AvailableUnits: {config.Units.Count}");
			Console.WriteLine($"AvailableMoves: {config.Moves.Count}");
			Console.WriteLine($"AvailableAttacks: {config.Attacks.Count}");

			return new InitializeMessage(RoomName,
				e.playerName,
				Players,
				config.Moves,
				config.Units,
				config.Attacks,
				Fields.Values.ToList());
		}

		private Message handleMove(ClientIntention e)
		{
			Console.WriteLine("Handling move event ... ");
			var mev = (MoveIntention)e;

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
					// QUESTIONABLE 
					// Should I just stop unit so it can actually be a feature, 
					// intercepting units in order to stop them ... ? 
					// Or should i send RecalculatePath message ... ? 
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
			// 	$"Unknown error ocurred while handling {e.type.ToString()}");
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

		private Message handleAttack(ClientIntention e)
		{
			var attackIntention = (AttackIntention)e;

			var attackerField = getField(attackIntention.sourceField);
			var targetField = getField(attackIntention.destinationField);

			if (attackerField.unit == null || targetField.unit == null)
			{
				Console.WriteLine("Attacker or defender do not exist on this fields ... ");

				return new AbortAttackMessage(e.playerName,
					RoomName,
					((AttackIntention)e).sourceField);
			}

			var attack = getAttack(attackIntention.attackType);

			// Ignore the range check since distance is calculated based on the 
			// fieldManager implementation which is at this point still 
			// bound to the client side.

			targetField.unit.health -= attack.attackDmg;

			if (targetField.unit.health <= 0)
			{
				// var owner = targetField.unit.owner;

				ActiveUnits.Remove(targetField.unit);

				targetField.unit = null;
				targetField.inBattle = false;

				if (checkEndGame())
				{
					Console.WriteLine("\n This game is done ... \n");
					onGameDone(this);

					return new GameDoneMessage(e.playerName,
						RoomName,
						config.winAward);
				}

			}

			return new AttackMessage(e.playerName,
				RoomName,
				attack.type.ToString(),
				attackIntention.sourceField,
				attackIntention.destinationField);
		}

		private bool checkEndGame()
		{
			int playersAlive = 0;
			foreach (var player in Players)
			{
				var unitCnt = ActiveUnits.Where(u => u.owner == player.username).Count();
				playersAlive += unitCnt > 0 ? 1 : 0;
			}

			Console.WriteLine($"Still has alive players: {playersAlive > 1}");
			return playersAlive <= 1;
		}

		private bool isDead(String player)
		{
			return Fields.Values.First(f => f.unit != null && f.unit.owner == player) == null;
		}

		private Message handleDefend(ClientIntention e)
		{
			var defendEvent = (DefendIntention)e;

			var attackedField = getField(defendEvent.sourceField);
			var attackerField = getField(defendEvent.destinationField);

			var defense = getAttack(defendEvent.defenseType);

			if (attackedField.unit == null || attackerField.unit == null)
			{
				Console.WriteLine("Defender or attacked do not exist on this fields ... ");

				return null;
			}

			attackerField.unit.health -= defense.defenseDmg;

			if (attackerField.unit.health <= 0)
			{
				attackerField.unit = null;
				attackerField.inBattle = false;
			}

			// return something else if unit dead or something ... 

			return new DefendMessage(e.playerName,
				RoomName,
				defendEvent.defenseType.ToString(),
				defendEvent.sourceField,
				defendEvent.destinationField);
		}

		private Message handleAbortAttack(ClientIntention e)
		{
			var abortIntention = (AbortAttackIntention)e;

			var attackerField = getField(abortIntention.position);

			if (attackerField.unit == null)
			{
				Console.WriteLine("Failed to abort attack ... ");
				return null;
			}

			return new AbortAttackMessage(abortIntention.playerName,
				RoomName,
				abortIntention.position);
		}

		private Attack getAttack(AttackType wantedType)
		{
			foreach (var att in config.Attacks)
			{
				if (att.type == wantedType)
				{
					return att;
				}
			}

			return null;
		}

		private Message handleBuildUnit(ClientIntention e)
		{
			var buildInt = (BuildUnitIntention)e;

			var stats = Incomes.Find(p => p.Name == e.playerName);
			var unitDesc = getUnit(Enum.Parse<UnitType>(buildInt.unitType));

			var field = getField(buildInt.field);

			if (field.unit == null && stats.CurrentAmount >= unitDesc.cost)
			{
				stats.CurrentAmount -= unitDesc.cost;

				var newUnit = unitDesc.copy();
				newUnit.owner = buildInt.playerName;

				field.unit = newUnit;

				ActiveUnits.Add(newUnit);

				return new BuildUnitMessage(e.playerName,
						RoomName,
						buildInt.field,
						buildInt.unitType,
						unitDesc.cost);
			}

			return null;
		}

		private Unit getUnit(UnitType type)
		{
			return config.Units.Find(u => u.unitName == type);
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
					Terrain? terrain = null;
					if (i % 2 == 0 && j % 5 == 0)
					{
						terrain = new Terrain(TerrainType.water, 1);
					}
					else
					{
						terrain = new Terrain(TerrainType.mountains, 1);
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
						var newUnit = generateUnit(UnitType.basicunit);
						newUnit.owner = playerName;

						ActiveUnits.Add(newUnit);

						newField.unit = newUnit;
					}

					Fields.Add(newField.position, newField);

				}

				if (left > -3)
					left--;
			}

			this.Incomes = new List<IncomeStats>();
			foreach (var player in Players)
			{
				Console.WriteLine($"Created income for: {player.username}");
				Incomes.Add(new IncomeStats(player.username,
											player.points,
											config.requiredIncomeTicks,
											config.incomeAmount));
			}

			tickTimer = new System.Timers.Timer();
			tickTimer.Elapsed += this.tickHandler;
			tickTimer.Interval = config.tickTime;
			tickTimer.AutoReset = false;
			tickTimer.Start();


			return true;
		}

		private void tickHandler(Object? source, ElapsedEventArgs args)
		{
			tickTimer.Stop();

			foreach (var stat in Incomes)
			{
				stat.TickCount += 1;
				if (stat.TickCount >= stat.Required)
				{
					stat.TickCount = 0;
					stat.CurrentAmount += stat.Income;

					onIncomeTick.Invoke(stat.CurrentAmount, RoomName, stat.Name);
				}
			}

			tickTimer.Start();
		}

		public Unit? generateUnit(UnitType unitType)
		{
			foreach (var unit in config.Units)
			{
				if (unit.unitName == unitType)
				{
					return unit.copy();
				}
			}

			return null;
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
			player.points = config.DefaultPoints;
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
			if (tickTimer != null)
			{
				tickTimer.Stop();
			}

			Console.WriteLine($"{RoomName} is stopped ... ");
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
