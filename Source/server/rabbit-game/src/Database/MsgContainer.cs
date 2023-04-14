using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace RabbitGameServer.Database
{
	public class MsgContainer
	{
		[BsonId]
		public ObjectId _id { get; set; }

		public string roomId { get; set; }
		public List<Message> messages { get; set; }

		public MsgContainer(string roomId)
		{
			this.roomId = roomId;
			this.messages = new List<Message>();
		}
	}
}