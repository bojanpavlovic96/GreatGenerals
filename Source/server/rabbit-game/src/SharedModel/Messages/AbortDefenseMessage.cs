
namespace RabbitGameServer.SharedModel.Messages
{
	public class AbortDefenseMessage : Message
	{
		public Point2D sourcePosition { get; set; }

		public AbortDefenseMessage(Point2D sourcePos, string username, string roomName)
			: base(MessageType.AbortDefenseMessage, username, roomName)
		{
			this.sourcePosition = sourcePos;
		}
	}
}