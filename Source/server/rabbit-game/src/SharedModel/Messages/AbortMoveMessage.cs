
namespace RabbitGameServer.SharedModel.Messages
{
	public class AbortMoveMessage : Message
	{
		public Point2D unitPosition;

		public AbortMoveMessage(DateTime timestamp, string username, string roomName, Point2D unitPos)
			: base(MessageType.AbortMoveMessage, timestamp, username, roomName)
		{
			this.unitPosition = unitPos;
		}
	}
}