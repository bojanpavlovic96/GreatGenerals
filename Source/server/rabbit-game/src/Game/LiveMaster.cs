using System.Timers;
using RabbitGameServer.Client;
using RabbitGameServer.Config;
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.SharedModel.ClientIntentions;

namespace RabbitGameServer.Game
{
	public delegate void GameDoneHandler(LiveMaster gameMaster);

	public delegate void IncomeTickHandler(int amount, string room, string player);

	public delegate Message IntentionHandler(ClientIntention e);

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

	public class LiveMaster : GameMaster
	{
		private GameConfig config;

		private PlayerData masterPlayer { get; set; }

		private string roomId { get; set; }

		private string RoomName { get; set; }
		private string Password { get; set; }
		private List<PlayerData> Players { get; set; }
		// private PlayerData winner { get; set; }
		private string winner { get; set; }
		private List<IncomeStats> Incomes;
		private List<Unit> ActiveUnits;

		private Database.IDatabase Database;
		private List<Message> Messages;

		private Dictionary<Point2D, Field> Fields;

		private int recEventsCnt;
		private int sendEventsCnt;

		private event GameDoneHandler onGameDone;

		// private event IncomeTickHandler onIncomeTick;

		private List<Color> availableColors;

		private System.Timers.Timer tickTimer;

		private Dictionary<ClientIntentionType, IntentionHandler> handlers;

		private IPlayerProxy playerProxy;

		public LiveMaster(string roomName,
					string password,
					PlayerData masterPlayer,
					GameConfig config,
					IPlayerProxy playerProxy,
					Database.IDatabase db,
					GameDoneHandler onGameDone
					// IncomeTickHandler incomeTickHandler
					)
		{
			this.config = config;
			parseColors();

			this.RoomName = roomName;
			this.Password = password;

			this.masterPlayer = masterPlayer;
			this.masterPlayer.color = getAvailableColor();
			// this.masterPlayer.points = config.DefaultPoints;
			this.Players = new List<PlayerData>();
			this.Players.Add(masterPlayer);
			this.winner = null;

			this.playerProxy = playerProxy;

			this.ActiveUnits = new List<Unit>();

			this.recEventsCnt = 0;
			this.sendEventsCnt = 0;

			this.Database = db;
			this.Messages = new List<Message>();

			this.onGameDone += onGameDone;
			// this.onIncomeTick += incomeTickHandler;

			this.Fields = new Dictionary<Point2D, Field>();
			this.initHandlers();
		}

		public LiveMaster(string roomId,
			GameConfig config,
			IPlayerProxy proxy,
			Database.IDatabase db,
			GameDoneHandler onGameDone)
		{

		}

		private void initHandlers()
		{
			handlers = new Dictionary<ClientIntentionType, IntentionHandler>();

			handlers.Add(ClientIntentionType.ReadyForInit, handleInitRequest);
			// handlers.Add(ClientIntentionType.ReadyForReplay, handleReplayRequest);
			handlers.Add(ClientIntentionType.Move, handleMove);
			handlers.Add(ClientIntentionType.Attack, handleAttack);
			handlers.Add(ClientIntentionType.Defend, handleDefend);
			handlers.Add(ClientIntentionType.AbortAttack, handleAbortAttack);
			handlers.Add(ClientIntentionType.BuildUnit, handleBuildUnit);

		}

		public void AddIntention(ClientIntention intention)
		{
			Console.WriteLine($"Handling: {intention.GetType().ToString()} ... ");
			recEventsCnt++;

			IntentionHandler? handler;
			if (handlers.TryGetValue(intention.type, out handler))
			{
				var resultMsg = handler.Invoke(intention);

				sendMessage(resultMsg, intention.playerName);

				// Messages.Add(resultMsg);
				// saveMessages();
				// playerProxy.sendMessage(RoomName, intention.playerName, resultMsg);
			}
			else
			{
				Console.WriteLine($"Handlers for {intention.type.ToString()} is missing ... ");
			}

		}

