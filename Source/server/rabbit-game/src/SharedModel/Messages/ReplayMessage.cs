
namespace RabbitGameServer.SharedModel.Messages
{
	public class ReplayMessage : Message
	{

		public List<Message> messages { get; set; }

		public ReplayMessage(DateTime timestamp,
			string username,
			string roomName,
			DateTime startTimestamp,
			List<Message> messages)
			: base(MessageType.ReplayMessage, timestamp, username, roomName)
		{
			this.messages = messages;
		}
	}
}