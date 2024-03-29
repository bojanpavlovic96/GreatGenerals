namespace RabbitGameServer.SharedModel.Messages
{
	public class DefendMessage : Message
	{

		public string defendType { get; set; }

		public Point2D startFieldPos { get; set; }
		public Point2D endFieldPos { get; set; }

		public DefendMessage(DateTime timestamp,
			string username,
			string roomName,
			string defendType,
			Point2D startFieldPos,
			Point2D endFieldPos)
			: base(MessageType.DefendMessage, timestamp, username, roomName)
		{
			this.defendType = defendType;
			this.startFieldPos = startFieldPos;
			this.endFieldPos = endFieldPos;
		}

	}
}