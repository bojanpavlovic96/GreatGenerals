
namespace RabbitGameServer.SharedModel.Messages
{
	public class CreateRoomMsg : Message
	{
		public string password { get; set; }

		public CreateRoomMsg(DateTime timestamp, string player, string roomName, string password)
			: base(MessageType.CreateRoomRequest, timestamp, player, roomName)
		{
			this.password = password;
		}


	}
}