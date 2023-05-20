
namespace RabbitGameServer.SharedModel.Messages
{
	public class ReadyForInitMessage : Message
	{
		public ReadyForInitMessage(DateTime timestamp, string player, string roomName) 
            : base(MessageType.ReadyForInitMsg,timestamp, player, roomName)
		{
		}
	}
}