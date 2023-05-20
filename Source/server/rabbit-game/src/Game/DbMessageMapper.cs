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