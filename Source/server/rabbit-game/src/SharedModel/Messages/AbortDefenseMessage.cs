
namespace RabbitGameServer.SharedModel.Messages
{
	public class AbortDefenseMessage : Message
	{
		public Point2D sourcePosition { get; set; }

		public AbortDefenseMessage(DateTime timestamp,Point2D sourcePos, string username, string roomName)
			: base(MessageType.AbortDefenseMessage, timestamp, username, roomName)
		{
			this.sourcePosition = sourcePos;
		}
	}
}