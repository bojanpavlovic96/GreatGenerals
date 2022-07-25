
namespace RabbitGameServer.SharedModel.Messages
{
	public class JoinRoomMessage : Message
	{
		public string password { get; set; }

		public JoinRoomMessage(string player, string roomName, string password)
			: base(MessageType.JoinRoomRequest, player, roomName)
		{
			this.password = password;
		}
	}
}