		private void sendMessage(Message message, string user)
		{
			Messages.Add(message);
			saveMessages();
			playerProxy.sendMessage(RoomName, user, message);
		}

		private void saveMessages(bool forced = false)
		{
			if (forced || Messages.Count >= config.msgQueueSize)
			{
				Console.WriteLine("Saving messages ... ");
				var dbMsgs = Messages.Select(m => DbMessageMapper.map(m, roomId)).ToList();
				try
				{
					Database.saveMessages(roomId, dbMsgs);
				}
				catch (Exception e)
				{
					Console.WriteLine("Exception in saving: ");
					Console.WriteLine($"{e.Message}");
				}
				Messages.Clear();
			}
		}

		// region separate event handlers

		private Message handleInitRequest(ClientIntention e)
		{
			Console.WriteLine("Handling ready for init request ... ");
			Console.WriteLine($"AvailableUnits: {config.Units.Count}");
			Console.WriteLine($"AvailableMoves: {config.Moves.Count}");
			Console.WriteLine($"AvailableAttacks: {config.Attacks.Count}");

			return new InitializeMessage(DateTime.Now,
				RoomName,
				e.playerName,
				Players,
				config.Moves,
				config.Units,
				config.Attacks,
				Fields.Values.ToList());
		}

		// private Message handleReplayRequest(ClientIntention e)
		// {
		// 	var intention = (ReadyForReplayIntention)e;
		// 	var messages = Database.getMessages(intention.roomId);

		// 	return new ReplayMessage(DateTime.Now, intention.playerName, "room");
		// }

