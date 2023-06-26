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

	class DecoratedPlayer
	{
		private PlayerData data;

		public string username { get { return data.username; } set { data.username = value; } }
		public Color color { get { return data.color; } set { data.color = value; } }
		public int level { get { return data.level; } set { data.level = value; } }
		public int points { get { return data.points; } set { data.points = value; } }


		public bool isAlive { get; set; }

		public DecoratedPlayer(PlayerData data)
		{
			this.data = data;
			this.isAlive = true;
		}

		public DecoratedPlayer(PlayerData data, bool isAlive)
		{
			this.data = data;
			this.isAlive = isAlive;
		}

		public static PlayerData justPlayer(DecoratedPlayer dp)
		{
			return dp.data;
		}
	}

	public class LiveMaster : GameMaster
	{
		private GameConfig config;

		private PlayerData masterPlayer { get; set; }

		private string roomId { get; set; }

		private string RoomName { get; set; }
		private string Password { get; set; }
		private List<DecoratedPlayer> Players { get; set; }
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
					GameDoneHandler onGameDone)
		{
			this.config = config;
			parseColors();

			this.RoomName = roomName;
			this.Password = password;

			this.masterPlayer = masterPlayer;
			this.masterPlayer.coins = config.defaultCoins;
			this.masterPlayer.color = getAvailableColor();
			this.Players = new List<DecoratedPlayer>();
			this.Players.Add(new DecoratedPlayer(masterPlayer));
			this.winner = null;

			this.playerProxy = playerProxy;

			this.ActiveUnits = new List<Unit>();

			this.recEventsCnt = 0;
			this.sendEventsCnt = 0;

			this.Database = db;
			this.Messages = new List<Message>();

			this.onGameDone += onGameDone;

			this.Fields = new Dictionary<Point2D, Field>();
			this.initHandlers();
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
			handlers.Add(ClientIntentionType.LeaveGame, handleLeaveGame);
		}

		public void AddIntention(ClientIntention intention)
		{
			Console.WriteLine($"Handling: {intention.GetType().ToString()} ... ");
			recEventsCnt++;

			IntentionHandler? handler;
			if (handlers.TryGetValue(intention.type, out handler))
			{
				// TODO add if not null because of the playerLEftHandler
				var resultMsg = handler.Invoke(intention);

				if(resultMsg!=null){
				sendMessage(resultMsg, intention.playerName);
				}

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

			// income ticks
			tickTimer.Start();

			return new InitializeMessage(DateTime.Now,
				RoomName,
				e.playerName,
				Players.Select(DecoratedPlayer.justPlayer).ToList(),
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
				var defeated = getPlayer(targetField.unit.owner);

				defeated.points -= config.defeatCost;
				var removePointsMsg = new PointsUpdateMessage(DateTime.Now,
					defeated.username,
					RoomName,
					-1 * config.defeatCost,
					getPlayer(defeated.username).points);

				sendMessage(removePointsMsg, defeated.username);

				ActiveUnits.Remove(targetField.unit);

				if (getUnits(defeated.username).Count == 0)
				{
					getPlayer(defeated.username).isAlive = false;
				}

				targetField.unit = null;
				targetField.inBattle = false;

				if (checkEndGame())
				{
					Console.WriteLine("\n This game is done ... \n");

					winner = attackIntention.playerName;

					tickTimer.Stop();

					var pointsGain = config.winAward + config.attackAward;

					getPlayer(attackIntention.playerName).points += pointsGain;

					Database.updateGame(roomId, winner, DateTime.Now, pointsGain);

					onGameDone(this);

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

		private List<Unit> getUnits(string user)
		{
			return ActiveUnits.FindAll(u => u.owner == user).ToList();
		}

		private DecoratedPlayer getPlayer(string name)
		{
			return Players.Find(p => p.username == name);
		}

		private IncomeStats getIncomes(string name)
		{
			return Incomes.Find(inc => inc.Name == name);
		}

		private void removeIncome(string name)
		{
			Incomes.RemoveAll(inc => inc.Name == name);
		}

		private bool checkEndGame()
		{

			return Players.FindAll(p => p.isAlive).Count <= 1;

			int playersAlive = 0;
			foreach (var player in Players.FindAll(p => p.isAlive))
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

					var pointsGain = config.defendAward + config.winAward;
					getPlayer(defendEvent.playerName).points += pointsGain;

					Database.updateGame(roomId, winner, DateTime.Now, pointsGain);

					onGameDone(this);

					return new GameDoneMessage(DateTime.Now,
						defendEvent.playerName,
						RoomName,
						pointsGain);

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

			Console.WriteLine($"req: {unitDesc.cost} have: {stats.CurrentAmount}");

			if (field.unit == null && stats.CurrentAmount >= unitDesc.cost)
			{
				Console.WriteLine("Will build unit ... ");
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
			else
			{
				Console.WriteLine("Won't build unit ... ");
			}

			return null;
		}

		private Unit getUnit(UnitType type)
		{
			return config.Units.Find(u => u.unitName == type);
		}

		private Message handleLeaveGame(ClientIntention e)
		{
			var leftPlayer = getPlayer(e.playerName);
			leftPlayer.isAlive = false;
			leftPlayer.points -= config.defeatCost;

			removeIncome(e.playerName);

			foreach (var player in Players.FindAll(p => p.isAlive))
			{
				player.points += config.defeatAward;
				var pointsUpdate = new PointsUpdateMessage(DateTime.Now,
					player.username,
					RoomName,
					config.defeatAward,
					player.points);

				sendMessage(pointsUpdate, player.username);
				// playerProxy.sendMessage(RoomName, player.username, update);
			}

			Fields.Values.ToList()
				.FindAll(f => f.unit != null && f.unit.owner == leftPlayer.username)
				.ForEach(f =>
				{
					ActiveUnits.Remove(f.unit);
					f.unit = null;
				});


			if (checkEndGame())
			{
				winner = Players.Find(p => p.isAlive).username; // has to be only one 
				Console.WriteLine($"Winner is: {winner}");

				tickTimer.Stop();
				onGameDone(this);

				var pointsGain = config.winAward;
				Database.updateGame(roomId, winner, DateTime.Now, pointsGain);

				var endMessage = new GameDoneMessage(DateTime.Now,
					winner,
					RoomName,
					pointsGain);

				return endMessage;
			}
			else
			{
				foreach (var alive in Players.FindAll(p => p.isAlive))
				{
					var msg = new RemovePlayerMessage(DateTime.Now, alive.username, RoomName, leftPlayer.username);
					sendMessage(msg, alive.username);
				}
				return null;
			}

		}

		// endregion 

		public bool InitGame()
		{

			int left = 3;
			int right = 17;
			int rows = 16;

			var random = new Random();

			for (int i = 1; i < rows; i++)
			{

				int segLen = (right - left) / Players.Count;
				for (int j = left; j < right; j++)
				{

					var point = new Point2D(j, i);

					var rand = random.NextInt64();
					Terrain? terrain = null;
					// if (i % 2 == 0 && j % 5 == 0)
					if (rand % 3 == 0)
					{
						terrain = new Terrain(TerrainType.water, 1);
					}
					else
					{
						terrain = new Terrain(TerrainType.mountains, 1);
					}

					var playerInd = (j - left) / segLen;

					if (playerInd >= Players.Count)
					{
						playerInd = Players.Count - 1;
					}
					else if (playerInd < 0)
					{
						playerInd = 0;
					}

					var playerName = Players[playerInd].username;

					var newField = new Field(point,
						true,
						null,
						terrain,
						playerName,
						false);

					if (config.DefaultPositions.Contains(point))
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
						config.defaultCoins,
						config.requiredIncomeTicks,
						config.incomeAmount));
			}

			tickTimer = new System.Timers.Timer();
			tickTimer.Elapsed += this.tickHandler;
			tickTimer.Interval = config.tickTime;
			tickTimer.AutoReset = false;

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
			return this.Players.Any<DecoratedPlayer>((player) => player.username == name);
		}

		public bool IsReady()
		{
			return Players.Count >= config.MinimumPlayers;
		}

		public PlayerData AddPlayer(PlayerData player)
		{
			player.color = getAvailableColor();
			player.coins = config.defaultCoins;
			Players.Add(new DecoratedPlayer(player));

			return player;
		}

		public PlayerData RemovePlayer(string name)
		{
			var player = Players.Find(p => p.username == name);

			availableColors.Add(player.color);

			Players.Remove(player);

			return DecoratedPlayer.justPlayer(player);
		}

		public GameSummary GetSummary()
		{

			return new GameSummary(RoomName,
				masterPlayer.username,
				Players.Count,
				Players
					.Select<DecoratedPlayer, string>((player) => player.username)
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
			return Players.Select(p => DecoratedPlayer.justPlayer(p)).ToList();
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
