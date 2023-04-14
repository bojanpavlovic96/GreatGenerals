using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Game
{
	public class DbMessageMapper
	{
		public static Database.Message map(Message msg, string roomId)
		{
			switch (msg.type)
			{
				case SharedModel.Messages.MessageType.InitializeMessage:
					var initMsg = (InitializeMessage)msg;
					return new RabbitGameServer.Database.Message(initMsg.type.ToString(),
						roomId,
						initMsg.roomName,
						initMsg.username)
					{
						// roomId = roomId,
						// type = initMsg.type.ToString(),
						// playerName = initMsg.username,
						// roomName = initMsg.roomName,
						players = initMsg
							.players
							.Select(pd => new Database.PlayerData(pd.username,
															pd.color,
															pd.level,
															pd.points))
							.ToList(),
						moves = initMsg
							.moves
							.Select(m => new Database.Move(m.type.ToString(),
													m.range,
													m.speed,
													m.terrainMultiplier))
							.ToList(),
						units = initMsg
							.units
							.Select(u => new Database.Unit(u.owner,
													u.unitName.ToString(),
													u.moveType.ToString(),
													u.attacks,
													u.defense,
													u.health,
													u.cost))
							.ToList(),
						attacks = initMsg
							.attacks
							.Select(a => new Database.Attack(a.type.ToString(),
														a.attackDmg,
														a.attackCooldown,
														a.attackRange,
														a.defenseDmg,
														a.defenseCooldown,
														a.defenseRange,
														a.duration))
							.ToList(),
						fields = initMsg
							.fields
							.Select(f => new Database.Field(Database.Point2D.fromPoint(f.position),
													f.isVisible,
													f.unitName.ToString(),
													f.terrain.ToString(),
													f.inBattle,
													f.owner))
							.ToList()

					};

				case SharedModel.Messages.MessageType.MoveMessage:
					var mMsg = (MoveMessage)msg;
					return new RabbitGameServer.Database.Message(mMsg.type.ToString(),
						roomId,
						mMsg.roomName,
						mMsg.username)
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
	}
}