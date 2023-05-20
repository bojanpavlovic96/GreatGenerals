
namespace RabbitGameServer.SharedModel.Messages
{
	public class ReadyForReplayMessage : Message
	{
		public ReadyForReplayMessage(DateTime timestamp, string username, string roomName) 
			: base(MessageType.ReadyForReplayMsg, timestamp, username, roomName)
		{
		}
	}
}