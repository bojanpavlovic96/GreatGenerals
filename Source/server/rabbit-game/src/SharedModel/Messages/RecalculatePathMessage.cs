
namespace RabbitGameServer.SharedModel.Messages
{
	public class RecalculatePathMessage : Message
	{
		public Point2D unitPosition;

		public RecalculatePathMessage(DateTime timestamp, string username, string roomName, Point2D unitPos)
			: base(MessageType.RecalculatePathMessage, timestamp, username, roomName)
		{
			this.unitPosition = unitPos;
		}
	}
}