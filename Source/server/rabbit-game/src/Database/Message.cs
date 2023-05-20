
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Database
{
	public class Message
	{

		// [BsonId]
		// public ObjectId _id { get; set; }

		public DateTime timestamp { get; set; }

		public string roomId { get; set; }

		public string? type { get; set; }

		public string? playerName { get; set; }
		public string? roomName { get; set; }

		public List<Database.PlayerData> players { get; set; }
		public List<Database.Move> moves { get; set; }
		public List<Database.Unit> units { get; set; }
		public List<Database.Attack> attacks { get; set; }

		public List<Field> fields { get; set; }

		public Point2D? fromField { get; set; }
		public Point2D? toField { get; set; }

		public string attackType { get; set; }
		public string defendType { get; set; }

		public string unitType { get; set; }
		public int cost { get; set; }
		public string password { get; set; }

		public Message(string type,
			string roomId,
			string roomName,
			string player,
			DateTime timestamp)
		{
			this.timestamp = timestamp;
			this.type = type;
			this.roomId = roomId;
			this.roomName = roomName;
			this.playerName = player;
		}

		override public string ToString()
		{
			if (type == MessageType.InitializeMessage.ToString())
			{
				return $"initMsg ... ";
			}
			else if (type == MessageType.MoveMessage.ToString())
			{
				return $"moveMsg: {fromField} -> {toField} ... ";
			}

			return $"Unknown message ... ({type.ToString()})";
		}

	}
}