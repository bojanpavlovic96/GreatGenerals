
namespace RabbitGameServer.SharedModel.Messages
{
	public class JoinRoomMessage : Message
	{
		public string password { get; set; }

		public JoinRoomMessage(DateTime timestamp, string player, string roomName, string password)
			: base(MessageType.JoinRoomRequest, timestamp, player, roomName)
		{
			this.password = password;
		}
	}
}