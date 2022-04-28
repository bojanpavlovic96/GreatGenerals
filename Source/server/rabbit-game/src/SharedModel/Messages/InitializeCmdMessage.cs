namespace RabbitGameServer.SharedModel.Messages
{
	public class InitializeCmdMessage : Message
	{

		public static string Name = "initialize-cmd-msg";

		public List<PlayerData> players;
		public List<Field> fields;

		public InitializeCmdMessage(
			string roomName,
			List<PlayerData> players,
			List<Field> fields)
			: base(InitializeCmdMessage.Name, "", roomName)
		{

			this.players = players;
			this.fields = fields;
		}
	}
}