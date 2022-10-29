namespace RabbitGameServer.SharedModel.Messages
{

	// Used both for client->server modelEvent "encapsulation"
	// and server->client command "encapsulation"

	public class MoveMessage : Message
	{

		public Point2D startFieldPos { get; set; }
		public Point2D endFieldPos { get; set; }

		public MoveMessage(string player,
			string roomName,
			Point2D startFieldPos,
			Point2D endFieldPos)
			: base(MessageType.MoveMessage, player, roomName)
		{

			this.startFieldPos = startFieldPos;
			this.endFieldPos = endFieldPos;
		}
	}
}