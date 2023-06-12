using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Game
{
	public class DbMessageMapper
	{
		public static Database.Message map(SharedModel.Messages.Message msg, string roomId)
		{
			switch (msg.type)
			{
				case SharedModel.Messages.MessageType.InitializeMessage:
					var initMsg = (InitializeMessage)msg;
					return new RabbitGameServer.Database.Message(initMsg.type.ToString(),
						roomId,
						initMsg.roomName,
						initMsg.username,
						initMsg.timestamp)
					{
						players = initMsg.players.Select(playerToDb).ToList(),
						moves = initMsg.moves.Select(moveToDb).ToList(),
						units = initMsg.units.Select(unitToDb).ToList(),
						attacks = initMsg.attacks.Select(attackToDb).ToList(),
						fields = initMsg.fields.Select(fieldToDb).ToList()
					};

				case SharedModel.Messages.MessageType.MoveMessage:
					var mMsg = (MoveMessage)msg;
					return new RabbitGameServer.Database.Message(mMsg.type.ToString(),
						roomId,
						mMsg.roomName,
						mMsg.username,
						mMsg.timestamp)
					{
						roomId = roomId,
						type = mMsg.type.ToString(),
						playerName = mMsg.username,
						roomName = mMsg.roomName,
						fromField = Database.Point2D.fromPoint(mMsg.startFieldPos),
						toField = Database.Point2D.fromPoint(mMsg.endFieldPos)
					};
				case SharedModel.Messages.MessageType.AttackMessage:
					var aMsg = (AttackMessage)msg;
					return new RabbitGameServer.Database.Message(msg.type.ToString(),
						roomId,
						aMsg.roomName,
						aMsg.username,
						aMsg.timestamp)
					{
						attackType = aMsg.attackType.ToString(),
						fromField = Database.Point2D.fromPoint(aMsg.startFieldPos),
						toField = Database.Point2D.fromPoint(aMsg.endFieldPos)
					};

				case SharedModel.Messages.MessageType.AbortAttackMessage:
					var abMsg = (AbortAttackMessage)msg;
					return new RabbitGameServer.Database.Message(msg.type.ToString(),
						roomId,
						abMsg.roomName,
						abMsg.username,
						abMsg.timestamp)
					{
						fromField = RabbitGameServer.Database.Point2D.fromPoint(abMsg.unitPosition)
					};

				case SharedModel.Messages.MessageType.DefendMessage:
					var dMsg = (DefendMessage)msg;
					return new RabbitGameServer.Database.Message(msg.type.ToString(),
						roomId,
						dMsg.roomName,
						dMsg.username,
						dMsg.timestamp)
					{
						defendType = dMsg.defendType,
						fromField = RabbitGameServer.Database.Point2D.fromPoint(dMsg.startFieldPos),
						toField = RabbitGameServer.Database.Point2D.fromPoint(dMsg.endFieldPos)
					};

				case SharedModel.Messages.MessageType.BuildUnit:
					var bMsg = (BuildUnitMessage)msg;
					return new RabbitGameServer.Database.Message(msg.type.ToString(),
						roomId,
						bMsg.roomName,
						bMsg.username,
						bMsg.timestamp)
					{
						fromField = Database.Point2D.fromPoint(bMsg.field),
						unitType = bMsg.unitType,
						cost = bMsg.cost
					};

				case SharedModel.Messages.MessageType.IncomeTick:
					Console.WriteLine("Mapping income tick to db model ... ");
					var iMsg = (IncomeTickMessage)msg;
					return new RabbitGameServer.Database.Message(iMsg.type.ToString(),
						roomId,
						iMsg.roomName,
						iMsg.username,
						iMsg.timestamp)
					{
						amount = iMsg.amount
					};

				case SharedModel.Messages.MessageType.GameDone:
					var gMsg = (GameDoneMessage)msg;
					return new RabbitGameServer.Database.Message(gMsg.type.ToString(),
						roomId,
						gMsg.roomName,
						gMsg.username,
						gMsg.timestamp)
					{
						amount = gMsg.bonusAmount
					};

				case SharedModel.Messages.MessageType.RemovePlayer:
					var rMsg = (RemovePlayerMessage)msg;
					return new RabbitGameServer.Database.Message(rMsg.type.ToString(),
						roomId,
						rMsg.roomName,
						rMsg.username,
						rMsg.timestamp)
					{
						whoLeft = rMsg.whoLeft
					};
					return null;


			}

			return null;
		}

		public static SharedModel.Messages.Message map(Database.Message msg)
		{
			Console.WriteLine("Mapping dbMessage to sharedMessage .... ");
			switch (Enum.Parse(typeof(MessageType), msg.type))
			{
				case MessageType.InitializeMessage:
					Console.WriteLine("Mapping init msg ... ");
					return new InitializeMessage(msg.timestamp,
						msg.roomName,
						msg.playerName,
						msg.players.Select(playerToShare).ToList(),
						msg.moves.Select(moveToShare).ToList(),
						msg.units.Select(unitToShare).ToList(),
						msg.attacks.Select(attackToShare).ToList(),
						msg.fields.Select(fieldToShare).ToList());

				case MessageType.MoveMessage:
					Console.WriteLine("Mapping move msg ... ");
					return new MoveMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.fromField.toPoint(),
						msg.toField.toPoint());

				case MessageType.AttackMessage:
					return new AttackMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.attackType,
						msg.fromField.toPoint(),
						msg.toField.toPoint());

				case MessageType.AbortAttackMessage:
					return new AbortAttackMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.fromField.toPoint());

				case MessageType.DefendMessage:
					return new DefendMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.defendType,
						msg.fromField.toPoint(),
						msg.toField.toPoint());

				case MessageType.IncomeTick:
					return new IncomeTickMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.amount);

				case MessageType.BuildUnit:
					return new BuildUnitMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.fromField.toPoint(),
						msg.unitType,
						msg.cost);

				case MessageType.GameDone:
					return new GameDoneMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.amount);

				case MessageType.RemovePlayer:
					return new RemovePlayerMessage(msg.timestamp,
						msg.playerName,
						msg.roomName,
						msg.whoLeft);

			}
			return null;
		}

		public static Database.PlayerData playerToDb(SharedModel.PlayerData pd)
		{
			return new Database.PlayerData(pd.username,
									pd.color,
									pd.level,
									pd.points);
		}

		public static Database.Move moveToDb(SharedModel.Move m)
		{
			return new Database.Move(m.type.ToString(),
						m.range,
						m.speed,
						m.terrainMultiplier);
		}

		public static Database.Unit unitToDb(SharedModel.Unit u)
		{
			return new Database.Unit(u.owner,
						u.unitName.ToString(),
						u.moveType.ToString(),
						u.attacks,
						u.defense,
						u.health,
						u.cost);
		}

		public static Database.Attack attackToDb(SharedModel.Attack a)
		{
			return new Database.Attack(a.type.ToString(),
						a.attackDmg,
						a.attackCooldown,
						a.attackRange,
						a.defenseDmg,
						a.defenseCooldown,
						a.defenseRange,
						a.duration);
		}

		public static Database.Field fieldToDb(SharedModel.Field f)
		{
			return new Database.Field(Database.Point2D.fromPoint(f.position),
						f.isVisible,
						f.unitName.ToString(),
						new Database.Terrain(f.terrain.type.ToString(), f.terrain.intensity),
						f.inBattle,
						f.owner);
		}

		public static SharedModel.PlayerData playerToShare(Database.PlayerData dp)
		{
			return new PlayerData(dp.username, dp.color, dp.level, dp.points);
		}

		public static SharedModel.Move moveToShare(Database.Move dm)
		{
			return new SharedModel.Move()
			{
				type = (MoveType)Enum.Parse(typeof(MoveType), dm.type),
				range = dm.range,
				speed = dm.speed,
				terrainMultiplier = dm.terrainMultiplier
			};
		}

		public static SharedModel.Unit unitToShare(Database.Unit dUnit)
		{
			return new SharedModel.Unit()
			{
				owner = dUnit.owner,
				unitName = (UnitType)Enum.Parse(typeof(UnitType), dUnit.unitName),
				moveType = (MoveType)Enum.Parse(typeof(MoveType), dUnit.moveType),
				attacks = dUnit.attacks,
				defense = dUnit.defense,
				health = dUnit.health,
				cost = dUnit.cost

			};
		}

		public static SharedModel.Attack attackToShare(Database.Attack dAtt)
		{
			return new SharedModel.Attack()
			{
				type = (AttackType)Enum.Parse(typeof(AttackType), dAtt.type),
				attackDmg = dAtt.attackDmg,
				attackCooldown = dAtt.attackCooldown,
				attackRange = dAtt.attackRange,
				defenseDmg = dAtt.defenseDmg,
				defenseCooldown = dAtt.defenseCooldown,
				defenseRange = dAtt.defenseRange,
				duration = dAtt.duration
			};
		}

		public static SharedModel.Field fieldToShare(Database.Field df)
		{
			var unitExists = !String.IsNullOrEmpty(df.unitName);
			return new SharedModel.Field()
			{
				position = new SharedModel.Point2D(df.position.x, df.position.y),
				isVisible = df.isVisible,
				unitName = unitExists ? parseType(df.unitName) : null,
				terrain = terrainToShare(df.terrain),
				inBattle = df.inBattle,
				owner = df.owner
			};
		}

		private static UnitType parseType(string strType)
		{
			return (UnitType)Enum.Parse(typeof(UnitType), strType);
		}

		public static SharedModel.Terrain terrainToShare(Database.Terrain dt)
		{
			var enType = (TerrainType)Enum.Parse(typeof(TerrainType), dt.type);
			return new Terrain(enType, dt.intensity);
		}

	}
}