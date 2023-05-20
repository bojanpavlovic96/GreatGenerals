
namespace RabbitGameServer.SharedModel.Messages
{

	// Used both for client->server modelEvent "encapsulation"
	// and server->client command "encapsulation"
	public class AttackMessage : Message
	{
		public string attackType { get; set; }

		public Point2D startFieldPos { get; set; }
		public Point2D endFieldPos { get; set; }

		public AttackMessage(DateTime timestamp,
			string username,
			string roomName,
			string attackType,
			Point2D startFieldPos,
			Point2D endFieldPos)
			: base(MessageType.AttackMessage, timestamp, username, roomName)
		{
			this.attackType = attackType;
			this.startFieldPos = startFieldPos;
			this.endFieldPos = endFieldPos;
		}
	}
}