		private Message handleMove(ClientIntention e)
		{
			Console.WriteLine("Handling move event ... ");
			var mIntention = (MoveIntention)e;

			var endField = getField(mIntention.destinationField);
			if (endField != null)
			{
				if (!isOccupied(endField))
				{
					Console.WriteLine("Unit is free to move ... ");

					var srcField = getField(mIntention.sourceField);
					endField.unit = srcField.unit;
					srcField.unit = null;

					return new MoveMessage(DateTime.Now,
						mIntention.playerName,
						RoomName,
						mIntention.sourceField,
						mIntention.destinationField);
				}
				else
				{
					// QUESTIONABLE 
					// Should I just stop unit so it can actually be a feature, 
					// intercepting units in order to stop them ... ? 
					// Or should i send RecalculatePath message ... ? 
					Console.WriteLine("Unit should recalculate path ... ");
					return new RecalculatePathMessage(DateTime.Now,
						mIntention.playerName,
						RoomName,
						mIntention.sourceField);
				}
			}
			else
			{
				Console.WriteLine("Move should be aborted ... ");
				return new AbortMoveMessage(DateTime.Now,
					mIntention.playerName,
					RoomName,
					mIntention.sourceField);
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

				return new AbortAttackMessage(DateTime.Now,
					e.playerName,
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
				var defeatedName = targetField.unit.owner;

				getPlayer(defeatedName).points -= config.defeatCost;
				var removePointsMsg = new PointsUpdateMessage(DateTime.Now,
					defeatedName,
					RoomName,
					-1 * config.defeatCost,
					getPlayer(defeatedName).points);
				// playerProxy.sendMessage(RoomName, defeatedName, removePointsMsg);
				sendMessage(removePointsMsg, defeatedName);

				ActiveUnits.Remove(targetField.unit);

				targetField.unit = null;
				targetField.inBattle = false;

				if (checkEndGame())
				{
					Console.WriteLine("\n This game is done ... \n");

					winner = attackIntention.playerName;

					tickTimer.Stop();

					onGameDone(this);

					getPlayer(attackIntention.playerName).points += config.winAward + config.attackAward;
					return new GameDoneMessage(DateTime.Now,
						e.playerName,
						RoomName,
						config.winAward + config.attackAward);
				}
				else
				{
					getPlayer(attackIntention.playerName).points += config.attackAward;
					var attackAwardMsg = new PointsUpdateMessage(DateTime.Now,
						attackIntention.playerName,
						RoomName,
						config.attackAward,
						getPlayer(attackIntention.playerName).points);
				}

			}

			return new AttackMessage(DateTime.Now,
				e.playerName,
				RoomName,
				attack.type.ToString(),
				attackIntention.sourceField,
				attackIntention.destinationField);
		}

		private PlayerData getPlayer(string name)
		{
			return Players.Find(p => p.username == name);
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

				getPlayer(attackerField.owner).points -= config.defeatCost;
				var defeatCostMsg = new PointsUpdateMessage(DateTime.Now,
					attackerField.owner,
					RoomName,
					-1 * config.defeatCost,
					getPlayer(attackerField.owner).points);
				sendMessage(defeatCostMsg, attackerField.owner);

				if (checkEndGame())
				{
					Console.WriteLine($"\t Game {RoomName} is dead ... ");
					winner = defendEvent.playerName;
					tickTimer.Stop();
					onGameDone(this);
					getPlayer(defendEvent.playerName).points += config.defendAward + config.winAward;

					return new GameDoneMessage(DateTime.Now,
						defendEvent.playerName,
						RoomName,
						config.defendAward + config.winAward);

				}
				else
				{
					getPlayer(defendEvent.playerName).points += config.defendAward;
					var defendAward = new PointsUpdateMessage(DateTime.Now,
						attackerField.owner,
						RoomName,
						-config.defendAward,
						getPlayer(attackerField.owner).points);
					sendMessage(defeatCostMsg, defendEvent.playerName);
				}

			}

			// return something else if unit dead or something ... 

			return new DefendMessage(DateTime.Now,
				e.playerName,
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

			return new AbortAttackMessage(DateTime.Now,
				abortIntention.playerName,
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

				return new BuildUnitMessage(DateTime.Now,
						e.playerName,
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

		public bool InitGame()
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

			var playerNames = Players.Select(pd => pd.username).ToList<string>();
			roomId = Database.saveGame(RoomName, masterPlayer.username, playerNames, DateTime.Now);
			Console.WriteLine("Saved game to db ... ");

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
					Console.WriteLine("INCOME >>> ");
					stat.TickCount = 0;
					stat.CurrentAmount += stat.Income;

					// onIncomeTick.Invoke(stat.CurrentAmount, RoomName, stat.Name);
					var tickMsg = new IncomeTickMessage(DateTime.Now, stat.Name, RoomName, stat.CurrentAmount);
					// Messages.Add(tickMsg);
					// saveMessages();

					// playerProxy.sendMessage(RoomName, stat.Name, tickMsg);
					sendMessage(tickMsg, stat.Name);
				}
			}

			tickTimer.Start();
		}

		private Unit? generateUnit(UnitType unitType)
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

		public bool HasPlayer(string name)
		{
			return this.Players.Any<PlayerData>((player) => player.username == name);
		}

		public bool IsReady()
		{
			return Players.Count >= config.MinimumPlayers;
		}

		public PlayerData AddPlayer(PlayerData player)
		{
			player.color = getAvailableColor();
			// player.points = config.DefaultPoints;
			Players.Add(player);

			return player;
		}

		public PlayerData RemovePlayer(string name)
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

		public GameSummary GetSummary()
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

		public bool IsMaster(string player)
		{
			return masterPlayer.username == player;
		}

		public void EndGame()
		{

			if (tickTimer != null)
			{
				tickTimer.Stop();
			}

			saveMessages(true);

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

		public string GetRoomName()
		{
			return this.RoomName;
		}

		public PlayerData GetMasterPlayer()
		{
			return this.masterPlayer;
		}

		public List<PlayerData> GetPlayers()
		{
			return this.Players;
		}

		public string GetRoomId()
		{
			return this.roomId;
		}

		public string GetPassword()
		{
			return this.Password;
		}

		public string GetWinner()
		{
			return this.winner;
		}
	}
}
