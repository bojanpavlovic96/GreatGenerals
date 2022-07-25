using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace RabbitGameServer.Database
{
	public class Game
	{
		[BsonId]
		public ObjectId id { get; set; }

		public string roomName { get; set; }
		public List<string> playerIds { get; set; }

		public DateTime startTime { get; set; }

		public List<Message> messages { get; set; }

		public Game(string roomName, List<string> players)
		{
			this.roomName = roomName;
			this.playerIds = players;

			this.startTime = DateTime.Now;
			this.messages = new List<Message>();
		}

	}

}