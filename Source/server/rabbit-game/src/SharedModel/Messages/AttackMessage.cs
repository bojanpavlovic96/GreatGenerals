
namespace RabbitGameServer.SharedModel.Messages
{

	// Used both for client->server modelEvent "encapsulation"
	// and server->client command "encapsulation"
	public class AttackMessage : Message
	{

		public Point2D startFieldPos { get; set; }
		public Point2D endFieldPos { get; set; }

		public AttackMessage(string username,
			string roomName,
			Point2D startFieldPos,
			Point2D endFieldPos)
			: base(MessageType.AttackMessage, username, roomName)
		{
			this.startFieldPos = startFieldPos;
			this.endFieldPos = endFieldPos;
		}
	}
}