
namespace RabbitGameServer.SharedModel.Messages
{
	public class RecalculatePathMessage : Message
	{
		public Point2D unitPosition;

		public RecalculatePathMessage(string username, string roomName, Point2D unitPos)
			: base(MessageType.RecalculatePathMessage, username, roomName)
		{
			this.unitPosition = unitPos;
		}
	}
}