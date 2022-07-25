
namespace RabbitGameServer.SharedModel.Messages
{
	public class CreateRoomMsg : Message
	{
		public string password { get; set; }

		public CreateRoomMsg(string player, string roomName, string password)
			: base(MessageType.CreateRoomRequest, player, roomName)
		{
			this.password = password;
		}


	}
}