
namespace RabbitGameServer.SharedModel.Messages
{
	public class ServerErrorMessage : Message
	{

		public string message;

		public ServerErrorMessage(string username, string roomName, string message)
			: base(MessageType.ServerErrorMessage, username, roomName)
		{
			this.message = message;
		}
	}
}