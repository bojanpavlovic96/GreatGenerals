namespace RabbitGameServer.SharedModel.Messages
{
	public class MoveCmdMessage : Message
	{

		public static string Name = "move-cmd-msg";

		public Point2D startFieldPos;
		public Point2D endFieldPos;

		public MoveCmdMessage(
			string roomName,
			string player,
			Point2D startFieldPos,
			Point2D endFieldPos)
			: base(InitializeCmdMessage.Name, player, roomName)
		{

			this.startFieldPos = startFieldPos;
			this.endFieldPos = endFieldPos;
		}
	}
}