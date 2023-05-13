
namespace RabbitGameServer.SharedModel.Messages
{
	public class ReadyForReplayMessage : Message
	{
		public ReadyForReplayMessage(string username, string roomName) 
			: base(MessageType.ReadyForReplayMsg, username, roomName)
		{
		}
	}
}