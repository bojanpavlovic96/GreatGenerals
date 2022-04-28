
namespace RabbitGameServer.SharedModel.Messages
{
	public class CreateGameMsg : Message
	{
		public static string Name = "create-game-msg";

		public CreateGameMsg(string player, string roomName)
			: base(CreateGameMsg.Name, player, roomName)
		{
			// room name in this case is wanted room name 
		}


	}
}