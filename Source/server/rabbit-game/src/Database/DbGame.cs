using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace RabbitGameServer.Database
{

	public class DbGame
	{
		[BsonId]
		public ObjectId _id { get; set; }

		public string roomName { get; set; }
		public string masterPlayer { get; set; }
		public List<string> players { get; set; }

		public DateTime startTime { get; set; }
		public DateTime endTime { get; set; }

		public string winner { get; set; }
		public bool isDone { get; set; }

		public int pointsGain { get; set; }

		public DbGame(string roomName,
			string masterPlayer,
			List<string> players,
			DateTime startedAt)
		{
			this.roomName = roomName;
			this.masterPlayer = masterPlayer;
			this.players = players;

			this.startTime = startedAt;

			this.isDone = false;
		}

	}

}