
namespace RabbitGameServer.SharedModel.Messages
{
	public class ReadyForInitMessage : Message
	{
		public ReadyForInitMessage(string player, string roomName) 
            : base(MessageType.ReadyForInitMsg, player, roomName)
		{
		}
	}
}