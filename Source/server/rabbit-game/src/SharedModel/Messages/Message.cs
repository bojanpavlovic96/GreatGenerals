
namespace RabbitGameServer.SharedModel.Messages
{
	public class Message
	{
		public MessageType type { get; set; }

		public string username { get; set; }
		public string roomName { get; set; }

		// public DateTime timestamp {get; set;}

		public Message(MessageType type, string username, string roomName)
		{
			this.type = type;
			// this.timestamp = timestamp;

			this.username = username;
			this.roomName = roomName;
		}

	}
}