
namespace RabbitGameServer.SharedModel.Messages
{
	public class ServerErrorMessage : Message
	{

		public string message;

		public ServerErrorMessage(DateTime timestamp, string username, string roomName, string message)
			: base(MessageType.ServerErrorMessage, timestamp, username, roomName)
		{
			this.message = message;
		}
	}
}